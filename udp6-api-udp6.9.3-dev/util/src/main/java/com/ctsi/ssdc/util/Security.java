package com.ctsi.ssdc.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * security加密工具
 * @author ctsi
 *
 */
public class Security {
	/**
	 * 加密
	 * 
	 * @param original
	 *            需要加密的字符串
	 * @return
	 * @return String
	 * @exception @createTime：2018年9月30日
	 */
	

	public static String encode(String original) {
		try {
			String algorithm = SecretKeyUtil.secretMode();
			byte[] keybyte = SecretKeyUtil.secretkey(algorithm);
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			// 在单一方面的加密或解密
			byte[] tmp = c1.doFinal(original.getBytes());
			return BaseCoderUtil.byteArr2HexStr(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param ciphertext
	 *            需要解密的字符串
	 * @return
	 * @return String
	 * @exception @createTime：2018年9月30日
	 */
	

	public static String decode(String ciphertext) {
		try {
			String algorithm = SecretKeyUtil.secretMode();
			byte[] keybyte = SecretKeyUtil.secretkey(algorithm);
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			// 传入的是一个16进制的字符串
			byte[] cpByte = BaseCoderUtil.hexStr2ByteArr(ciphertext);
			byte[] tmp = c1.doFinal(cpByte);
			return new String(tmp);
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

}
