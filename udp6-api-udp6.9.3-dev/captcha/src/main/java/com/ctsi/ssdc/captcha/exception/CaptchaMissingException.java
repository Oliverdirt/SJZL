package com.ctsi.ssdc.captcha.exception;

import java.util.HashMap;
import java.util.Map;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import com.ctsi.ssdc.exception.ErrorConstants;

public class CaptchaMissingException extends AbstractThrowableProblem{

	private static final long serialVersionUID = 1L;
	
    private  static final String TITLE = "Captcha Missing";
    private  static final String DETAIL = "The captcha is missing";
	private  static final String MESSAGE = "captcha.missing";
    
	
	public CaptchaMissingException() {
		super(ErrorConstants.CAPTCHA_TYPE, TITLE, Status.BAD_REQUEST, DETAIL, null, null, getProblemParameters(MESSAGE));
	}

	private static Map<String, Object> getProblemParameters(String msg) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", msg);
        return parameters;
    }

}