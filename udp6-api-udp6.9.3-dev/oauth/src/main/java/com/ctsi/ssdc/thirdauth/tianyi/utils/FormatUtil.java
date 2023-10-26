package com.ctsi.ssdc.thirdauth.tianyi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FormatUtil {
    /**
     * JSON->URL参数
     *
     * @param json          JSON
     * @param isUrlEncoding 是否进行URL编码
     * @param charset       字符编码集（可空，默认：UTF-8）
     * @return URL参数
     */
    public static String json2UrlParam(String json, boolean isUrlEncoding, String charset) {
        if (StringUtil.isEmpty(json)) {
            return "";
        }

        StringBuilder urlParamStrBuilder = new StringBuilder();
        JSONObject jsonObj = JSON.parseObject(json);
        dealWithJsonEmptyValue(jsonObj);
        for (String key : jsonObj.keySet()) {
            String value = jsonObj.getString(key);
            value = StringUtil.isEmpty(value) ? "" : value;
            if (isUrlEncoding && !StringUtil.isEmpty(value)) {
                value = StringUtil.getUrlEncodeStr(value);
            }
            urlParamStrBuilder.append("&");
            urlParamStrBuilder.append(key);
            urlParamStrBuilder.append("=");
            urlParamStrBuilder.append(value);
        }
        String urlParam = urlParamStrBuilder.toString();
        return urlParam.startsWith("&") ? urlParam.substring(1, urlParam.length()) : urlParam;
    }

    /**
     * 处理JSON空值
     *
     * @param jsonObj JSON对象
     */
    private static void dealWithJsonEmptyValue(JSONObject jsonObj) {
        if (jsonObj == null) {
            return;
        }

        for (String key : jsonObj.keySet()) {
            if (StringUtil.isEmpty(jsonObj.getString(key))) {
                jsonObj.put(key, "");
            }
        }
    }
}