package com.ctsi.ssdc.thirdauth.tianyi.utils.cipher;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

/** */

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * <p>
 * BASE64编码解码工具包
 * </p>
 * <p>
 * 依赖javabase64-1.3.1.jar
 * </p>
 *
 * @author IceWee
 * @date 2012-5-19
 * @version 1.0
 */
public class Base64Utils {

/** *//**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

/**
     * <p>
     * BASE64字符串解码为二进制数据
     * </p>
     *
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] buffer = decoder.decode(base64);
        return buffer;
    }

/**
     * <p>
     * 二进制数据编码为BASE64字符串
     * </p>
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encode = encoder.encode(bytes);
        return new String(encode, StandardCharsets.UTF_8);
    }


}