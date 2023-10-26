package com.ctsi.ssdc.captcha.component.cache;

import com.ctsi.ssdc.captcha.component.CaptchaCache;
import com.ctsi.ssdc.captcha.constants.CaptchaEnhanceConstant;
import com.ctsi.ssdc.captcha.exception.CaptchaMissingException;
import com.ctsi.ssdc.util.JacksonUtil;
import com.ctsi.ssdc.utils.RedisUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RedisCaptchaCache implements CaptchaCache{
	
	public  static final String KEY_PREFIX = "KEY_";
	
	@Autowired
	RedisUtil redisUtil;

	@Override
	public boolean putKey(String key) {
		return redisUtil.set(KEY_PREFIX+key, "",CaptchaEnhanceConstant.KEY_EXPIRED_SECOND);
	}

	@Override
	public String getKey(String key) {
		return (String) redisUtil.get(KEY_PREFIX+key);
	}

	@Override
	public boolean putCaptcha(String key, Map<String, String> value) {
		
		return redisUtil.set(key, JSONObject.fromObject(value).toString(), CaptchaEnhanceConstant.CAPTCHA_EXPIRED_SECOND);
	}

	@Override
	public Map<String, String> getCaptcha(String key) {
		
		String mapStr = (String) redisUtil.get(key);
				
		Map<String, String> code = null;
		try {
			code = (Map<String,String>)JacksonUtil.json2Bean(mapStr, Map.class);
		} catch (Exception e) {
			throw new CaptchaMissingException();
		}
		return code;
		
	}

	@Override
	public boolean deleteCaptcha(String key) {
			return redisUtil.delete(key)!=null && redisUtil.delete(key)>0;
	}

}
