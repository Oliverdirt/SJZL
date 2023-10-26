package com.ctsi.ssdc.captcha.component;

public interface CaptchaKeyProvider {

	public String getKey();

	public boolean validateKey(String key);

}
