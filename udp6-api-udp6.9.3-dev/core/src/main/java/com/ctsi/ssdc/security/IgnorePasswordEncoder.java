package com.ctsi.ssdc.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class IgnorePasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return true;
	}

}
