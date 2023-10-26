package com.ctsi.ssdc.errorcode.exception;

import com.ctsi.ssdc.errorcode.exception.enums.ServiceErrorCodeRange;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务逻辑异常 Exception
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class ServiceException extends RuntimeException {

    /**
     * 业务错误码
     *
     * @see ServiceErrorCodeRange
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String codeMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {
    }

    public ServiceException(com.ctsi.ssdc.errorcode.domain.ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.codeMessage = errorCode.getMsg();
    }

    public ServiceException(Integer code, String message) {
        this.code = code;
        this.codeMessage = message;
    }

    public Integer getCode() {
        return code;
    }

    public ServiceException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return codeMessage;
    }

    public ServiceException setMessage(String message) {
        this.codeMessage = message;
        return this;
    }

}
