package com.ctsi.ssdc.thirdauth.workwx.consts;

public class WorkWxLoginConstant {
    /**
     * token的缓存键(之所以里面加了单引号是因为Ehcache默认存储的是对象，而不是变量，如果存储的是变量就需要加单引号)
     */
    public static final String WX_TOKEN = "'WX_TOKEN'";

    /**
     * token的缓存键
     */
    public static final String WX_TONG_XUN_LU_TOKEN = "'WX_TONG_XUN_LU_TOKEN'";
}
