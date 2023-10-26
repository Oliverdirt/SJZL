package com.ctsi.flow.exception;

import lombok.Data;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-13 16:10
 * @description ：异常信息类
 */

@Data
public class ErrorCode {

    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误提示
     */
    private final String msg;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }
}
