package com.ctsi.ssdc.captcha.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class CaptchaGenerateException extends AbstractThrowableProblem {

	private static final long serialVersionUID = 1L;
	
	private static final  URI TYPE = URI.create("problem/captcha");
    private static final String TITLE = "Generate Failed";
    private static final String DETAIL = "Can not generate captcha";
	private static final String MESSAGE = "captcha.generate.fail";
    
	public CaptchaGenerateException(String path) {
		super(TYPE, TITLE, Status.INTERNAL_SERVER_ERROR, DETAIL, null, null, getProblemParameters(MESSAGE,path));
	}

	private static Map<String, Object> getProblemParameters(String msg, String path) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", msg);
        parameters.put("path", path);
        return parameters;
    }
}
