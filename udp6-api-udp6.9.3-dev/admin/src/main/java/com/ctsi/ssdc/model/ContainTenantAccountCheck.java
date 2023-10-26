package com.ctsi.ssdc.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 租户账号相关检测
 * @author MyBatis Generator
 */
@Component
public class ContainTenantAccountCheck implements IPasswordCheck {

    private String tenantAccount;

    public String getTenantAccount() {
        return tenantAccount;
    }


    public void setTenantAccount(String tenantAccount) {
        this.tenantAccount = tenantAccount;
    }
    @Value("${ctsi.password-check.check-contains-tenantAccount:true}")
    private boolean checkContainsTenantAccount;

    @Override
    public CheckResult check(String password,CheckResult checkResult) throws Exception {
        if(!checkContainsTenantAccount){
            return checkResult;
        }
        if(password.toLowerCase().indexOf(tenantAccount.toLowerCase())==-1){
            return checkResult;
        }else{
            checkResult.errorReason.add("密码不应包含租户账号信息");
            return checkResult;
        }

    }
}
