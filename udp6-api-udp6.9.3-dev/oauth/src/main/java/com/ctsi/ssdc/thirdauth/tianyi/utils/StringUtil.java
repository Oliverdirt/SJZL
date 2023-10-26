package com.ctsi.ssdc.thirdauth.tianyi.utils;


import com.ctsi.ssdc.thirdauth.tianyi.config.LoginboxConfig;

import java.net.URLEncoder;

public class StringUtil {
    private static final char[] DIGIT = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getUrlEncodeStr(String str) {
        try {
            return URLEncoder.encode(str, LoginboxConfig.CHARSET);
        } catch (Exception e) {
            return str;
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String toHex(String data, String encode) {
        try {
            return toHex(data.getBytes(encode));
        } catch (Exception e) {
            return "";
        }
    }

    private static String toHex(byte[] byteData) {
        int len = byteData.length;
        char[] buf = new char[len * 2];
        int k = 0;
        for (byte b : byteData) {
            buf[k++] = DIGIT[(b & 255) >> 4];
            buf[k++] = DIGIT[b & 15];
        }
        return new String(buf);
    }
}