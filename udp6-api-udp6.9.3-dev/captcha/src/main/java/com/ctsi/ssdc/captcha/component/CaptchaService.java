package com.ctsi.ssdc.captcha.component;

import java.util.Map;

public interface CaptchaService {

	public Map<String,String> getCaptcha(String key) throws Exception;

	public boolean validateCaptcha(String key, Map<String,String> captcha);
}
