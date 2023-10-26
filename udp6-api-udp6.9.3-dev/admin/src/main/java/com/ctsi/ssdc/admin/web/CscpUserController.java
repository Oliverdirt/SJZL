package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.admin.domain.CscpTenant;
import com.ctsi.ssdc.admin.domain.CscpUserPasswordChangeLog;
import com.ctsi.ssdc.admin.domain.dto.CscpUserCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpUserDTO;
import com.ctsi.ssdc.admin.domain.dto.CscpUserPasswordUpdate;
import com.ctsi.ssdc.admin.service.CscpTenantService;
import com.ctsi.ssdc.admin.service.CscpUserPasswordChangeLogService;
import com.ctsi.ssdc.admin.service.CscpUserService;
import com.ctsi.ssdc.admin.service.impl.CscpUserServiceImpl;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.AjaxBackData;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.security.jwt.JWTHxConfigurer;
import com.ctsi.ssdc.security.jwt.TokenHxProvider;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;


/**
 * REST controller for managing CscpUser.
 *
 * @author ctsi biyi generator
 *
 */
@RestController
@RequestMapping("/api/system")
public class CscpUserController {

    private final Logger log = LoggerFactory.getLogger(CscpUserController.class);

    private static final String ENTITY_NAME = "cscpUser";

    private final CscpUserService cscpUserService;

    @Value("${ctsi.login-password:#{null}}")
    private String defaultPassward;

    @Value("${ctsi.RSA-prikey:}")
    private String rsaPrikey = "";

    @Value("${ctsi.password-check.check-min-length}")
    private String minLength;

    @Value("${ctsi.password-check.check-max-length}")
    private String maxLength;

    @Value("${ctsi.password-check.check-lowercase}")
    private boolean lowercase;

    @Value("${ctsi.password-check.check-uppercase}")
    private boolean uppercase;

    @Value("${ctsi.password-check.check-digit}")
    private boolean digit;

    @Value("${ctsi.password-check.check-special-character}")
    private boolean specialChar;

    @Value("${ctsi.password-check.check-contains-username}")
    private boolean checkContainsUserName;

    @Value("${ctsi.password-check.check-contains-tenantAccount}")
    private boolean checkContainsTenantAccount;



    @Autowired
    CscpUserPasswordChangeLogService cscpUserPasswordChangeLogService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenHxProvider tokenHxProvider;

    @Autowired
    CscpTenantService cscpTenantService;

    public CscpUserController(CscpUserService cscpUserService) {
        this.cscpUserService = cscpUserService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
    
    /**
     * POST  /cscpUsers : Create a new cscpUser.
     *
     * @param cscpUserDTO the cscpUserDTO to create
     * @return the ResponseEntity with status 201 (Created) and
     * with body the new cscpUserDTO, or with status 400 (Bad Request) if the cscpUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cscpUsers")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpUser")
    public ResponseEntity<CscpUserDTO> createCscpUser(@RequestBody CscpUserDTO cscpUserDTO) throws URISyntaxException {
        log.debug("REST request to save CscpUser : {}", cscpUserDTO);
        if (cscpUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new cscpUser cannot already have an ID", ENTITY_NAME, "idexists");
        }

        //如果用户未设置密码，则使用默认密码
        if(StringUtils.isBlank(cscpUserDTO.getPassword()) && null!=defaultPassward){
            cscpUserDTO.setPassword(passwordEncoder.encode(defaultPassward));
        }

        CscpUserDTO result = cscpUserService.insert(cscpUserDTO);
        if(result.getId()!=null){
            CscpUserPasswordChangeLog cscpUserPasswordChangeLog = new CscpUserPasswordChangeLog();
            cscpUserPasswordChangeLog.setUserId(result.getId());
            cscpUserPasswordChangeLog.setPassword(passwordEncoder.encode(result.getPassword()));
            cscpUserPasswordChangeLog.setTime(ZonedDateTime.now());
            cscpUserPasswordChangeLogService.insert(cscpUserPasswordChangeLog);

        }

        return ResponseEntity.created(new URI("/api/cscpUsers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cscpUsers : Updates an existing cscpUser.
     *
     * @param cscpUserDTO the cscpUserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpUserDTO,
     * or with status 400 (Bad Request) if the cscpUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the cscpUserDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cscpUsers")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpUser")
    public ResponseEntity<CscpUserDTO> updateCscpUser(@RequestBody CscpUserDTO cscpUserDTO) throws URISyntaxException {
        log.debug("REST request to update CscpUser : {}", cscpUserDTO);
        if (cscpUserDTO.getId() == null) {
            return createCscpUser(cscpUserDTO);
        }
        CscpUserDTO result = cscpUserService.update(cscpUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cscpUserDTO.getId().toString()))
            .body(result);
    }
    
    /**
     * PUT  /cscpUserPassword : 修改密码
     *
     * @param cscpUserPasswordUpdate the cscpUserPasswordUpdate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpUserDTO,
     * or with status 400 (Bad Request) if the cscpUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the cscpUserDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cscpUserPassword")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpUserPassword")
    public ResponseEntity<JwtToken> updateCscpUserPassword(
            @RequestBody CscpUserPasswordUpdate cscpUserPasswordUpdate) throws URISyntaxException {
        log.debug("REST request to update cscpUserPassword : {}", cscpUserPasswordUpdate);
        CscpUserServiceImpl.UpdatePasswordResult updatePasswordResult = null;
        CscpTenant cscpTenant = cscpTenantService.selectByTenantAccount(cscpUserPasswordUpdate.getTenantAccount());
        if(cscpTenant == null){
            throw new BadRequestAlertException("租户不存在", null, null);
        }
        try {
            cscpUserPasswordUpdate.setTenantId(cscpTenant.getId());
            updatePasswordResult = cscpUserService.updatePassword(cscpUserPasswordUpdate,true);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity(new JwtToken(null,e.getMessage()), null, HttpStatus.OK);
        }
        String principal = cscpTenant.getId() +","+cscpTenant.getTenantAccount()+","+cscpUserPasswordUpdate.getUserName();
        // security
        String password = cscpUserService.decryptPassword(updatePasswordResult.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principal,
                password);
        Authentication authentication = null;
        try {
            authentication = this.authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw e;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenHxProvider.createToken(authentication, false);


        HttpHeaders httpHeaders = new HttpHeaders();
        String token = JWTHxConfigurer.AUTHORIZATION_BEARER + jwt;

        httpHeaders.add(JWTHxConfigurer.AUTHORIZATION_HEADER, token);
        return new ResponseEntity(new JwtToken(token), httpHeaders, HttpStatus.OK);

    }

    /**
     * GET  /cscpUsers : get the cscpUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpUsers in body
     */
    @GetMapping("/cscpUsers")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpUsers")
    public PageResult<CscpUserDTO> getCscpUsers(CscpUserDTO cscpUserDTO, Pageable page) {
        log.debug("REST request to get CscpUsers");
        return cscpUserService.findByCscpUserDTO(cscpUserDTO, page);
    }
    
    /**
     * GET  /cscpUsers : get the cscpUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpUsers in body
     */
    @GetMapping("/cscpUsersByCriteria")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpUsersByCriteria")
    public PageResult<CscpUserDTO> getCscpUsersByCriteria(CscpUserCriteria cscpUserCriteria, Pageable page) {
        log.debug("REST request to get CscpUsersByCriteria");
        return cscpUserService.findByCscpUserCriteria(cscpUserCriteria, page);
    }

    /**
     * GET  /cscpUsers/:id : get the "id" cscpUser.
     *
     * @param id the id of the cscpUserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpUserDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cscpUsers/{id}")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpUser")
    public ResponseEntity<CscpUserDTO> getCscpUser(@PathVariable Long id) {
        log.debug("REST request to get CscpUser : {}", id);
        CscpUserDTO cscpUserDTO = cscpUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpUserDTO));
    }

    /**
     * DELETE  /cscpUsers/:id : delete the "id" cscpUser.
     *
     * @param id the id of the cscpUserDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cscpUsers/{id}")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteCscpUser")
    public ResponseEntity<Void> deleteCscpUser(@PathVariable  Long id) {
        log.debug("REST request to delete CscpUser : {}", id);
        if(id == 1){
            throw new BadRequestAlertException("删除失败,管理员用户不能删除",ENTITY_NAME,"error");
        }
        cscpUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * 用户名是否存在
     */
    @GetMapping("/cscpUserExistByUsername")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "existByUsername")
    public ResponseEntity<AjaxBackData> existByUsername( String username) {
        log.debug("REST request to vilidate cscpUserUsername : {}", username);
       
        boolean exists = cscpUserService.existByUsername(username);
        AjaxBackData ajaxBackData = new AjaxBackData(exists,"");

        return ResponseEntity.ok()
            .body(ajaxBackData);
    }

    @GetMapping("/cscpUserPasswordRule")
//
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "passwordRule")
    public String passwordRule(){
        StringBuilder sb = new StringBuilder();
        sb.append("密码长度大于");
        sb.append(minLength);
        sb.append("小于");
        sb.append(maxLength);
        sb.append(";需要包括");
        if (lowercase){
            sb.append("小写字母、");
        }
        if(uppercase){
            sb.append("大写字母、");
        }
        if (digit){
            sb.append("数字、");
        }
        if (specialChar){
            sb.append("特殊字符");
        }
        sb.append(".");
        if(checkContainsTenantAccount){
            sb.append("不能包含租户账号");
        }
        sb.append("、");
        if(checkContainsUserName){
            sb.append("用户名");
        }

        return sb.toString();
    }

    @GetMapping("/deblockingAccount/{username}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deblockingAccount")
    public void deblockingAccount(@PathVariable("username") String username){
        cscpUserService.deblockingAccount(username);
    }


    /**
     * Object to return as body in JWT Authentication.
     */
    public static class JwtToken {

        private String token;


        private String msg;

        JwtToken(String token) {
            this.token = token;
        }

        public JwtToken(String token, String msg) {
            this.token = token;
            this.msg = msg;
        }

        @JsonProperty("token")
        String getToken() {
            return token;
        }

        void setToken(String token) {
            this.token = token;
        }


        @JsonProperty("msg")
        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
