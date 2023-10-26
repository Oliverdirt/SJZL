package com.ctsi.ssdc.thirdauth.tianyi.web;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.admin.domain.CscpTenant;
import com.ctsi.ssdc.admin.domain.CscpUserDetail;
import com.ctsi.ssdc.admin.service.CscpTenantService;
import com.ctsi.ssdc.admin.service.UserService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.security.ThirdAuthToken;
import com.ctsi.ssdc.security.jwt.JWTHxConfigurer;
import com.ctsi.ssdc.security.jwt.TokenHxProvider;
import com.ctsi.ssdc.thirdauth.tianyi.config.LoginboxConfig;
import com.ctsi.ssdc.thirdauth.tianyi.service.Tianyiservice;
import com.ctsi.ssdc.thirdauth.tianyi.utils.*;
import com.ctsi.ssdc.utils.HxStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/api/system/thirdAuth/tianyi")
public class TianyiLoginController {

    @Autowired
    private Tianyiservice tianyiservice;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenHxProvider tokenHxProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private CscpTenantService cscpTenantService;

    @Value("${tianyi.appId}")
    private String appId;

    @Value("${tianyi.appSecret}")
    private String appSecret;

    @Value("${tianyi.returnUrl}")
    private String returnUrl;

    @Value("${tianyi.route}")
    private String route;

    @Value("${ctsi.web-domain-name}")
    private String webDomainName;



    /**
     * 获取Web登录框链接
     *
     * @return Web登录框链接
     */
    @GetMapping("/getUnifyAccountLoginUrl")
    @ResponseBody
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getUnifyAccountLoginUrl")
    public String getUnifyAccountLoginUrl(@RequestParam("state") String state) throws UnknownHostException {
        return tianyiservice.getUnifyAccountLoginUrl(state,returnUrl);
    }


    /**
     * 注销登录接口
     * @return
     */
    @GetMapping("/logout")
    @ResponseBody
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "loginOut")
    public String loginOut(@RequestParam("url") String url){
        com.alibaba.fastjson.JSONObject paramsJson = new com.alibaba.fastjson.JSONObject();
        paramsJson.put("appId",appId);
        paramsJson.put("clientType", LoginboxConfig.CLIENT_TYPE_WEB);
        paramsJson.put("format", LoginboxConfig.FORMAT);
        paramsJson.put("version",LoginboxConfig.VERSION_WEB);
        paramsJson.put("timeStamp",String.valueOf(System.currentTimeMillis()));
        paramsJson.put("returnURL",url+route);

        String paras;
        try {
            paras = XXTea.encrypt(FormatUtil.json2UrlParam(paramsJson.toString(), false, null),
                    LoginboxConfig.CHARSET, StringUtil.toHex(appSecret,
                    LoginboxConfig.CHARSET));
        } catch (Exception e) {
            paras = "";
        }
        String sign;
        try {
            sign = HMACSHA1.getSignature(appId + LoginboxConfig.CLIENT_TYPE_WEB + LoginboxConfig.FORMAT +
                            LoginboxConfig.VERSION_WEB + paras,
                    appSecret).toUpperCase(Locale.getDefault());
        } catch (Exception e) {
            sign = "";
        }
        com.alibaba.fastjson.JSONObject req = new com.alibaba.fastjson.JSONObject();
        req.put("appId", appId);
        req.put("clientType", LoginboxConfig.CLIENT_TYPE_WEB);
        req.put("format", LoginboxConfig.FORMAT);
        req.put("version", LoginboxConfig.VERSION_WEB);
        req.put("paras", paras);
        req.put("sign", sign);

        String logoutUrl = LoginboxConfig.LOGOUT_URL + "?" + FormatUtil.json2UrlParam(req.toString(), false, null);
        return logoutUrl;
    }

    /**
     * 通过code获取accessToken
     * @return
     */
    @GetMapping("/accessToken")
    @ResponseBody
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "accessToken")
    public void accessToken(String appId, String paras, HttpServletRequest request, HttpServletResponse response){
        try {
            String  returnUrl = XXTea.decrypt(paras, LoginboxConfig.CHARSET, StringUtil.toHex(appSecret,
                    LoginboxConfig.CHARSET));
            Map<String, String> stringMap = UrlParam.urlSplit(returnUrl);
            String state = stringMap.get("state");
            String code = stringMap.get("code");
            String encryValue = appId + stringMap.get("code") +"authorization_login" ;
            String sign = HMACSHA1.getSignature(encryValue, appSecret).toUpperCase(Locale.getDefault());

            //获取token
            JSONObject jo = new JSONObject();
            jo.append("appId",appId).append("grantType","authorization_login").append("code",code).append("sign",sign);
            String post = HttpUtil.post(LoginboxConfig.GET_ACCESS_TOKEN, jo);
            //获取用户信息
            JSONObject jsonObject = JSONUtil.parseObj(post);
            String accessToken = String.valueOf(jsonObject.get("accessToken"));
            String userInfo = "";
            if(HxStringUtils.isNull(accessToken)){
//                response.sendRedirect(state + route + "/errPage?msg=" + URLEncoder.encode("获取token失败，天翼账号登录异常&code=500&type=ty", "UTF-8"));
                if(webDomainName.equals(state)){
                    state = state + route + "/errPage?msg=" + URLEncoder.encode("获取token失败，天翼账号登录异常&code=500&type=ty", "UTF-8");
                    response.sendRedirect(state);
                    return ;
                }

            }
            userInfo = tianyiservice.getUserInfo(accessToken);
            if(HxStringUtils.isBlank(userInfo)){
                if(webDomainName.equals(state)){
                    state = state + route + "/errPage?msg=" + URLEncoder.encode("获取用户信息失败，天翼账号登录异常&code=500&type=ty", "UTF-8");
                    response.sendRedirect(state);
                    return ;
                }
//                response.sendRedirect(state + route + "/errPage?msg=" + URLEncoder.encode("获取用户信息失败，天翼账号登录异常&code=500&type=ty", "UTF-8"));
//                return ;
            }
            JSONObject jsonObject1 = JSONUtil.parseObj(userInfo);
            String mobile = String.valueOf(jsonObject1.get("mobile"));
            CscpUserDetail userInfoByMobile = null;
            if(mobile.indexOf("@") != -1){
                userInfoByMobile = tianyiservice.getUserInfoByMobile(null,mobile);
            }else{
                userInfoByMobile = tianyiservice.getUserInfoByMobile(mobile,null);
            }
            if(HxStringUtils.isNull(userInfoByMobile)){
//                response.sendRedirect(state + route + "/errPage?msg=" + URLEncoder.encode("您暂无权限登录此应用，请联系管理员&code=401&type=ty", "UTF-8"));
//                return ;
                if(webDomainName.equals(state)){
                    state = state + route + "/errPage?msg=" + URLEncoder.encode("您暂无权限登录此应用，请联系管理员&code=401&type=ty", "UTF-8");
                    response.sendRedirect(state);
                    return ;
                }
            }
            String username = userInfoByMobile.getUsername();
            Long tenantId = userInfoByMobile.getTenantId();
            CscpTenant cscpTenant = cscpTenantService.findOne(tenantId);
            String principal = cscpTenant.getId() +","+cscpTenant.getTenantAccount()+","+username;
            boolean rememberMe = false;
            ThirdAuthToken authenticationToken = new ThirdAuthToken(principal, null);

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
            response.setHeader("token",token);
//            response.sendRedirect(state + route + "/thirdAuthLogin?thirdAuthLoginToken=" + URLEncoder.encode(token, "UTF-8"));
            if(webDomainName.equals(state)){
                response.sendRedirect(state + route + "/thirdAuthLogin?thirdAuthLoginToken=" + URLEncoder.encode(token, "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
