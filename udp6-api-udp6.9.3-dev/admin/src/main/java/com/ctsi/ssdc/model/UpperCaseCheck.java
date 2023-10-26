package com.ctsi.ssdc.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 大写检测
 * @author MyBatis Generator
 */
@Component
public class UpperCaseCheck implements IPasswordCheck {

    @Value("${ctsi.password-check.check-uppercase:true}")
    private boolean checkUpperCase;

    @Override
    public CheckResult check(String password,CheckResult checkResult) throws Exception{
        if(!checkUpperCase){
            return checkResult;
        }

        char[] chPass = password.toCharArray();
        boolean flag = false;
        int charCount = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (Character.isUpperCase(chPass[i])) {
                charCount++;
            }
        }
        if (charCount >= 1) {
            flag = true;
        }

        if(!flag){
//            throw new Exception("密码不包含大写字母");
            checkResult.confirmInfo.add("大写字母");
        }else {
            checkResult.confirmCount++;
        }
        return checkResult;

    }
}
