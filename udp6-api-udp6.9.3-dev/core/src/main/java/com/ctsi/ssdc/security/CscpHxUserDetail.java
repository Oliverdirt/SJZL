package com.ctsi.ssdc.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CscpHxUserDetail extends User {

    /**
     *
     */
    private static final long serialVersionUID = 3899399998765880053L;

    public CscpHxUserDetail(String username, String password,
                            Collection<? extends GrantedAuthority> authorities) {

        super(username, password,  authorities);

    }

    public CscpHxUserDetail(Long tenantId,String tenantAccount,Long id, String username,
                            String password,Collection<? extends GrantedAuthority> authorities) {

        this(username,password,authorities);
        this.id = id;
        this.tenantAccount = tenantAccount;
        this.tenantId = tenantId;
    }

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    private String tenantAccount;

    public String getTenantAccount() {
        return tenantAccount;
    }

    public void setTenantAccount(String tenantAccount) {
        this.tenantAccount = tenantAccount;
    }
}
