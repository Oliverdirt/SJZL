package com.ctsi.ssdc.captcha.exception;

import com.ctsi.ssdc.exception.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.HashMap;
import java.util.Map;

public class CaptchaValidateException extends AbstractThrowableProblem{

	private static final long serialVersionUID = 1L;
	
    private final static String TITLE = "Validate Failed";
    private final static String DETAIL = "Validate captcha failed,the captcha is wrong or can not be found";
	private final static String MESSAGE = "captcha.validate.failed";
    
	
	public CaptchaValidateException(String path) {
		super(ErrorConstants.CAPTCHA_TYPE, TITLE, Status.INTERNAL_SERVER_ERROR, DETAIL, null, null, getProblemParameters(MESSAGE,path));
	}

	private static Map<String, Object> getProblemParameters(String msg, String path) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", msg);
        parameters.put("path", path);
        return parameters;
    }

}
