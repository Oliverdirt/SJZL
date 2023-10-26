package com.ctsi.ssdc.thirdauth.workwx.web;

import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.service.CscpUserDetailService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.security.ThirdAuthToken;
import com.ctsi.ssdc.security.jwt.JWTHxConfigurer;
import com.ctsi.ssdc.security.jwt.TokenHxProvider;
import com.ctsi.ssdc.thirdauth.workwx.domain.WorkWxDepartment;
import com.ctsi.ssdc.thirdauth.workwx.domain.WorkWxLogin;
import com.ctsi.ssdc.thirdauth.workwx.domain.WorkWxUserInfo;
import com.ctsi.ssdc.thirdauth.workwx.service.WorkWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/api/system")
public class WorkWxLoginController {


    @Autowired
    private WorkWxService workWxService;

    @Autowired
    private CscpUserDetailService cscpUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenHxProvider tokenHxProvider;

    @Value("${workwx.route}")
    private String routePath;

    @Value("${ctsi.web-domain-name}")
    private String webDomainName;

    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getQrCode")
    @GetMapping("/getQrCode")
    @ResponseBody
    public AjaxResult getQrCode() throws UnsupportedEncodingException {
        WorkWxLogin weiXinLoginDTO = workWxService.loginQrCode();
        AjaxResult res = new AjaxResult(200,"ok",weiXinLoginDTO);
        return res;
    }

    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "workWxLoginCallBack")
    @ResponseBody
    @GetMapping("/workWxLogin/callback")
    public AjaxResult workWxLoginCallBack(HttpServletResponse response, @RequestParam("code") String code, @RequestParam("state") String state)throws IOException {
        CscpUserDetailDTO user = workWxService.oauth2Login(code);
        AjaxResult result = null;
        if (user == null){
            result = new AjaxResult(401,"The current user does not have permission, please contact the administrator!");
//            response.sendRedirect(state  + "/#errPage?msg=" + URLEncoder.encode("您暂无权限登录此应用，请联系管理员&code=401", "UTF-8"));
            if(webDomainName.equals(state)){
                state = state +  "/errPage?msg=" + URLEncoder.encode("您暂无权限登录此应用，请联系管理员&code=401", "UTF-8");
                response.sendRedirect(state);
            }
            return result;
        }
        String principal =  user.getTenantId()+","+user.getTenantAccount()+","+user.getUsername();
        ThirdAuthToken authenticationToken = new ThirdAuthToken(principal,null);
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            result =  new AjaxResult(500,"Third party authentication failed, please try another way");
//            response.sendRedirect(state  + "/#errPage?msg=" + URLEncoder.encode("您暂无权限登录此应用，请联系管理员&code=500", "UTF-8"));
            if(webDomainName.equals(state)){
                state = state +  "/errPage?msg=" + URLEncoder.encode("您暂无权限登录此应用，请联系管理员&code=401", "UTF-8");
                response.sendRedirect(state);
            }
            return result;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt;
        jwt = tokenHxProvider.createToken(authentication, false);
        String token = JWTHxConfigurer.AUTHORIZATION_BEARER + jwt;
        // 请求第三方登录接口
        state = state + routePath+ "?thirdAuthLoginToken=" + URLEncoder.encode(token, "UTF-8");
//        response.sendRedirect(state);
        if(webDomainName.equals(state)){
            response.sendRedirect(state);
        }
        result  = new AjaxResult(304,"Successful");
        return  result;
    }


    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "syncWorkWxUser")
    @ResponseBody
    @GetMapping("/syncWorkWxUser")
    public AjaxResult syncWorkWxUser(){
        // 获取部门信息，建立部门树
        List<WorkWxDepartment> departmentList = workWxService.getDepartment(null);
        workWxService.syncDept(departmentList);
        // 读取每个部门的人员信息，同步到数据库
        List<WorkWxUserInfo> user = workWxService.getAllUser(departmentList);
        workWxService.syncUser(user);
        return new AjaxResult(200,"Successfully synchronized enterprise wechat address book!");
    }


}
