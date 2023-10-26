package com.ctsi.ssdc.security;

import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.service.CscpMenusService;
import com.ctsi.ssdc.admin.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Authenticate a user from the database.
 */

public class UserDetailsServiceImpl implements UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	private final UserService userService;
	private final CscpMenusService cscpMenusService;

	public UserDetailsServiceImpl(UserService userService, CscpMenusService cscpMenusService) {
		this.userService = userService;
		this.cscpMenusService = cscpMenusService;
	}


	@Override
	public UserDetails loadUserByUsername(final String login) {
		log.debug("Authenticating {}", login);
		String[] tenantAndUsers = login.split(",");

		return userService.finUserByUsernameAndTenantId(Long.valueOf(tenantAndUsers[0]),tenantAndUsers[2]).map(user -> createSpringSecurityUser(tenantAndUsers[2],tenantAndUsers[1], user))
				.orElseThrow(() -> new UsernameNotFoundException("User  " + login + " was not found"));


	}

	protected CscpHxUserDetail createSpringSecurityUser(String lowercaseLogin, String tenantName, CscpUserDetailDTO cscpUserDetailDTO) {

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

		cscpMenusService.findByUserId(cscpUserDetailDTO.getUserId()).forEach(menu -> {

			if (StringUtils.isNotEmpty(menu.getPermissionCode())) {
				grantedAuthorities.add(new SimpleGrantedAuthority(menu.getPermissionCode()));
			}

		});

		return new CscpHxUserDetail(cscpUserDetailDTO.getTenantId(),tenantName,cscpUserDetailDTO.getUserId(), cscpUserDetailDTO.getUsername(), cscpUserDetailDTO.getPassword(), grantedAuthorities);
	}

//		@Override
//	public UserDetails loadUserByUsername(final String login) {
//		log.debug("Authenticating {}", login);
//		String[] tenantAndUsers = login.split(",");
//		return userService.findByUserName(login).map(user -> createSpringSecurityUser(login, user))
//				.orElseThrow(() -> new UsernameNotFoundException("User  " + login + " was not found"));
//
//
//	}
//
//	protected CscpUserDetail createSpringSecurityUser(String lowercaseLogin, CscpUser user) {
//		// if (!user.getActivated()) {
//		// throw new UserNotActivatedException("User " + lowercaseLogin + " was
//		// not
//		// activated");
//		// }
//
//		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//
//		cscpMenusService.findByUserId(user.getId()).forEach(menu -> {
//
//			if (StringUtils.isNotEmpty(menu.getPermissionCode())) {
//				grantedAuthorities.add(new SimpleGrantedAuthority(menu.getPermissionCode()));
//			}
//
//		});
//
//		return new CscpUserDetail(user.getId(), user.getUsername(), user.getPassword(), grantedAuthorities);
//	}
}
