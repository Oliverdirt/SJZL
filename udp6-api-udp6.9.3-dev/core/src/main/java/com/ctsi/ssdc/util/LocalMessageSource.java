package com.ctsi.ssdc.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化，获取信息内容
 * @author lym
 *
 */
@Component
public class LocalMessageSource {
	@Autowired
	private MessageSource messageSource;
	/**
	 * 获取信息内容
	 * @param key
	 * @return message
	 */
	public String getMessage(String key) {
		return getMessage(key, null);
	}
	/**
	 * 获取信息内容
	 * @param key
	 * @param args 参数
	 * @return message
	 */
	public String getMessage(String key, Object[] args) {
		return getMessage(key, args, null);
	}
	/**
	 * 获取信息内容
	 * @param key
	 * @param args 参数
	 * @param defaultMessage 默认信息
	 * @return message
	 */
	public String getMessage(String key, Object[] args, String defaultMessage) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(key, args, defaultMessage, locale);
	}
}
