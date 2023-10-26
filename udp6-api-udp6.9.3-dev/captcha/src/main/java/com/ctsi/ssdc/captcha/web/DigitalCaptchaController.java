package com.ctsi.ssdc.captcha.web;

import com.ctsi.ssdc.captcha.component.CaptchaKeyProvider;
import com.ctsi.ssdc.captcha.component.service.DigitalCaptchaServiceImpl;
import com.ctsi.ssdc.captcha.exception.CaptchaGenerateException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/catpcha")
public class DigitalCaptchaController {
	public final static String CAPTCHA="biyi-captcha";

	@Resource(name= "digitalCaptchaServiceImpl")
    DigitalCaptchaServiceImpl digitalCaptchaServiceImpl;

	@Resource(name = "${ctsi.captcha.keyProvider:simpleCaptchaKeyProvider}")
	CaptchaKeyProvider captchaKeyProvider;
	

	/*@GetMapping("/CaptchaKey")
	public String getCaptchaKey(HttpServletRequest request) {
		
		return captchaKeyProvider.getKey();
		
		
		
	}	*/

	@GetMapping("/digitalCaptcha")
	public Map<String,String> getDigitalCaptcha() {
		Map<String,String> map = new HashMap<>();
		String key = captchaKeyProvider.getKey();
		try {
			map = digitalCaptchaServiceImpl.getCaptcha(key);
		} catch (Exception e) {
			throw new CaptchaGenerateException("api/catpcha/digitalCaptcha");
		}
		return map;
	}
}
