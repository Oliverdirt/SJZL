package com.ctsi.ssdc.security;

import com.ctsi.ssdc.config.CtsiProperties;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.json.Cas30JsonServiceTicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

public class CasHxAuthenticationProvider implements AuthenticationProvider, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(CasHxAuthenticationProvider.class);

	private final CtsiProperties ctsiProperties;
	private final UserDetailsService userDetailsService;

	@Value("${ctsi.saasModel:false}")
	private boolean saasModel;

	public CasHxAuthenticationProvider(CtsiProperties ctsiProperties, UserDetailsService userDetailsService) {
		this.ctsiProperties = ctsiProperties;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (CasHxAuthenticationToken.class.isAssignableFrom(authentication));
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		CasHxAuthenticationToken casToken = (CasHxAuthenticationToken) authentication;
		String principal = null;
		Cas30JsonServiceTicketValidator validator = new Cas30JsonServiceTicketValidator(
				ctsiProperties.getSecurity().getAuthentication().getCas().getCasServerUrlPrefix());
		try {
			Assertion assertion = validator.validate(casToken.getPrincipal(), casToken.getServiceUrl());

			if (assertion != null && assertion.isValid()) {
				principal = assertion.getPrincipal().getName();
			}
		} catch (TicketValidationException e) {
			throw new BadCredentialsException("TicketValidationException", e);
		}
		if(saasModel){
			throw new BadCredentialsException("多租户下暂不支持cas");
		}
		//非租户默认使用默认租户id和默认租户default
		principal = "1478649882954985474,default," + principal;
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
