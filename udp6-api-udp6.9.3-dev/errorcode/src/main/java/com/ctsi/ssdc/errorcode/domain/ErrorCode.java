package com.ctsi.ssdc.errorcode.domain;

import com.ctsi.ssdc.errorcode.exception.enums.GlobalErrorCodeConstants;
import com.ctsi.ssdc.errorcode.exception.enums.ServiceErrorCodeRange;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 错误码对象
 *
 * 全局错误码，占用 [0, 999], 参见 {@link GlobalErrorCodeConstants}
 * 业务异常错误码，占用 [1 000 000 000, +∞)，参见 {@link ServiceErrorCodeRange}
 *
 */
@Data
@ApiModel(description = "CscpErrorCode")
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
