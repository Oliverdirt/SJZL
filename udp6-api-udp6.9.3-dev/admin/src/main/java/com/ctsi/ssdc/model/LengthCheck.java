package com.ctsi.ssdc.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 密码长度检测
 * @author MyBatis Generator
 */
@Component
public class LengthCheck implements IPasswordCheck {


    private static String minNum;

    @Value("${ctsi.password-check.check-min-length:10}")
    public void setMinNum(final String minNum) {
        setMinNumStatic(minNum);
    }
    private static void setMinNumStatic(final String minNum){
        LengthCheck.minNum = minNum;
    }


    private static String maxNum;
    @Value("${ctsi.password-check.check-max-length:20}")
    public void setMaxNum(final String maxNum) {
        setMaxNumStatic(maxNum);
    }
    private static void setMaxNumStatic(final String maxNum){
        LengthCheck.maxNum = maxNum;
    }

    @Override
    public CheckResult check(String password,CheckResult checkResult) throws Exception{
        boolean flag =false;
        if (StringUtils.isBlank(maxNum))  {
            minNum = StringUtils.isBlank(minNum) == true ? "0":minNum;
            if (password.length() >= Integer.parseInt(minNum)) {
                flag = true;
            }
        } else {
            minNum = StringUtils.isBlank(minNum) == true ? "0":minNum;
            if (password.length() >= Integer.parseInt(minNum) &&
                    password.length() <= Integer.parseInt(maxNum)) {
                flag = true;
            }
        }

        if(!flag){
            checkResult.errorReason.add(new StringBuilder().append("密码长度应大于等于")
                    .append(minNum).append(",小于等于").append(maxNum).toString());
//            throw new Exception(new StringBuilder().append("密码长度应大于等于").append(minNum).append(",小于等于").append(maxNum).toString());
        }
        return checkResult;

    }
}
