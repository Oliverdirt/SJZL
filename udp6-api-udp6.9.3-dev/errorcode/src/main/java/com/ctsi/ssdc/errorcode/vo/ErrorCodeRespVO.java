package com.ctsi.ssdc.errorcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author hx
 */
@ApiModel("管理后台 - 错误码 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErrorCodeRespVO extends ErrorCodeBaseVO {

    @ApiModelProperty(value = "错误码编号", required = true, example = "1024")
    private Long codeId;

    @ApiModelProperty(value = "错误码类型", required = true, example = "1", notes = "参见 ErrorCodeTypeEnum 枚举类")
    private String codeType;

    @ApiModelProperty(value = "创建时间", required = true)
    private ZonedDateTime createTime;

}
