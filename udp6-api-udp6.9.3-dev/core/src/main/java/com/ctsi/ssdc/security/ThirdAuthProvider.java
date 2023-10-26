package com.ctsi.ssdc.security;

import com.ctsi.ssdc.config.CtsiProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

public class ThirdAuthProvider implements AuthenticationProvider, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(ThirdAuthProvider.class);

	private final CtsiProperties ctsiProperties;
	private final UserDetailsService userDetailsService;

	public ThirdAuthProvider(CtsiProperties ctsiProperties, UserDetailsService userDetailsService) {
		this.ctsiProperties = ctsiProperties;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (ThirdAuthToken.class.isAssignableFrom(authentication));
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		ThirdAuthToken thirdToken = (ThirdAuthToken) authentication;

		String principal = thirdToken.getPrincipal();

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal,
				StringUtils.EMPTY);

		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new IgnorePasswordEncoder());

		return provider.authenticate(token);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");

	}

}
