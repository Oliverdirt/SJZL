package com.ctsi.ssdc.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BizException extends AbstractThrowableProblem {

    private final String entityName;

    private final String errorKey;

    public BizException(String defaultMessage) {
        this(ErrorConstants.DEFAULT_TYPE,Status.INTERNAL_SERVER_ERROR, defaultMessage, null, null );
    }

    public BizException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE,Status.INTERNAL_SERVER_ERROR, defaultMessage, entityName, errorKey );
    }

    public BizException(String defaultMessage,Status status, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE,status, defaultMessage, entityName, errorKey );
    }

    public BizException(URI type,Status status, String title, String entityName, String errorKey) {
        super(type, title, status, null, null, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + entityName + "." +errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
