package com.ctsi.ssdc.controller;

import com.ctsi.ssdc.cache.LoginCache;
import com.ctsi.ssdc.admin.constants.PermisionCodeConstant;
import com.ctsi.ssdc.admin.constants.TenantStatusConstant;
import com.ctsi.ssdc.admin.domain.CscpTenant;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusDTO;
import com.ctsi.ssdc.admin.service.CscpMenusService;
import com.ctsi.ssdc.admin.service.CscpTenantService;
import com.ctsi.ssdc.admin.service.CscpUserService;
import com.ctsi.ssdc.admin.service.UserService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.captcha.anno.BiyiCaptcha;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.CscpUserForm;
import com.ctsi.ssdc.security.CasHxAuthenticationToken;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.security.UserLoginValidator;
import com.ctsi.ssdc.security.jwt.JWTHxConfigurer;
import com.ctsi.ssdc.security.jwt.TokenHxProvider;
import com.ctsi.ssdc.util.RSAUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller to authenticate users.
 * @author hx
 */
@RestController
@RequestMapping("/api/system")
public class UserJwtController {

    private final Logger logger = LoggerFactory.getLogger(UserJwtController.class);

    private static final String UNAUTHORIZED = "Unauthorized";

    private final UserService userService;

    @Autowired(required = false)
    UserLoginValidator userLoginValidator;

    @Autowired
    CscpUserService cscpUserService;

    private final CscpMenusService cscpMenusService;

    @Value("${ctsi.RSA-prikey:}")
    private String rsaPrikey = "";

    @Value("${ctsi.login.bad-password-attempts:5}")
    private int badPasswordAttempts;

    @Value("${ctsi.login.captcha-attempts:3}")
    private int captchaAttempts;

    @Value("${ctsi.login.lockout-time:3600}")
    private int lockoutTime;

    private final TokenHxProvider tokenHxProvider;

    private final AuthenticationManager authenticationManager;

//	@Resource(name = "redisUtil")
//	private RedisUtil redisUtil;

    @Resource(name = "${ctsi.login.cache:guavaLoginCache}")
    LoginCache loginCache;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CscpTenantService cscpTenantService;

    public UserJwtController(TokenHxProvider tokenHxProvider,
                             AuthenticationManager authenticationManager,
                             CscpMenusService cscpMenusService, UserService userService) {
        this.tokenHxProvider = tokenHxProvider;
        this.authenticationManager = authenticationManager;
        this.cscpMenusService = cscpMenusService;
        this.userService = userService;
    }

    //	@BeanExposeMethodAble(component = ADMIN,method = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "biyiCaptchaKey", value = "验证码key", paramType = "header",
                    dataType = "String", defaultValue = ""),
            @ApiImplicitParam(name = "biyiCaptcha", value = "验证码", paramType = "header",
                    dataType = "String", defaultValue = "{\"code\":\"1234\"}"),
            @ApiImplicitParam(name = "captchaVerification", value = "行为路径", paramType = "header",
                    dataType = "String")
    })
    @BiyiCaptcha(rule = "defaultRule", service = "digitalCaptchaServiceImpl")
    @PostMapping(value = "/login")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN, method = "authorize")
    public ResponseEntity<JwtToken> authorize(HttpServletRequest request, @RequestBody CscpUserForm user) {
        //租户id
        String tenantAccount = user.getTenantAccount();

        String userName = user.getUsername();
        //前端使用公钥进行加密，服务端私钥解密,私钥为空不解密
        String password = decryptPassword(user.getPassword());
        boolean rememberMe = user.getRememberme() == 1;

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("loginFailed:");
        stringBuffer.append(userName);
        String countKey = stringBuffer.toString();
        //countKey = loginFailed:用户名
        Integer countKeyValue = null;
        if (loginCache != null) {
            //countKeyValue缓存的次数
            countKeyValue = (Integer) loginCache.get(countKey);
            if (countKeyValue != null && countKeyValue == badPasswordAttempts) {

                //expire过期时间
                long expire = loginCache.getExpire(countKey);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new JwtToken(captchaAttempts, (int) expire, 0, badPasswordAttempts));
            }
        }
        //判断租户id是否存在，判断是否过期,判断用户和租户是否匹配
        int ret = cscpTenantService.checkTenantAccount(tenantAccount, user.getUsername());
        if (ret == TenantStatusConstant.TENANT_NULL) {
            throw new BadRequestAlertException("租户不存在", null, null);
        } else if (ret == TenantStatusConstant.TENANT_EXPIRED) {
            throw new BadRequestAlertException("租户已过期", null, null);
        } else if (ret == TenantStatusConstant.USER_IN_TENANT_NOT_EXIST) {
            throw new BadRequestAlertException("用户不存在", null, null);
        }
        CscpTenant cscpTenant = cscpTenantService.selectByTenantAccount(tenantAccount);
        String jwt;

//		if (!cscpUserService.existByUsername(userName)) {
//			throw new BadRequestAlertException("userName is not exist", "username", "notexist");
//		}

        // 自定义验证
        if (userLoginValidator != null) {
            userLoginValidator.validate(user);
        }
//		String principal = cscpTenant.getId() +","+userName;
//		// security
//		UsernamePasswordAuthenticationToken authenticationToken =
//				new UsernamePasswordAuthenticationToken(principal,
//				password);
        String principal = cscpTenant.getId() + "," + cscpTenant.getTenantAccount() + "," + userName;
        // security
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principal,
                        password);
        Authentication authentication = null;
        try {
            authentication = this.authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            if (loginCache != null) {
                int countKeyLeft = -1;
                if (countKeyValue == null) {
                    loginCache.put(countKey, 1);//lockoutTime /6
                    countKeyLeft = badPasswordAttempts - 1;
                } else if (countKeyValue < badPasswordAttempts) {
                    loginCache.put(countKey, countKeyValue + 1);
                    countKeyLeft = badPasswordAttempts - 1 - countKeyValue;
                    if (countKeyLeft == 0) {
                        loginCache.put(countKey, badPasswordAttempts);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                                body(new JwtToken(captchaAttempts, lockoutTime, countKeyLeft,badPasswordAttempts));
                    } else if (badPasswordAttempts-countKeyLeft >= captchaAttempts) {
                        loginCache.put(countKey, countKeyValue + 1);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtToken(captchaAttempts, countKeyLeft, badPasswordAttempts));
                    }
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtToken(captchaAttempts, countKeyLeft, badPasswordAttempts));
            }
            throw e;
        }
        //登陆成功，删除countKey
        if (loginCache != null && countKeyValue != null) {
            loginCache.delete(countKey);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        jwt = tokenHxProvider.createToken(authentication, rememberMe);

        SecurityHxUtils.getOptionalCurrentUserId().map(userId -> {
            userService.updateUserDetailForLogin(String.valueOf(userId));
            return null;
        });

        HttpHeaders httpHeaders = new HttpHeaders();
        String token = JWTHxConfigurer.AUTHORIZATION_BEARER + jwt;

        int status = cscpUserService.passwordNeedChange(SecurityHxUtils.getCurrentUserId(), password);
        PermisionCodeConstant.permisionCodeList = cscpMenusService.findByUserId(SecurityHxUtils.getCurrentUserId());
        if (status == 0) {
            httpHeaders.add(JWTHxConfigurer.AUTHORIZATION_HEADER, token);
            return new ResponseEntity<>(new JwtToken(token, status), httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new JwtToken("", status), httpHeaders, HttpStatus.OK);
        }

    }

    @GetMapping(value = "/getCaptchaAttempts")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN, method = "getCaptchaAttempts")
    public ResponseEntity<JwtToken> getCaptchaAttempts(){
        return ResponseEntity.status(HttpStatus.OK).body(new JwtToken(captchaAttempts, 0, badPasswordAttempts));
    }

    @GetMapping(value = "/getCountKeyValue/{userName}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN, method = "getCountKeyValue")
    public ResponseEntity<JwtToken> getCountKeyValue(@PathVariable("userName") String userName) {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("loginFailed:");
        stringBuffer.append(userName);
        String countKey = stringBuffer.toString();
        //countKey = loginFailed:用户名
        Integer countKeyValue = null;
        if (loginCache != null) {
            //countKeyValue缓存的次数
            countKeyValue = (Integer) loginCache.get(countKey);
            if (countKeyValue == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtToken(captchaAttempts, 0, badPasswordAttempts));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtToken(captchaAttempts, countKeyValue, badPasswordAttempts));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtToken(captchaAttempts, 0, badPasswordAttempts));
    }

    @GetMapping(value = "/refreshToken")
//	@BeanExposeMethodAble(component = ADMIN,method = METHOD)
//	@ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "refreshToken")
    public ResponseEntity<JwtToken> refreshToken(@RequestParam int rememberme) {

        boolean rememberMe = rememberme == 1;

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<CscpMenusDTO> menusList = cscpMenusService.findByUserId(SecurityHxUtils.getCurrentUserId());
        PermisionCodeConstant.permisionCodeList = menusList;
        menusList.forEach(menu -> {

            if (StringUtils.isNotEmpty(menu.getPermissionCode())) {
                grantedAuthorities.add(new SimpleGrantedAuthority(menu.getPermissionCode()));
            }

        });

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
                currentAuth.getPrincipal(), currentAuth.getCredentials(), grantedAuthorities);

        String jwt = tokenHxProvider.createToken(newAuthentication, rememberMe);

        HttpHeaders httpHeaders = new HttpHeaders();
        String token = JWTHxConfigurer.AUTHORIZATION_BEARER + jwt;

        httpHeaders.add(JWTHxConfigurer.AUTHORIZATION_HEADER, token);
        return new ResponseEntity<>(new JwtToken(token), httpHeaders, HttpStatus.OK);

    }

    @PostMapping(value = "/loginByCas")
//	@BeanExposeMethodAble(component = ADMIN,method = METHOD)
    @ComponentCallAnno(component = HxComponentConstant.ADMIN, method = "loginByCas")
    public ResponseEntity<JwtToken> loginByCas(@RequestParam String serviceUrl, @RequestParam String ticket) {

        boolean rememberMe = false;

        CasHxAuthenticationToken authenticationToken = new CasHxAuthenticationToken(serviceUrl, ticket, null);

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenHxProvider.createToken(authentication, rememberMe);

        SecurityHxUtils.getOptionalCurrentUserId().map(userId -> {
            userService.updateUserDetailForLogin(String.valueOf(userId));
            return null;
        });

        HttpHeaders httpHeaders = new HttpHeaders();
        String token = JWTHxConfigurer.AUTHORIZATION_BEARER + jwt;

        httpHeaders.add(JWTHxConfigurer.AUTHORIZATION_HEADER, token);
        return new ResponseEntity<>(new JwtToken(token), httpHeaders, HttpStatus.OK);
    }

    /**
     * 解密
     *
     * @param ciphertext 密码的密文
     * @return
     */
    private String decryptPassword(String ciphertext) {
        String password = ciphertext;
        try {
            if (StringUtils.isNotBlank(rsaPrikey)) {
                password = new String(
                        RSAUtil.decryptPri(Base64.decodeBase64(ciphertext),
                                Base64.decodeBase64(rsaPrikey)), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return password;
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    public static class JwtToken {

        private String token;

        //
        private int passwordStatus;

        private int lockoutTime;

        private int attempts;

        private int captchaAttempts;

        private int badPasswordAttempts;

        JwtToken(String token) {
            this.token = token;
        }


        public JwtToken(String token, int passwordStatus) {
            this.token = token;
            this.passwordStatus = passwordStatus;
        }

        public JwtToken(int captchaAttempts, int lockoutTime, int attempts, int badPasswordAttempts) {
            this.captchaAttempts = captchaAttempts;
            this.lockoutTime = lockoutTime;
            this.attempts = attempts;
            this.badPasswordAttempts = badPasswordAttempts;
        }

        public JwtToken(int captchaAttempts, int attempts, int badPasswordAttempts) {
            this.captchaAttempts = captchaAttempts;
            this.attempts = attempts;
            this.badPasswordAttempts = badPasswordAttempts;
        }


        @JsonProperty("token")
        String getToken() {
            return token;
        }

        void setToken(String token) {
            this.token = token;
        }

        @JsonProperty("passwordStatus")
        int getPasswordStatus() {
            return passwordStatus;
        }

        void setPasswordStatus(int passwordStatus) {
            this.passwordStatus = passwordStatus;
        }

        @JsonProperty("lockoutTime")
        public int getLockoutTime() {
            return lockoutTime;
        }

        public void setLockoutTime(int lockoutTime) {
            this.lockoutTime = lockoutTime;
        }

        @JsonProperty("attempts")
        public int getAttempts() {
            return attempts;
        }

        public void setAttempts(int attempts) {
            this.attempts = attempts;
        }

        @JsonProperty("captchaAttempts")
        public int getCaptchaAttempts() {
            return captchaAttempts;
        }

        public void setCaptchaAttempts(int captchaAttempts) {
            this.captchaAttempts = captchaAttempts;
        }

        @JsonProperty("badPasswordAttempts")
        public int getBadPasswordAttempts() {
            return badPasswordAttempts;
        }

        public void setBadPasswordAttempts(int badPasswordAttempts) {
            this.badPasswordAttempts = badPasswordAttempts;
        }
    }
}




