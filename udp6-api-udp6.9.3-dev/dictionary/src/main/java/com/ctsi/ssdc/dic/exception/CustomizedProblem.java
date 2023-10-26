package com.ctsi.ssdc.dic.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-02-10 11:18
 **/
public class CustomizedProblem extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String message;
    //构造器
    public CustomizedProblem(String title, StatusType status,
                             String detail, String message, String path) {
        super(URI.create("problem/customized-test"), title, status, detail, null, null,                 getProblemParameters(message,path));
        this.message = message;
    }
    //封装参数
    private static Map<String, Object> getProblemParameters(String message, String path) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", message);
        parameters.put("path", path);
        return parameters;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
