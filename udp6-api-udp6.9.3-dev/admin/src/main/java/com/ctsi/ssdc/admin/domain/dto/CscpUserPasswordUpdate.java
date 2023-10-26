package com.ctsi.ssdc.admin.domain.dto;

import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


public class CscpUserPasswordUpdate implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 5257123406387889586L;

    @JsonSerialize(using = LongtoStringSerialize.class)
	private Long userId;

    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long tenantId;

    private String tenantAccount;

	@NotNull
	private String userName;
    private String oldPassword;
    private String newPassword;
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantAccount() {
        return tenantAccount;
    }

    public void setTenantAccount(String tenantAccount) {
        this.tenantAccount = tenantAccount;
    }
}
