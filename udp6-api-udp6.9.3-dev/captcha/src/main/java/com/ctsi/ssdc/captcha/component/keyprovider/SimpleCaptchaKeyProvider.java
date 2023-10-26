package com.ctsi.ssdc.captcha.component.keyprovider;

import java.util.UUID;

import org.springframework.stereotype.Component;
import com.ctsi.ssdc.captcha.component.CaptchaKeyProvider;

@Component
public class SimpleCaptchaKeyProvider implements CaptchaKeyProvider {

	@Override
	public String getKey() {
		return UUID.randomUUID().toString();
	}

	@Override
	public boolean validateKey(String key) {
		// TODO
		return true;
	}

}
