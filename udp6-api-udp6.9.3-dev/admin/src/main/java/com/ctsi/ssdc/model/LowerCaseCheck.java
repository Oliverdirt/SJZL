package com.ctsi.ssdc.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 小写检测
 * @author MyBatis Generator
 */
@Component
public class LowerCaseCheck implements IPasswordCheck {

    @Value("${ctsi.password-check.check-lowercase:true}")
    private boolean checkLowerCase;

    @Override
    public CheckResult check(String password,CheckResult checkResult)throws Exception {
        if(!checkLowerCase){
            return checkResult;
        }

        char[] chPass = password.toCharArray();
        boolean flag = false;
        int charCount = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (Character.isLowerCase(chPass[i])) {
                charCount++;
            }
        }
        if (charCount >= 1) {
            flag = true;
        }

        if(!flag){
//            throw new Exception("密码不包含小写字母");
            checkResult.confirmInfo.add("小写字母");
        }else {
            checkResult.confirmCount++;
        }
        return checkResult;

    }
}
