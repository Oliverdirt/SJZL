package com.ctsi.ssdc.model;


/**
 * @author MyBatis Generator
 */
public interface IPasswordCheck {

    /**
     * 校验密码
     * @param password
     * @return boolean
     */
    CheckResult check(String password, CheckResult checkResult) throws Exception;
}
