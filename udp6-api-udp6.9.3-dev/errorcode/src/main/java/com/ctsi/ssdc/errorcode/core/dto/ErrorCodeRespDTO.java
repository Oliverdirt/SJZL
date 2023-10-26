package com.ctsi.ssdc.errorcode.core.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 错误码的 Response DTO
 *
 * @author hx
 */
@Data
public class ErrorCodeRespDTO {

    /**
     * 错误码编码
     */
    private Integer code;
    /**
     * 错误码错误提示
     */
    private String codeMessage;
    /**
     * 更新时间
     */
    private ZonedDateTime updateTime;

}
