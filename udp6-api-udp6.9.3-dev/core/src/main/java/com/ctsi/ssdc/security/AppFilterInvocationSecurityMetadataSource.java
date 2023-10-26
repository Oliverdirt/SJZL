package com.ctsi.ssdc.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.Collection;

public class AppFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private FilterInvocationSecurityMetadataSource superMetadataSource;

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	public AppFilterInvocationSecurityMetadataSource(FilterInvocationSecurityMetadataSource datasource) {
		this.superMetadataSource = datasource;

	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

		//FilterInvocation fi = (FilterInvocation) object;
		//String url = fi.getRequest().getRequestURI();
		
		return superMetadataSource.getAttributes(object);

//		Collection<ConfigAttribute> atts = superMetadataSource.getAttributes(object);
//
//		if (CollectionUtils.isNotEmpty(atts)) {
//			return atts;
//		} 
//		else {
//			return SecurityConfig.createList("cscp.deny.others");
//		}

	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

}
