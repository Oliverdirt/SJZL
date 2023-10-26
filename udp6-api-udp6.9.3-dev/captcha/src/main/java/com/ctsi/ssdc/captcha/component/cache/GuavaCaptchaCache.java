package com.ctsi.ssdc.captcha.component.cache;

import com.ctsi.ssdc.captcha.component.CaptchaCache;
import com.ctsi.ssdc.captcha.constants.CaptchaEnhanceConstant;
import com.ctsi.ssdc.captcha.exception.CaptchaMissingException;
import com.ctsi.ssdc.util.JacksonUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于google guava的实现，缓存验证码信息
 * @author ctsi
 *
 */

@Component
public class GuavaCaptchaCache implements CaptchaCache {

	// 缓存,适用于单节点部署
	private static LoadingCache<String, String> cache = CacheBuilder.newBuilder()// CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
			.expireAfterAccess(CaptchaEnhanceConstant.CAPTCHA_EXPIRED_SECOND, TimeUnit.SECONDS)
			.build(new CacheLoader<String, String>() {
				@Override
				public String load(String key) throws Exception {// 在缓存不存在时通过CacheLoader的实现自动加载缓存
					// TODO Auto-generated method stub
					return null;
				}

			});

	@Override
	public boolean putKey(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getKey(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean putCaptcha(String key, Map<String, String> value) {
		try {
			cache.put(key, JSONObject.fromObject(value).toString());
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getCaptcha(String key) {
		String mapStr;
		try {
			mapStr = cache.get(key);// 获取value,验证码校验信息
		} catch (Exception e) {
			throw new CaptchaMissingException();
		}

		Map<String, String> code = null;
		try {
			code = (Map<String, String>) JacksonUtil.json2Bean(mapStr, Map.class);//String转Map
		} catch (Exception e) {
			throw new CaptchaMissingException();
		}
		return code;
	}

	@Override
	public boolean deleteCaptcha(String key) {
		try {
			cache.invalidate(key);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

}
