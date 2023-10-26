package com.ctsi.ssdc.gen.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常
 */
public class BusinessException extends AbstractThrowableProblem {
    private  static  final  long serialVersionUID = 1L;


    public BusinessException(String title, StatusType status, String detail, String message, String path) {
        super(URI.create("problem/业务异常"), title, status, detail, null, null, getProblemParameters(message, path));
    }

    private static Map<String, Object> getProblemParameters(String message, String path) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", message);
        parameters.put("path", path);
        return parameters;
    }


}
