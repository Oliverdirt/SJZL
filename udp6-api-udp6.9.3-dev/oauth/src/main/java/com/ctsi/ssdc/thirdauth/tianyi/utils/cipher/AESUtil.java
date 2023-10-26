package com.ctsi.ssdc.thirdauth.tianyi.utils.cipher;

import com.ctsi.ssdc.thirdauth.tianyi.config.LoginboxConfig;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
* @author: hx
* @description: AES工具类
* @date: Created in 15:18 2022/05/10
*/
public class AESUtil {

	private static byte[] iv;

	static {
		try {
			iv = "0000000000000000".getBytes(LoginboxConfig.CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解密AES加密过的字符串
	 *
	 * @param content
	 *            AES加密过过的内容
	 * @param password
	 *            加密时的密码
	 * @return 明文
	 */
	public static String decrypt(String content, String password) {
		try {
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			SecretKeySpec secretKey = new SecretKeySpec(password.getBytes(LoginboxConfig.CHARSET), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			byte[] contentByte = ByteFormat.hexToBytes(content);
			byte[] result = cipher.doFinal(contentByte);
			return new String(result, StandardCharsets.UTF_8);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
