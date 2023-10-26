package com.ctsi.ssdc.thirdauth.tianyi.utils.cipher;

import java.nio.charset.StandardCharsets;

/**
* @author: hx
* @description: xxtea加解密工具类
*
*/
public class XXTeaUtil {
    public XXTeaUtil() {
    }

    public static byte[] encrypt(byte[] plainData, byte[] key) {
        return plainData != null && plainData.length != 0 && key != null ? toByteArray(encrypt(toIntArray(plainData, true), toIntArray(key, false)), false) : null;
    }

    public static byte[] decrypt(byte[] cipherData, byte[] key) {
        return cipherData != null && cipherData.length != 0 && key != null ? toByteArray(decrypt(toIntArray(cipherData, false), toIntArray(key, false)), true) : null;
    }

    private static int[] encrypt(int[] v, int[] k) {
        int n = v.length - 1;
        if (n < 1) {
            return v;
        } else {
            if (k.length < 4) {
                int[] key = new int[4];
                System.arraycopy(k, 0, key, 0, k.length);
                k = key;
            }

            int z = v[n];
            int y = v[0];
            int delta = -1640531527;
            int sum = 0;

            int e;
            int p;
            for(int var9 = 6 + 52 / (n + 1); var9-- > 0; z = v[n] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z)) {
                sum += delta;
                e = sum >>> 2 & 3;

                for(p = 0; p < n; ++p) {
                    y = v[p + 1];
                    z = v[p] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
                }

                y = v[0];
            }

            return v;
        }
    }

    private static int[] decrypt(int[] v, int[] k) {
        int n = v.length - 1;
        if (n < 1) {
            return v;
        } else {
            if (k.length < 4) {
                int[] key = new int[4];
                System.arraycopy(k, 0, key, 0, k.length);
                k = key;
            }

//            int var10000 = v[n];
            int y = v[0];
            int delta = -1640531527;
            int q = 6 + 52 / (n + 1);

            for(int sum = q * delta; sum != 0; sum -= delta) {
                int e = sum >>> 2 & 3;

                int p;
                int z;
                for(p = n; p > 0; --p) {
                    z = v[p - 1];
                    y = v[p] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
                }

                z = v[n];
                y = v[0] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
            }
            return v;
        }
    }

    private static int[] toIntArray(byte[] data, boolean includeLength) {
        int n = (data.length & 3) == 0 ? data.length >>> 2 : (data.length >>> 2) + 1;
        int[] result;
        if (includeLength) {
            result = new int[n + 1];
            result[n] = data.length;
        } else {
            result = new int[n];
        }
        n = data.length;
        for(int i = 0; i < n; ++i) {
            result[i >>> 2] |= (255 & data[i]) << ((i & 3) << 3);
        }
        return result;
    }

    private static byte[] toByteArray(int[] data, boolean includeLength) {
        int n = data.length << 2;
        if (includeLength) {
            int m = data[data.length - 1];
            if (m > n || m <= 0) {
                return null;
            }

            n = m;
        }
        byte[] result = new byte[n];
        for(int i = 0; i < n; ++i) {
            result[i] = (byte)(data[i >>> 2] >>> ((i & 3) << 3) & 255);
        }
        return result;
    }

    public static void main(String[] args) {
        String plainText = "a=1&b=2&c=3";
        String appSecret = "N4rg8jiKy8618nY9B5uUKenVLyr0cNwp";
        try {
            //xxtea 加密
            byte[] key = appSecret.getBytes("UTF-8");
            byte[]  encBytes = encrypt(plainText.getBytes("UTF-8"), key);
            String params = ByteFormat.bytesToHexString(encBytes);
            System.out.println(params);

            //xxtea解密
            byte[] pText = decrypt(ByteFormat.hexToBytes(params), key);
            System.out.println(new String(pText, StandardCharsets.UTF_8));
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
}
