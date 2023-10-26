package com.ctsi.ssdc.thirdauth.workwx.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctsi.ssdc.thirdauth.workwx.domain.WorkWxDepartment;
import com.ctsi.ssdc.thirdauth.workwx.domain.WorkWxUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class WeiXinUtil {

    private static String accessTokenUrl;

    @Value("${workwx.accessTokenUrl}")
    public void setAccessTokenUrl(final String accessTokenUrl) {
        setAccessTokenUrlStatic(accessTokenUrl);
    }

    private static void setAccessTokenUrlStatic(final String accessTokenUrl) {
        WeiXinUtil.accessTokenUrl = accessTokenUrl;
    }

    private static String getUserIdUrl;

    @Value("${workwx.getUserIDUrl}")
    public void setGetUserIdUrl(final String getUserIdUrl) {
        setGetUserIdUrlStatic(getUserIdUrl);
    }

    private static void setGetUserIdUrlStatic(final String getUserIdUrl) {
        WeiXinUtil.getUserIdUrl = getUserIdUrl;
    }

    private static String getUserUrl;

    @Value("${workwx.getUserUrl}")
    public void setGetUserUrl(final String getUserUrl) {
        setGetUserUrlStatic(getUserUrl);
    }

    private static void setGetUserUrlStatic(final String getUserUrl) {
        WeiXinUtil.getUserUrl = getUserUrl;
    }

    private static String getDepartmentUrl1;

    @Value("${workwx.getDepartmentUrl1}")
    public void setGetDepartmentUrl1(final String getDepartmentUrl1) {
        getDepartmentUrl1Static(getDepartmentUrl1);
    }

    private static void getDepartmentUrl1Static(final String getDepartmentUrl1) {
        WeiXinUtil.getDepartmentUrl1 = getDepartmentUrl1;
    }

    private static String getDepartmentUrl2;

    @Value("${workwx.getDepartmentUrl2}")
    public void setGetDepartmentUrl2(final String getDepartmentUrl2) {
        getDepartmentUrl2Static(getDepartmentUrl2);
    }

    private static void getDepartmentUrl2Static(final String getDepartmentUrl2) {
        WeiXinUtil.getDepartmentUrl2 = getDepartmentUrl2;
    }

    private static String getDepartmentUsersDetailUrl;

    @Value("${workwx.getDepartmentUsersDetailUrl}")
    public void setGetDepartmentUsersDetailUrl(final String getDepartmentUsersDetailUrl) {
        setGetDepartmentUsersDetailUrlStatic(getDepartmentUsersDetailUrl);
    }

    private static void setGetDepartmentUsersDetailUrlStatic(final String getDepartmentUsersDetailUrl) {
        WeiXinUtil.getDepartmentUsersDetailUrl = getDepartmentUsersDetailUrl;
    }


    /**
     * 获取用户ID
     *
     * @param accessToken
     * @param code
     * @return
     */
    public static String getUserIdByToken(String accessToken, String code) {
        //1.获取请求的url
        String url = getUserIdUrl.replace("ACCESS_TOKEN", accessToken)
                .replace("CODE", code);
        //2.调用接口，发送请求，获取成员
        JSONObject jsonObject = SendRequest.sendGet(url);

        //3.错误消息处理
        if (null == jsonObject) {
            log.error("获取成员失败,接口返回为空");
            return null;
        }
        if (0 != jsonObject.getIntValue("errcode")) {
            log.error("获取成员失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
        } else {
            log.info("用户ID：" + jsonObject.getString("UserId"));
            if (!StringUtils.isEmpty(jsonObject.getString("UserId"))) {
                return jsonObject.getString("UserId");
            }
        }
        return null;
    }

    /**
     * 获取token
     *
     * @param appid
     * @param appsecret
     * @return
     */
    public static String getFirstAccessToken(String appid, String appsecret) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String saveDate = sdf.format(new Date());
        log.info("获取微信TOKN请求开始，当前时间：" + saveDate);
        String requestUrl = accessTokenUrl.replace("{corpId}", appid)
                .replace("{corpsecret}", appsecret);
        JSONObject jsonObject = SendRequest.sendGet(requestUrl);
        // 如果请求成功
        if (null == jsonObject) {
            log.error("获取成员失败,接口返回为空");
            return null;
        }

        if (jsonObject.getIntValue("errcode") == 0) {
            try {
                log.info("获取的token: " + jsonObject.getString("access_token"));
                log.info("时间" + saveDate);
                return jsonObject.getString("access_token");
            } catch (Exception e) {
                // 获取token失败
                log.error("获取token失败 errcode:{} errmsg:{}",
                        jsonObject.getString("errmsg"));
            }
        } else {
            log.error("获取token失败 errcode:{} errmsg:{}",
                    jsonObject.getString("errmsg"));
        }
        return null;
    }

    /**
     * 获取用户
     *
     * @param accessToken
     * @param userId
     * @return
     */
    public static WorkWxUserInfo getUserInfoById(String accessToken, String userId) {
        //1.获取请求的url
        String url = getUserUrl.replace("ACCESS_TOKEN", accessToken)
                .replace("USERID", userId);
        //2.调用接口，发送请求，获取成员
        JSONObject jsonObject = SendRequest.sendGet(url);

        //3.错误消息处理
        if (null == jsonObject) {
            log.error("获取成员失败,接口返回为空");
            return null;
        }

        if (0 != jsonObject.getIntValue("errcode")) {
            log.error("获取成员失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
        } else {
            log.info("用户ID：" + jsonObject.getString("userid"));
            log.info("用户名称：" + jsonObject.getString("name"));
            log.info("用户手机号：" + jsonObject.getString("mobile"));
            log.info("用户邮箱：" + jsonObject.getString("email"));
            log.info("用户邮箱1：" + jsonObject.getString("biz_mail"));
            WorkWxUserInfo weiXinUserInfoDTO = new WorkWxUserInfo();
            weiXinUserInfoDTO.setUserid(jsonObject.getString("userid"));
            weiXinUserInfoDTO.setName(jsonObject.getString("name"));
            weiXinUserInfoDTO.setMobile(jsonObject.getString("mobile"));
            weiXinUserInfoDTO.setEmail(jsonObject.getString("email"));
            // 头像
            weiXinUserInfoDTO.setAvatar(jsonObject.getString("avatar"));
            // 成员启用状态。1表示启用的成员，0表示被禁用。
            weiXinUserInfoDTO.setEnable(jsonObject.getString("enable"));
            return weiXinUserInfoDTO;
        }
        return null;
    }

    /**
     * 获取部门列表
     */
    public static List<WorkWxDepartment> getDepartmentList(String token, Long id) {
        //1、获取url
        String url = "";
        if (null == id) {
            url = getDepartmentUrl2.replace("ACCESS_TOKEN", token);
        } else {
            url = getDepartmentUrl1.replace("ACCESS_TOKEN", token).replace("ID", id + "");
        }
        JSONObject jsonObject = SendRequest.sendGet(url);

        if (null == jsonObject) {
            log.error("获取成员失败,接口返回为空");
            return null;
        }

        if (0 != jsonObject.getIntValue("errcode")) {
            log.error("获取部门列表失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
        } else {
            JSONArray jsonArray = jsonObject.getJSONArray("department");
            if (null != jsonArray) {
                List<WorkWxDepartment> departmentDTOList = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jb = jsonArray.getJSONObject(i);
                    WorkWxDepartment wxdto = new WorkWxDepartment();
                    wxdto.setId(jb.getLongValue("id"));
                    wxdto.setName(jb.getString("name"));
                    wxdto.setOrder(jb.getInteger("order"));
                    wxdto.setParentid(jb.getLongValue("parentid"));
                    departmentDTOList.add(wxdto);
                }
                return departmentDTOList;
            }
        }
        return null;
    }

    /**
     * 获取部门成员详情列表
     *
     * @param token
     * @param departmentId
     * @param fetch        1/0 是否递归获取子部门下面的成员：1-递归获取，0-只获取本部门
     * @return
     */
    public static List<WorkWxUserInfo> getDepartmentUsersDetail(String token, Long departmentId, Integer fetch) {
        //1、获取url
        String url = getDepartmentUsersDetailUrl.replace("ACCESS_TOKEN", token).replace("DEPARTMENT_ID", departmentId + "").replace("FETCH_CHILD", fetch + "");
        //2.调用接口，发送请求，获取成员
        JSONObject jsonObject = SendRequest.sendGet(url);
        //3.错误消息处理
        if (null == jsonObject) {
            log.error("获取成员失败,接口返回为空");
            return null;
        }
        if (0 != jsonObject.getIntValue("errcode")) {
            log.error("获取成员失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
        } else {
            JSONArray jsonArray = jsonObject.getJSONArray("userlist");
            if (null != jsonArray) {
                List<WorkWxUserInfo> weiXinUserInfoDTOList = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    WorkWxUserInfo weiXinUserInfoDTO = new WorkWxUserInfo();
                    weiXinUserInfoDTO.setUserid(jo.getString("userid"));
                    weiXinUserInfoDTO.setName(jo.getString("name"));
                    weiXinUserInfoDTO.setMobile(jo.getString("mobile"));
                    weiXinUserInfoDTO.setEmail(jo.getString("email"));
                    // 头像
                    weiXinUserInfoDTO.setAvatar(jo.getString("avatar"));
                    // 成员启用状态。1表示启用的成员，0表示被禁用。注意，服务商调用接口不会返回此字段
                    weiXinUserInfoDTO.setEnable(jo.getString("enables"));
                    weiXinUserInfoDTO.setAddress(jo.getString("address"));
                    JSONArray departArray = jo.getJSONArray("department");
                    List<Long> departmentIds = new ArrayList<>();
                    for (int b = 0; b < departArray.size(); b++) {
                        departmentIds.add(Long.parseLong(departArray.getString(b)));
                    }
                    weiXinUserInfoDTO.setDepartment(departmentIds);
                    weiXinUserInfoDTOList.add(weiXinUserInfoDTO);
                }
                return weiXinUserInfoDTOList;
            }
        }
        return null;
    }

}
