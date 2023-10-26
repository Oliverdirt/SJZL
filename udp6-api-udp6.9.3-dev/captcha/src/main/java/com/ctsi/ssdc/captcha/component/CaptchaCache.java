package com.ctsi.ssdc.captcha.component;

import java.util.Map;

public interface CaptchaCache {
	
	public boolean putKey(String key);
	
	public String getKey(String key);
	/**
	 * 存储验证码
	 * @param key 验证码KEY
	 * @param value 验证码校验信息
	 * @return 是否成功
	 */
	public boolean putCaptcha(String key,Map<String,String> value);
	/**
	 * 获取验证码
	 * @param key 验证码KEY
	 * @return 验证码校验信息
	 */
	public Map<String,String> getCaptcha(String key);
	/**
	 * 删除验证码
	 * @param key 验证码KEY
	 * @return 是否成功
	 */
	public boolean deleteCaptcha(String key);
}
