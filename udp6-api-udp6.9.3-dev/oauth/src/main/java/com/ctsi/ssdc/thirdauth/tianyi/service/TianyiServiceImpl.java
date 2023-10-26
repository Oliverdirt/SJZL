package com.ctsi.ssdc.thirdauth.tianyi.service;

import com.alibaba.fastjson.JSONObject;
import com.ctsi.ssdc.admin.domain.CscpUserDetail;
import com.ctsi.ssdc.admin.repository.CscpUserDetailRepository;
import com.ctsi.ssdc.thirdauth.tianyi.config.LoginboxConfig;
import com.ctsi.ssdc.thirdauth.tianyi.utils.*;
import com.ctsi.ssdc.thirdauth.tianyi.utils.cipher.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class TianyiServiceImpl implements Tianyiservice {

    @Value("${tianyi.appId}")
    private String appId;

    @Value("${tianyi.appSecret}")
    private String appSecret;


    @Value("${tianyi.pageKey}")
    private String pageKey;

    @Autowired
    private CscpUserDetailRepository cscpUserDetailRepository;

    @Override
    public  String getUnifyAccountLoginUrl(String state, String url) {
        JSONObject parasParam = new JSONObject();
        parasParam.put("timeStamp", System.currentTimeMillis()); // 时间戳，毫秒
        parasParam.put("returnURL", StringUtil.getUrlEncodeStr(url)); // 重定向返回URL（returnURL如带参数，整个returnURL需进行URL编码）
        if (!StringUtil.isEmpty(pageKey)) {
            parasParam.put("pageKey", pageKey); // 值为normal或popping（normal为正常嵌入页面，popping为弹出式页面,默认normal）
        }
        if (!StringUtil.isEmpty(url)) {
            parasParam.put("regReturnUrl", StringUtil.getUrlEncodeStr(LoginboxConfig.REG_RETURN_URL)); // 重定向返回URL（returnURL如带参数，整个returnURL需进行URL编码）
        }
        parasParam.put("state", state); // 用于保持请求和回调的状态，登录请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验

        String paras;
        try {
            paras = XXTea.encrypt(FormatUtil.json2UrlParam(parasParam.toString(), false, null), LoginboxConfig.CHARSET, StringUtil.toHex(appSecret,
                    LoginboxConfig.CHARSET));
        } catch (Exception e) {
            paras = "";
        }

        String sign;
        try {
            sign = HMACSHA1.getSignature(appId + LoginboxConfig.CLIENT_TYPE_WEB + LoginboxConfig.FORMAT + LoginboxConfig.VERSION_WEB + paras,
                    appSecret).toUpperCase(Locale.getDefault());
        } catch (Exception e) {
            sign = "";
        }

        JSONObject req = new JSONObject();
        req.put("appId", appId);
        req.put("clientType", LoginboxConfig.CLIENT_TYPE_WEB);
        req.put("format", LoginboxConfig.FORMAT);
        req.put("version", LoginboxConfig.VERSION_WEB);
        req.put("paras", paras);
        req.put("sign", sign);

        return LoginboxConfig.UNIFY_ACCOUNT_LOGIN_URL + "?" + FormatUtil.json2UrlParam(req.toString(), false, null);
    }


    @Override
    public String getUserInfo(String accessToken) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String format = "json";
        Map<String, String> generalParamsMap = new HashMap<>(0);
        Map<String, String> businessParamsMap = new HashMap<>(0);
        businessParamsMap.put("accessToken", accessToken);

        String params = null;
        try {
            params = RequestUtil.generateParams(businessParamsMap, appSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        generalParamsMap.put("clientId", appId);
        generalParamsMap.put("timeStamp", timeStamp);
        generalParamsMap.put("format", format);
        generalParamsMap.put("params", params);
        String sign = null;
        try {
            sign = RequestUtil.generateSign(generalParamsMap, LoginboxConfig.PRI_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        generalParamsMap.put("sign", sign);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : generalParamsMap.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");        }
        System.out.println(LoginboxConfig.GET_USER_INFO_URL + "?" + sb.toString());

        String resp = HttpUtil.doHttpPost(LoginboxConfig.GET_USER_INFO_URL, generalParamsMap);
        System.out.println(resp);

        //取号结果需要RSA解密
        JSONObject respObj = JSONObject.parseObject(resp);
        if (0 == respObj.getInteger("result")) {
            String encData = respObj.getString("data");
            //使用RSA私钥解密encData
            String finalData = null;
            try {
                finalData = RSAUtils.decryptByPrivateKeyForLongStr(encData, LoginboxConfig.PRI_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(finalData);
            return finalData;
        }
        return null;

    }

    @Override
    public CscpUserDetail getUserInfoByMobile(String mobile,String email) {
        return cscpUserDetailRepository.getUserInfoByMobile(mobile,email);
    }
}
