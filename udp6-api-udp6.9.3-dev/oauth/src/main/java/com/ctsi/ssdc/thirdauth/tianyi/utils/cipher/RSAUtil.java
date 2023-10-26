package com.ctsi.ssdc.thirdauth.tianyi.utils.cipher;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
* @author: hx
* @description: RSA工具类
* @date: Created in 12:08 20220510
*/
public class RSAUtil {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String RSA_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
    private static final String RSA_ALGORITHM_PADDING = "RSA/ECB/PKCS1Padding";

    private static final String NO_SUCH_ALGORITHM_EXCEPTION_MSG = "No such algorithm";
    private static final String INVALID_KEY_SPEC_EXCEPTION_MSG = "Invalid key spec";
    private static final String IO_EXCEPTION_MSG = "Reading key data error";
    private static final String NO_SUCH_PADDING_EXCEPTION_MSG = "No such padding";
    private static final String NULL_POINTER_EXCEPTION_MSG = "No key data found";
    private static final String INVALID_KEY_EXCEPTION_MSG = "Invalid key";
    private static final String ILLEGAL_BLOCK_SIZE_EXCEPTION_MSG = "Illegal block size";
    private static final String BAD_PADDING_EXCEPTION_MSG = "Bad padding";
    private static final String SIGNATURE_EXCEPTION_MSG = "Signature exception";
    private static final String UNSUPPORTED_ENCODING_EXCEPTION_MSG = "Unsupported encoding";

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
        RSAPublicKey publicKey;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] buffer = decoder.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
        } catch (InvalidKeySpecException e) {
            throw new InvalidKeySpecException(INVALID_KEY_SPEC_EXCEPTION_MSG);
        } catch (NullPointerException e) {
            throw new NullPointerException(NULL_POINTER_EXCEPTION_MSG);
        }
        return publicKey;
    }

    /**
     * 加载私钥
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        RSAPrivateKey privateKey;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] buffer = decoder.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
        } catch (InvalidKeySpecException e) {
            throw new InvalidKeySpecException(INVALID_KEY_SPEC_EXCEPTION_MSG);
        }  catch (NullPointerException e) {
            throw new NullPointerException(NULL_POINTER_EXCEPTION_MSG);
        }
        return privateKey;
    }

    /**
     * 加密过程
     *
     * @param publicKey 公钥
     * @param content   明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(RSAPublicKey publicKey, String content) throws Exception {
        byte[] plainTextData = content.getBytes("UTF-8");
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(RSA_ALGORITHM_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return ByteFormat.bytesToHexString(output);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
        } catch (NoSuchPaddingException e) {
            throw new NoSuchPaddingException(NO_SUCH_PADDING_EXCEPTION_MSG);
        } catch (InvalidKeyException e) {
            throw new InvalidKeyException(INVALID_KEY_EXCEPTION_MSG);
        } catch (IllegalBlockSizeException e) {
            throw new IllegalBlockSizeException(ILLEGAL_BLOCK_SIZE_EXCEPTION_MSG);
        } catch (BadPaddingException e) {
            throw new BadPaddingException(BAD_PADDING_EXCEPTION_MSG);
        }
    }

    /**
     * 解密过程
     *
     * @param privateKey 私钥
     * @param content    密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(RSAPrivateKey privateKey, String content) throws Exception {
        byte[] cipherData = ByteFormat.hexToBytes(content);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(RSA_ALGORITHM_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return new String(output, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
        } catch (NoSuchPaddingException e) {
            throw new NoSuchPaddingException(NO_SUCH_PADDING_EXCEPTION_MSG);
        } catch (InvalidKeyException e) {
            throw new InvalidKeyException(INVALID_KEY_EXCEPTION_MSG);
        } catch (IllegalBlockSizeException e) {
            throw new IllegalBlockSizeException(ILLEGAL_BLOCK_SIZE_EXCEPTION_MSG);
        } catch (BadPaddingException e) {
            throw new BadPaddingException(BAD_PADDING_EXCEPTION_MSG);
        }
    }

    /**
     * RSA签名
     * @param content 待签名数据
     * @param privateKey 私钥
     * @return 签名值
     */
    public static String sign(String content, PrivateKey privateKey) throws Exception {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            byte[] signed = signature.sign();
            return ByteFormat.bytesToHexString(signed);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
        } catch (InvalidKeyException e) {
            throw  new InvalidKeyException(INVALID_KEY_EXCEPTION_MSG);
        } catch (SignatureException e) {
            throw new SignatureException(SIGNATURE_EXCEPTION_MSG);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException(UNSUPPORTED_ENCODING_EXCEPTION_MSG);
        }
    }

    /**
     * RSA验签
     * @param content 待签名数据
     * @param sign 签名值
     * @param publicKey 分配给开发商公钥
     * @return 布尔值
     */
    public static boolean verifySign(String content, String sign, PublicKey publicKey) throws Exception {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update( content.getBytes(DEFAULT_CHARSET) );
            return signature.verify(ByteFormat.hexToBytes(sign));
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(NO_SUCH_ALGORITHM_EXCEPTION_MSG);
        } catch (InvalidKeyException e) {
            throw  new InvalidKeyException(INVALID_KEY_EXCEPTION_MSG);
        } catch (SignatureException e) {
            throw new SignatureException(SIGNATURE_EXCEPTION_MSG);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException(UNSUPPORTED_ENCODING_EXCEPTION_MSG);
        }
    }

    public static void main(String[] args) throws Exception {
        String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvC7WWvgcvpFmybFTHlCpWQvh7rpzatimcOFiGJ3OzCPz/YF0LWugr/46ub0Bdh5Cq/cb5ZmOJiA0KEK5QdM2V8/k4wtJeYl7wnwoEolXp5Swqs/hdYb/YOINRFFgHsbnrV3AY4S2x37ztJL0UrEWEONmlcWKLN4Srv89VGKgvJwIDAQAB";
        String privateKeyStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK8LtZa+By+kWbJsVMeUKlZC" +
                "+HuunNq2KZw4WIYnc7MI/P9gXQta6Cv/jq5vQF2HkKr9xvlmY4mIDQoQrlB0zZXz+TjC0l5iXvCfCgSiVenlLCqz+F1hv9g4g1EUWAexuetXcBjhLbHfvO0kvRSsRYQ42aVxYos3hKu/z1UYqC8nAgMBAAECgYBEsE6YkYcWXeLIzhPSoUSfxvXk6tcoR/U6FS4/rnmLVsqYl8LLMugKY+UkdAmI6iqNvrn2ogQLMvUS7wrIDZ2iRLdU5yiNfqMSlG2kPK2lMYa0GEQGH9xF6KwI/yJ+WtjIjPxCAFFUrRi1aCJlCNT/yav3j82g6uovG3ciTo6GeQJBAOHZfn5lCCKnF3iZX6BhH6C7CFYhTRdetJKfrGZqlj4tRbJDou54OjHe/eGT+o8xlrgu7PZFmnLu/I5ve36GvU0CQQDGafjbOS3DFQUvFA0ULJOQzdCAQvdx/AYQUsdW3EvwfK0S1ZuZqZoOykT7biy00vBF8pwXy4kL6GBOkY4cNTRDAkEAvYi/3iS9gg9F5ECafsZjO4kaguWpg55H3fDARqbWdVGa9vJKDuS4udeQqjl8gaU0/lbrJ/Xbmu6y5nnqYpGC1QJAfRWp0WrGssm5ruhRJnrDvPJDk9ij7rQXjorhKJ1RMpcm9Uy8/66wdMqHmormnEivOSmtxkKGb39b16xjjtzryQJAJEnDgK97Ktq1lGR/Nx7nwE6Uikvxc0V/UgAuJdB6Hj53hVWHwmoIpyillJvNfZNnNLEQxikglSIUpBQ++p0VqQ==";

        //平台使用合作方公钥加密
        String msg = "{\"email\":\"\",\"operator\":\"中国移动\"}";
        String encStr = RSAUtil.encrypt(RSAUtil.loadPublicKey(publicKeyStr), msg);
        System.out.println(encStr);

        //合作方使用自己私钥解密
        String decStr = RSAUtil.decrypt(RSAUtil.loadPrivateKey(privateKeyStr), encStr);
        System.out.println(decStr);

        //合作方使用自己私钥签名
        String signStr = sign(msg, loadPrivateKey(privateKeyStr));
        System.out.println(signStr);

        //平台使用合作方公钥验签
        System.out.println(verifySign(msg, signStr, loadPublicKey(publicKeyStr)));
    }
}