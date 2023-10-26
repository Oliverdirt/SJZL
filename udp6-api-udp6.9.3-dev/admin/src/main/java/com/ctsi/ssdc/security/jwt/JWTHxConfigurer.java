package com.ctsi.ssdc.security.jwt;

import com.ctsi.ssdc.admin.service.CscpTenantService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTHxConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_BEARER = "Bearer ";
    
    private TokenHxProvider tokenHxProvider;
    private CscpTenantService cscpTenantService;

    public JWTHxConfigurer(TokenHxProvider tokenHxProvider, CscpTenantService cscpTenantService) {
        this.tokenHxProvider = tokenHxProvider;
        this.cscpTenantService = cscpTenantService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JWTHxFilter customFilter = new JWTHxFilter(tokenHxProvider,cscpTenantService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
