package com.ctsi.ssdc.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 特殊字符检测
 * @author MyBatis Generator
 */
@Component
public class SpecialCharCheck implements IPasswordCheck {
    public static final String SPECIAL_CHAR = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    @Value("${ctsi.password-check.check-special-character:true}")
    private boolean checkSpecialCharacter;

    @Override
    public CheckResult check(String password,CheckResult checkResult) throws Exception{
        if(!checkSpecialCharacter){
            return checkResult;
        }

        char[] chPass = password.toCharArray();
        boolean flag = false;
        int specialCount = 0;

        for (int i = 0; i < chPass.length; i++) {
            if (SPECIAL_CHAR.indexOf(chPass[i]) != -1) {
                specialCount++;
            }
        }

        if (specialCount >= 1){
            flag = true;
        }

        if(!flag){
//            throw new Exception("密码不包含特殊字符");
            checkResult.confirmInfo.add("#$%&'()*+等特殊字符");
        }else{
            checkResult.confirmCount++;
        }
        return checkResult;
    }
}
