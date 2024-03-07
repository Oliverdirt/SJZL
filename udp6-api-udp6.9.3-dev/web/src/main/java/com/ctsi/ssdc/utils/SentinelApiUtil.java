package com.ctsi.ssdc.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ctsi.ssdc.utils.ConstantValue.*;
import static com.ctsi.ssdc.utils.ConstantValue.FIELD_DATA;

/**
 * @author Len
 * @describe
 * @Date 2022/8/15 9:48
 */
public class SentinelApiUtil {

    private SentinelApiUtil() {

    }

    private static Logger log = LoggerFactory.getLogger(SentinelApiUtil.class);

    /**
     * @Describe 根据登录用户获取cookie
     * @author Len
     * @date 2022/8/10 8:52
     */
    public static String getCookie(String username, String password, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("password", password);
        params.put("username", username);
        try {
            return HttpUtil.post1(HTTP_SUFFIX + url + "/auth/login", params, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param app
     * @param cookie
     * @param url
     * @return {@link }
     * @Describe 获取机器列表
     * @author Len
     * @date 2022/8/15 10:01
     */
    public static JSONArray getHosts(String app, String cookie, String url) {
        Map<String, String> heardMap = new HashMap<>();
        JSONArray data = new JSONArray();
        heardMap.put(REQUEST_HEADER_COOKIE, cookie);
        try {
            String result = HttpUtil.sendGet(HTTP_SUFFIX + url + "/app/" + app + "/machines.json", null, heardMap, null, null);
            log.info("SentinelApiUtil.getHosts.result: {}", result);
            if (null != result && result.length() > 0) {
                JSONObject resultJson = JSON.parseObject(result);
                if (resultJson.getInteger("code").equals(NUM_ZERO)) {
                    data = resultJson.getJSONArray("data");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @param ip
     * @param port
     * @param searchKey
     * @param cookie
     * @param url
     * @return {@link }
     * @Describe 获取列表的簇点链路
     * @author Len
     * @date 2022/8/15 10:45
     */
    public static JSONArray getMachineResource(String ip, String port, String searchKey, String cookie, String url) {
        Map<String, String> heardMap = new HashMap<>();
        JSONArray data = new JSONArray();
        heardMap.put(REQUEST_HEADER_COOKIE, cookie);
        try {
            //拼接参数
            StringBuilder sb = new StringBuilder();
            sb.append("ip=").append(ip)
                    .append("&port=").append(port);
            if (null != searchKey) {
                sb.append("&searchKey=").append(searchKey);
            }
            String result = HttpUtil.sendGet(HTTP_SUFFIX + url + "/resource/machineResource.json", sb.toString(), heardMap, 1000, 1000);
            log.info("SentinelApiUtil.getHosts.getMachineResource: {}", result);
            JSONObject resultJson = JSON.parseObject(result);
            if (resultJson.getInteger("code") == 0) {
                data = resultJson.getJSONArray("data");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @param app       服务名
     * @param pageIndex 当前页
     * @param pageSize  页面大小
     * @return {@link }
     * @Describe 实时监控数据
     * @author Len
     * @date 2022/8/10 8:57
     */
    public static Map<String, Object> queryMetric(String app, Integer pageIndex, Integer pageSize, String searchKey, String cookie, String url) {
        Map<String, Object> data = new HashMap<>();
        Map<String, String> heardMap = new HashMap<>();
        heardMap.put(REQUEST_HEADER_COOKIE, cookie);
        try {
            //拼接参数
            StringBuilder sb = new StringBuilder();
            sb.append("app=").append(app);
            if (pageIndex != null) {
                sb.append("&pageIndex=").append(pageIndex);
            }
            if (pageSize != null) {
                sb.append("&pageSize=").append(pageSize);
            }
            if (searchKey != null) {
                sb.append("&searchKey=").append(searchKey);
            }
            String result = HttpUtil.sendGet(HTTP_SUFFIX + url + "/metric/queryTopResourceMetric.json", sb.toString(), heardMap, null, null);
            log.info("SentinelApiUtil.getHosts.queryMetric: {}", result);
            JSONObject resultJson = JSON.parseObject(result);
            if (0 == resultJson.getInteger(FIELD_CODE)) {
                JSONObject dataJson = resultJson.getJSONObject(FIELD_DATA);
                if (dataJson == null) {
                    return data;
                }
                //获取实时监控
                JSONObject metricJson = dataJson.getJSONObject("metric");
                for (Map.Entry<String, Object> stringObjectEntry : metricJson.entrySet()) {
                    String value = stringObjectEntry.getValue().toString();
                    data.put(stringObjectEntry.getKey(), JSON.parseArray(value));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}
