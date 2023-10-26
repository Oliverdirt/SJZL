package com.ctsi.ssdc.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 用户名相关检测
 * @author MyBatis Generator
 */
@Component
public class ContainUserNameCheck implements IPasswordCheck {

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Value("${ctsi.password-check.check-contains-username:true}")
    private boolean checkContainsUserName;

    private String userName;

    @Override
    public CheckResult check(String password,CheckResult checkResult) throws Exception {
        if(!checkContainsUserName){
            return checkResult;
        }
        if(password.toLowerCase().indexOf(userName.toLowerCase())==-1){
            return checkResult;
        }else{
            checkResult.errorReason.add("密码不应包含用户名信息");
            return checkResult;
        }

    }
}
