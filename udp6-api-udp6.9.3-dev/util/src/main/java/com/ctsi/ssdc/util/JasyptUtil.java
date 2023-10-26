package com.ctsi.ssdc.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class JasyptUtil {
    public static String passwordToEnc(String salt, String password, String prefix, String suffix) throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword(salt);
        standardPBEStringEncryptor.setConfig(config);
        String plainText = password;
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        return prefix + encryptedText + suffix;
    }

    public static String encToPassword(String salt, String encryptPassword) throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword(salt);
        standardPBEStringEncryptor.setConfig(config);
        String encryptedText = encryptPassword;
        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
        return plainText;
    }

    /**
     *   * Jasypt生成加密结果
     *   * @param password 配置文件中设定的加密盐值
     *   * @param value 加密值
     *   * @return
     *   
     */


    public static String encyptPwd(String password, String value) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(cryptor(password));
        String result = encryptor.encrypt(value);
        return result;
    }

    /**
     *   * 解密
     *   * @param password 配置文件中设定的加密盐值
     *   * @param value 解密密文
     *   * @return
     *   
     */


    public static String decyptPwd(String password, String value) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(cryptor(password));
        String result = encryptor.decrypt(value);
        return result;
    }

    public static SimpleStringPBEConfig cryptor(String password) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("2");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        return config;
    }


    public static void main(String[] args) throws Exception {

//        String biyi = passwordToEnc("biyi", "root", "ENC(", ")");
//        System.out.println(biyi);
//        String biyi1 = encToPassword("biyi", biyi);
//        System.out.println(biyi1);
        // 加密
        String encPwd = encyptPwd("biyi", "123456");
// 解密
        String decPwd = decyptPwd("biyi", encPwd);
        System.out.println(encPwd);
        System.out.println(decPwd);
    }
}
