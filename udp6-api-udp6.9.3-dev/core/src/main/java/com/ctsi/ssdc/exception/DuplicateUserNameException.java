package com.ctsi.ssdc.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown in case of a not activated user trying to authenticate.
 */
public class DuplicateUserNameException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public DuplicateUserNameException(String message) {
        super(message);
    }

    public DuplicateUserNameException(String message, Throwable t) {
        super(message, t);
    }
}
