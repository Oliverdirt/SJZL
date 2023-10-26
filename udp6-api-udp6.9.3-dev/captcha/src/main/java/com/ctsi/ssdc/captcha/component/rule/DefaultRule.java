package com.ctsi.ssdc.captcha.component.rule;

import org.springframework.stereotype.Component;

import com.ctsi.ssdc.captcha.component.ValidateRule;

@Component
public class DefaultRule implements ValidateRule {

	@Override
	public boolean accordWihtRule(String key) {
		
		return true;
	}

}
