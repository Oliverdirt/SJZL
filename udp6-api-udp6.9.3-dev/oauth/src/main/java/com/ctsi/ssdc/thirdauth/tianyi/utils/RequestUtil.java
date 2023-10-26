package com.ctsi.ssdc.thirdauth.tianyi.utils;


import com.ctsi.ssdc.thirdauth.tianyi.utils.cipher.ByteFormat;
import com.ctsi.ssdc.thirdauth.tianyi.utils.cipher.RSAUtil;
import com.ctsi.ssdc.thirdauth.tianyi.utils.cipher.XXTeaUtil;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.util.*;
import java.util.Map.Entry;

/**
* @author: hx
* @description: 请求工具类
* @date: Created in 18:53 2022/5/8
*/
public class RequestUtil {

    /**
     * 生成签名串
     * @param requestData 除sign外的通用参数
     * @param privateKey rsa私钥
     * @return
     */
    public static String generateSign(Map<String, String> requestData, String privateKey) throws Exception {
        RSAPrivateKey rsaPrivateKey = RSAUtil.loadPrivateKey(privateKey);
        return generateSign(requestData, rsaPrivateKey);
    }

    /**
     * 生成签名串 所有参数按key的升序排序
     * @param requestData 除sign外的通用参数
     * @param rsaPrivateKey RSA私钥
     * @return
     * @throws Exception
     */
    public static String generateSign(Map<String, String> requestData, RSAPrivateKey rsaPrivateKey) throws Exception {
        /*List<Entry<String, String>> requestDataList = new ArrayList<>(requestData.entrySet());
        Collections.sort(requestDataList, Comparator.comparing(Entry::getKey));
        String encryptValue = "";
        for(Entry<String, String> entry : requestDataList){
            encryptValue = encryptValue + entry.getValue();
        }*/
        return RSAUtil.sign(generatePlainText(requestData), rsaPrivateKey);
    }

    /**
     * 参数排序
     * @param returnData
     * @return
     */
    private static String generatePlainText(Map<String,String> returnData){
        //排序参数
        List<Entry<String,String>> mappingList = null;
        mappingList = new ArrayList<Entry<String,String>>(returnData.entrySet());
        Collections.sort(mappingList,new Comparator<Entry<String,String>>(){
            @Override
            public int compare(
                Entry<String,String> mapping1,
                Entry<String,String> mapping2){
            return mapping1.getKey().compareTo(mapping2.getKey());
        }
        });

        StringBuffer plainText = new StringBuffer();
        for(Entry<String,String> mapping:mappingList){
            plainText.append(mapping.getValue());
        }
        return plainText.toString();
    }


    /**
     * 生成params参数
     * @param requestData 业务参数
     * @param appSecret 应用秘钥
     * @return
     */
    public static String generateParams(Map<String, String> requestData, String appSecret) throws Exception {
        StringBuffer sb = new StringBuffer();
//        requestData.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        for(Entry<String, String> entry : requestData.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        byte[] encValue = XXTeaUtil.encrypt(sb.toString().getBytes("UTF-8"), appSecret.getBytes("UTF-8"));
        return ByteFormat.bytesToHexString(encValue);
    }

    public static void main(String[] args) throws Exception {
        String params =
                "9FBCFC97786BBF8404E12E4BDFAA86A5EA250BF22DACBDC44E385324210249220954E63F8DEB39E561D63826F95CA940EB7DA451";
        byte[] result = XXTeaUtil.decrypt(ByteFormat.hexToBytes(params),
                "C16bkftbCzGuiUwNj10Q0NbZH4NKa7Vz".getBytes("UTF-8"));
        System.out.println(new String(result, StandardCharsets.UTF_8));
    }
}
