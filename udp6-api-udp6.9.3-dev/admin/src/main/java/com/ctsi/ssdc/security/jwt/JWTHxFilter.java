package com.ctsi.ssdc.security.jwt;

import com.ctsi.ssdc.admin.constants.TenantStatusConstant;
import com.ctsi.ssdc.admin.service.CscpTenantService;
import com.ctsi.ssdc.security.CscpHxUserDetail;
import com.ctsi.ssdc.wrapper.CustomHttpServletRequestWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
@Component
public class JWTHxFilter extends GenericFilterBean {

    private TokenHxProvider tokenHxProvider;
    private CscpTenantService cscpTenantService;
    public JWTHxFilter(TokenHxProvider tokenHxProvider,CscpTenantService cscpTenantService) {
        this.tokenHxProvider = tokenHxProvider;
        this.cscpTenantService = cscpTenantService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt) && this.tokenHxProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenHxProvider.getAuthentication(jwt);
            CscpHxUserDetail cscpHxUserDetail = (CscpHxUserDetail) authentication.getPrincipal();
            int ret = cscpTenantService.checkTenantAccount(cscpHxUserDetail.getTenantAccount(), cscpHxUserDetail.getUsername());
            if(ret == TenantStatusConstant.TENANT_EXIST){
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        ServletRequest requestWrapper = null;
        if(servletRequest instanceof HttpServletRequest) {
            requestWrapper = new CustomHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        }
        if(requestWrapper == null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(JWTHxConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JWTHxConfigurer.AUTHORIZATION_BEARER)) {
            return bearerToken.substring(JWTHxConfigurer.AUTHORIZATION_BEARER.length(), bearerToken.length());
        }
        return null;
    }
}
