package com.ctsi.ssdc.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * AES对称加密算法
 * <pre> 
 * 这里演示的是其Java6.0的实现,理所当然的BouncyCastle也支持AES对称加密算。
 * 另外,我们也可以以AES算法实现为参考,完成RC2,RC4和Blowfish算法的实现。
 * 由于DES的不安全性以及DESede算法的低效,于是催生了AES算法(Advanced Encryption Standard。
 * 该算法比DES要快,安全性高,密钥建立时间短,灵敏性好,内存需求低,在各个领域应用广泛。
 * 目前,AES算法通常用于移动通信系统以及一些软件的安全外壳,还有一些无线路由器中也是用AES算法构建加密协议。
 * 由于Java6.0支持大部分的算法,但受到出口限制,其密钥长度不能满足需求。
 * 所以特别需要注意的是:如果使用256位的密钥,则需要无政策限制文件(Unlimited Strength Jurisdiction Policy Files)，
 * 不过Sun是通过权限文件local_poblicy.jar和US_export_policy.jar做的相应限制,我们可以在Sun官网下载替换文件,
 * 减少相关限制.网址为http://www.Oracle.com/technetwork/java/javase/downloads/index.html。
 * 在该页面的最下方找到Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 6,
 * 点击下载
 * http://download.oracle.com/otn-pub/java/jce_policy/6/jce_policy-6.zip
 * http://download.oracle.com/otn-pub/java/jce/7/UnlimitedJCEPolicyJDK7.zip
 * 然后覆盖本地JDK目录和JRE目录下的security目录下的文件即。
 * 关于AES的更多详细介绍 ,可以参考此博客http://blog.csdn.net/kongqz/article/category/800296
 * </pre>
 * 
 */
public class AESUtil {
	/**
	 * ECB 填充方式
	 */
	public static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";
	/**
	 * CBC 填充方式
	 */
	public static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
	/**
	 * 默认密钥
	 */
	public static final String KEY = "1q2w3e4r";

	/**
	 * 使用默认密钥加密
	 * @param content 原文
	 * @return 密文
	 * @throws Exception 异常
	 */

	public static String encrypt(String content) throws Exception {
		return encrypt(KEY,content);
	}
	
	/**
	 * 加密
	 * @param strKey 密钥
	 * @param content 原文
	 * @return 密文
	 * @throws Exception 异常
	 */

	public static String encrypt(String strKey, String content) throws Exception {
		SecretKeySpec skeySpec = getKey(strKey);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
		IvParameterSpec iv = new IvParameterSpec("1234567812345678".getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(content.getBytes());

		return Base64.getEncoder().encodeToString(encrypted);
	}


	/**
	 * 使用默认密钥解密
	 * @param content 密文
	 * @return 原文
	 * @throws Exception 异常
	 */
	
	public static String decrypt(String content) throws Exception {
		return decrypt(KEY,content);
	}
	
	/**
	 * 解密
	 * @param strKey 密钥
	 * @param content 密文
	 * @return 原文
	 * @throws Exception 异常
	 */
	
	public static String decrypt(String strKey, String content) throws Exception {
		SecretKeySpec skeySpec = getKey(strKey);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
		IvParameterSpec iv = new IvParameterSpec("1234567812345678".getBytes());
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		byte[] encrypted1 = Base64.getDecoder().decode(content);
		
		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original,"utf-8");
		return originalString;
	}

	/**
	 * 密钥加密
	 * @param strKey 密钥
	 * @return 密文
	 * @throws Exception 异常
	 */
	private static SecretKeySpec getKey(String strKey) throws Exception {
		byte[] arrBTmp = strKey.getBytes();
		// 创建一个空的16位字节数组（默认值为0）
		byte[] arrB = new byte[16]; 
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");
		return skeySpec;
	}

}