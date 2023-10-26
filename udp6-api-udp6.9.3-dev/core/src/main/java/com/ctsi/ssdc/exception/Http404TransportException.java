package com.ctsi.ssdc.exception;

import org.zalando.problem.Status;

public class Http404TransportException extends HttpTransportException{
    public Http404TransportException(String defaultMessage, String entityName, String errorKey) {
        super(ErrorConstants.DEFAULT_TYPE, Status.NOT_FOUND, defaultMessage, entityName, errorKey);
    }

}
