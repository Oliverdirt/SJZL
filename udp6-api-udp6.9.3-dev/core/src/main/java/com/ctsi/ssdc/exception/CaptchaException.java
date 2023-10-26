package com.ctsi.ssdc.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.HashMap;
import java.util.Map;
/**
 * 验证码校验失败
 * @author lym
 */
public class CaptchaException extends AbstractThrowableProblem {

	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE = "error.captcha";//验证码错误或已失效
    
    public CaptchaException(String defaultMessage) {
        super(ErrorConstants.DEFAULT_TYPE, defaultMessage, Status.UNAUTHORIZED, null, null, null, getAlertParameters());
    }

    private static Map<String, Object> getAlertParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", MESSAGE);
        return parameters;
    }
}
