package com.ctsi.ssdc.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 包含数字检测
 * @author MyBatis Generator
 */
@Component
public class ContainDigitCheck implements IPasswordCheck {

    @Value("${ctsi.password-check.check-digit:true}")
    private boolean checkDigit;

    @Override
    public CheckResult check(String password,CheckResult checkResult) throws Exception {
        if(!checkDigit){
            return checkResult;
        }

        char[] chPass = password.toCharArray();
        boolean flag = false;
        int numCount = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (Character.isDigit(chPass[i])) {
                numCount++;
            }
        }
        if (numCount >= 1){
            flag = true;
        }
        if(!flag){
//            throw new Exception("密码不包含数字");
            checkResult.confirmInfo.add("数字");
        }else{
            checkResult.confirmCount++;
        }
        return checkResult;

    }
}
