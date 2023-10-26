package com.ctsi.ssdc.errorcode.vo;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 错误码 Excel VO
 *
 * @author hx
 */
@Data
public class ErrorCodeExcelVO {

    /**
     * 错误码编号
     */
    private Long codeId;

    /**
     * 错误码类型
     */
    private String codeType;

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 错误码编码
     */
    private Integer code;

    /**
     * 错误码错误提示
     */
    private String codeMessage;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private ZonedDateTime createTime;

}
