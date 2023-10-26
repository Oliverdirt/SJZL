package com.ctsi.ssdc.util;


/**
 * 密钥工具
 * 
 * @author liminqiang
 *
 */
public class SecretKeyUtil {
	// 算法名称
	private static final String ALGORITHM_3DES = "DESede";
	// 密钥原文
	private static final String KEYSTR = "ctsi$#@!";
	// 密钥
	private final static byte[] DESC3KEY = new byte[24];

	static {
		byte[] tmp = createKey();
		for (int i = 0; i < 24; i++) {
			DESC3KEY[i] = tmp[23 - i];
		}
	}

	private static byte[] createKey() {
		byte[] first = KEYSTR.getBytes();
		byte[] sec = BaseCoderUtil.byteArr2HexStr(first).getBytes();
		byte[] key = new byte[24];
		for (int i = 0; i < 8; i++) {
			key[i] = first[i];
		}
		for (int i = 0; i < 16; i++) {
			key[i + 8] = sec[i];
		}
		return key;
	}

	/**
	 * 获取算法名称
	 * 
	 * @return
	 */
	

	public static String secretMode() {
		return ALGORITHM_3DES;
	}

	/**
	 * 根据算法名称，获取不同的密钥
	 * 
	 * @param secretMode
	 * @return
	 */
	

	public static byte[] secretkey(String secretMode) {
		if (ALGORITHM_3DES.equals(secretMode)) {
			return DESC3KEY;
		}
		return null;
	}

}
