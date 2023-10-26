package com.ctsi.ssdc.thirdauth.tianyi.utils;

import com.ctsi.ssdc.thirdauth.tianyi.config.LoginboxConfig;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACSHA1 {
    private static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * 生成签名数据
     *
     * @param data 待加密的数据
     * @param key  加密使用的key
     * @throws Exception
     */
    public static String getSignature(String data, String key) throws Exception {
        byte[] keyBytes = key.getBytes(LoginboxConfig.CHARSET);
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes(LoginboxConfig.CHARSET));
        StringBuilder sb = new StringBuilder();
        for (byte b : rawHmac) {
            sb.append(byteToHexString(b));
        }
        return sb.toString();
    }

    private static String byteToHexString(byte ib) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] ob = new char[2];
        ob[0] = digit[(ib >>> 4) & 0X0f];
        ob[1] = digit[ib & 0X0F];
        return new String(ob);
    }
}