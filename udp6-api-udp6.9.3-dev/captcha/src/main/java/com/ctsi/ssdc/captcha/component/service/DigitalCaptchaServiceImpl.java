package com.ctsi.ssdc.captcha.component.service;

import com.ctsi.ssdc.captcha.component.CaptchaCache;
import com.ctsi.ssdc.captcha.component.CaptchaService;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.exception.ErrorConstants;
import com.github.pagehelper.util.StringUtil;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class DigitalCaptchaServiceImpl implements CaptchaService {
	
	//public final static String BASE64_IMG = "base64Img";
	
	public  static  final  String CODE = "code";
	
	@Resource(name = "${ctsi.captcha.cache:guavaCaptchaCache}")
	CaptchaCache captchaCache;

	@Autowired
	Producer kaptchaProducer;

	@Override
	public Map<String, String> getCaptcha(String key) throws Exception {
		// 生成验证码
		String code = kaptchaProducer.createText();
		BufferedImage kaptchaImage = kaptchaProducer.createImage(code);
		// 生成BASE64
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		ImageIO.write(kaptchaImage, "jpg", outputStream);
		
		String base64Img = Base64.getEncoder().encodeToString(outputStream.toByteArray());
		//返回给用户的验证码图片
		Map<String, String> imgMap = new HashMap<>();
		
		imgMap.put(key, "data:image/jpeg;base64," + base64Img);
		
		Map<String, String> cacheMap = new HashMap<>();
		
		cacheMap.put(CODE, code.toUpperCase());
		//服务端存储校验信息
		captchaCache.putCaptcha(key, cacheMap);
		
		return imgMap;
	}

	@Override
	public boolean validateCaptcha(String key, Map<String, String> captcha) {
		if(StringUtil.isEmpty(key)) {
			throw new BadRequestAlertException(ErrorConstants.CAPTCHA_TYPE, "The information of captcha is missing", "captcha.header", "missing");
		}
		
		Map<String, String> validateMap = captchaCache.getCaptcha(key);
		
		captchaCache.deleteCaptcha(key);
		
		if(validateMap==null || StringUtil.isEmpty(validateMap.get(CODE))) {
			return false;
		}
		if(captcha.get(CODE).equalsIgnoreCase(validateMap.get(CODE))){
			return true;
		}
		
		return false;
	}

}
