package com.ctsi.ssdc.thirdauth.tianyi.config;

public class LoginboxConfig {
    // 统一为：redirect（用于重定向接口的显示说明）
    public static final String FORMAT = "redirect";
    // TODO: 可不传
    // TODO: 注册成功后返回的URL（登录框右下角注册链接）。默认注册后返回天翼账号，如需注册后返回接入方，可传入该参数。（regReturnUrl如带参数，整个regReturnUrl需进行URL编码）
    public static final String REG_RETURN_URL = "";
    // Web登录框参数
    public static final String UNIFY_ACCOUNT_LOGIN_URL = "https://open.e.189.cn/api/logbox/oauth2/web/unifyAccountLogin.do";
    // 统一为：10010
    public static final int CLIENT_TYPE_WEB = 10010;
    // 调用的接口版本号：v1.5
    public static final String VERSION_WEB = "v1.5";

    public static final String CHARSET = "UTF-8";

    //获取用户信息地址
    public static final String GET_USER_INFO_URL = "https://open.e.189.cn/openapi/asymauth/gbcs/getUserInfo.do";
    /**
     * 测试RSA公钥
     */
    public static final String PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0Jr1NzVUQMburkZT6Rkt0eaPmH8TN6E258l2tZMJgVCP/sL4oKjroKYmNPBkSSiLKFr9wwJqfesMeef6ChGRUXjG6DX0oxQRe0f5/UnyEm/NicJwz9xwkU34gbuo1VB/EA2QZ5dl1rj9iSsiqKLK6/QFlVuzslRdAXYZC79vprwIDAQAB";
    /**
     * 测试RSA私钥
     */
    public static final String PRI_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALQmvU3NVRAxu6uRlPpGS3R5o+YfxM3oTbnyXa1kwmBUI/+wvigqOugpiY08GRJKIsoWv3DAmp96wx55/oKEZFReMboNfSjFBF7R/n9SfISb82JwnDP3HCRTfiBu6jVUH8QDZBnl2XWuP2JKyKoosrr9AWVW7OyVF0BdhkLv2+mvAgMBAAECgYA3q3D8ReNvaa7wycAbxvUyPRSk3FRYvNyoBXllR6m5Usb9EITyrHofnBZhipzExhwSDw2BrkMZ3TnGhTRc6MSHuQAnkI8g1yB/UJRLX752M+m/MBEm/xNEK4D1z1yIueGv8zxjKVdNw8o8WGJV0V2FMKWLWW/r6PfcUTGERQNIQQJBANv9O2q0cEB7lujyuYvFVTKzG6d+msrzSqZ6yNWQSqVfGy5W2KE39Bg9Iklg0nWg0hWjO+Kn+wQ2BuidlpXn+ykCQQDRpBRx2w6y7yPR8ZThpHPK8HL0ZKarf7oW7ZViAXspL3hhnMXSeq9oTMctdm1riCTlAeJ6Cu5Rk/K0sjLRFbEXAkEAooCGEoVlYgypZjxeWWQUUlZB+FEqUP60xxNuJWvn6A7AClP2w+iMNBd8q02NSXzZk0g4JW7Gms65/bzjDqsFOQJAUFqz+3AbkjpXYIFYr6R5PbAsX8C0ocezbFZki8xFiOPJIKEWsjaURUYokiRyjGUCv0SZqm3GKz6qKNik4b+OwQJAfynVAyHawEWB7laoDiKMk4rtX55TQdtzqmIlfI63wMCmtAByOauQp6YiUzht6I73C/wBQ7o49ys0RTU0LyDQuQ==";
    //获取token请求地址
    public static final String GET_ACCESS_TOKEN = "https://open.e.189.cn/api/logbox/oauth2/accessToken.do";
    //注销地址
    public static final String LOGOUT_URL = "https://open.e.189.cn/api/account/unifyAccountLogout.do";
}