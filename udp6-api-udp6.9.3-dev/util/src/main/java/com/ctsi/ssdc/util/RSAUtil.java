package com.ctsi.ssdc.util;

import org.apache.axis.encoding.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/** *//**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 *
 * @author fuxiang
 * @date 2019.4.30
 * @version 1.0
 */
public class RSAUtil {

	/** *//**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/** *//**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	/** *//**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/** *//**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/** *//**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/** *//**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	//默认公钥,BASE64编码
	public static final String PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQK"
			+ "BgQDP2nBLFOX+0TMh9G1+2wz4hJUPFwmBAIVWq031VzvGp5skYL0e4/1LNm/sg"
			+ "Q0wjME8uwulq0ylDt0Kt1S4V54MkPmkdHHD5AlHLzmlr0bN4kEFkaU9l/slMDN"
			+ "Mwxx0KT/1m6ETlg+fX3iaC66wIRijK5FSmvC10fBuuRutqisBEwIDAQAB";

	//默认私钥,BASE64编码
	public static final String PRI_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAM/acEsU5f7RMyH0" +
			"bX7bDPiElQ8XCYEAhVarTfVXO8anmyRgvR7j/Us2b+yBDTCMwTy7C6WrTKUO3Qq3" +
			"VLhXngyQ+aR0ccPkCUcvOaWvRs3iQQWRpT2X+yUwM0zDHHQpP/WboROWD59feJoL" +
			"rrAhGKMrkVKa8LXR8G65G62qKwETAgMBAAECgYEAhwqBmJZMU4DxeqcGmLuSssfY" +
			"3EP4XcLL3qJPdQ2m/YsZZcogNTik8T+9+edifV+FkIJpr8oacsa0X+j6dwYd3Rjo" +
			"Dov1enSSkjjYGDyjCZhLfgYw29wMHY1MPDXRA95HJGzUBA6nPDpo+rPhkjnwldqg" +
			"3lsZyAxxzefGr20VMWECQQD/if9Mf/7ElimjpNKxKq5eXfyDbsQv0296N3aUK6Lz" +
			"Tlzj/ejFWBACyipmaD6BHQAWYi2lFEcWcauJORTu7IBjAkEA0DprysVUiQOwkBUr" +
			"XqwwkfqytT+qtW6al7mPNpZI/LAixe6d8RweCY2n9UkyZV0Xr19kw4AyNfAIeEzm" +
			"WjBjkQJBAPvqryRbim2DG8mX052U1hFx1l479GyxpFR4GQ45dosl1a1twuvJxP77" +
			"DpGmcvgV/uASKa10MCCQLus4uspPnMECQQDE8Ux8RzESOhQn4m3XqCJ0H1ohBWuP" +
			"GGJM+KtUZMX5gRoSfQKTRhW+0WU3IJjyuB6rtxzVr41N6I4KQiloyfWRAkBq+YP9" +
			"T9jR/z2y7GWj0K/Jr44Pgc/Vl/NjFA2KJiO4fkb19c4nsNJ7BcHRkaFQaQCPWkZe" +
			"jWYEUwlI1dR47bGD";

	/**
	 * 公钥加密（不推荐使用，请使用encryptByPublicKey方法）
	 * @param str 明文
	 * @param publickey 公钥
	 * @return 密文
	 * @throws Exception
	 */
	

	public static byte[] encryptPub(byte[] str, byte[] publickey) throws Exception {

		return encryptByPublicKey(str,Base64.encode(publickey));

	}
	/**
	 * 私钥解密（不推荐使用，请使用decryptByPrivateKey方法）
	 * @param ciphertext 密文
	 * @param privatekey 私钥
	 * @return 解密内容
	 * @throws Exception
	 */
	

	public static byte[] decryptPri(byte[] ciphertext, byte[] privatekey) throws Exception {

		return decryptByPrivateKey(ciphertext, Base64.encode(privatekey));
	}


	/** *//**
	 * <p>
	 * 生成密钥对(公钥和私钥)
	 * </p>
	 *
	 * @return
	 * @throws GeneralSecurityException
	 */
	

	public static Map<String, RSAKey> genKeyPair() throws GeneralSecurityException {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, RSAKey> keyMap = new HashMap<String, RSAKey>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/** *//**
	 * <p>
	 * 用私钥对信息生成数字签名
	 * </p>
	 *
	 * @param data 已加密数据
	 * @param privateKey 私钥(BASE64编码)
	 *
	 * @return
	 * @throws GeneralSecurityException
	 */
	

	public static String sign(byte[] data, String privateKey) throws GeneralSecurityException {
		byte[] keyBytes = Base64Utils.decode(privateKey.getBytes());
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return new String(Base64Utils.encode(signature.sign()));
	}

	/** *//**
	 * <p>
	 * 校验数字签名
	 * </p>
	 *
	 * @param data 已加密数据
	 * @param publicKey 公钥(BASE64编码)
	 * @param sign 数字签名
	 *
	 * @return
	 * @throws GeneralSecurityException
	 *
	 */
	

	public static boolean verify(byte[] data, String publicKey, String sign)
			throws GeneralSecurityException{
		byte[] keyBytes = Base64Utils.decode(publicKey.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(Base64Utils.decode(sign.getBytes()));
	}

	/** *//**
	 * <P>
	 * 私钥解密（分段）
	 * </p>
	 *
	 * @param encryptedData 已加密数据
	 * @param privateKey 私钥(BASE64编码)
	 * @return
	 * @throws GeneralSecurityException,IOException
	 */
	

	@SuppressWarnings("Duplicates")
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
			throws GeneralSecurityException,IOException {
		byte[] keyBytes = Base64Utils.decode(privateKey.getBytes());
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/** *//**
	 * <p>
	 * 公钥解密（分段）
	 * </p>
	 *
	 * @param encryptedData 已加密数据
	 * @param publicKey 公钥(BASE64编码)
	 * @return
	 * @throws GeneralSecurityException,IOException
	 */
	

	@SuppressWarnings("Duplicates")
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws GeneralSecurityException,IOException {
		byte[] keyBytes = Base64Utils.decode(publicKey.getBytes());
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/** *//**
	 * <p>
	 * 公钥加密（分段）
	 * </p>
	 *
	 * @param data 源数据
	 * @param publicKey 公钥(BASE64编码)
	 * @return
	 * @throws GeneralSecurityException,IOException
	 */
	

	@SuppressWarnings("Duplicates")
	public static byte[] encryptByPublicKey(byte[] data, String publicKey)
			throws GeneralSecurityException,IOException {
		byte[] keyBytes = Base64Utils.decode(publicKey.getBytes());
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/** *//**
	 * <p>
	 * 私钥加密（分段）
	 * </p>
	 *
	 * @param data 源数据
	 * @param privateKey 私钥(BASE64编码)
	 * @return
	 * @throws GeneralSecurityException,IOException
	 */
	

	@SuppressWarnings("Duplicates")
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
			throws GeneralSecurityException,IOException {
		byte[] keyBytes = Base64Utils.decode(privateKey.getBytes());
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/** *//**
	 * <p>
	 * 获取私钥
	 * </p>
	 *
	 * @param keyMap 密钥对
	 * @return
	 */
	

	public static String getPrivateKey(Map<String, RSAKey> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return new String(Base64Utils.encode(key.getEncoded()));
	}

	/** *//**
	 * <p>
	 * 获取公钥
	 * </p>
	 *
	 * @param keyMap 密钥对
	 * @return
	 */
	

	public static String getPublicKey(Map<String, RSAKey> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return new String(Base64Utils.encode(key.getEncoded()));
	}


}