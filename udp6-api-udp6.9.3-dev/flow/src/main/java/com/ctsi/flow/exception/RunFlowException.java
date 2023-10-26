package com.ctsi.flow.exception;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-05 17:02
 * @description : 工作流运行异常
 */
public class RunFlowException extends RuntimeException {

    private static final long serialVersionUID = 4523697480985114004L;

    private int code;

    public RunFlowException(int code) {
        this.code = code;
    }

    public RunFlowException(String message) {
        super(message);
    }

    public RunFlowException(int code, String message) {
        super(message);
        this.code = code;
    }

    public RunFlowException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
