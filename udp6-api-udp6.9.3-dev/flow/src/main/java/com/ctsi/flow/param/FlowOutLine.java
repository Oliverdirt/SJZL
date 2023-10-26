package com.ctsi.flow.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-08-31 15:17
 * @description ：出口路线
 */

@Data
@Builder

public class FlowOutLine {

    @ApiModelProperty(value = "路线名称")
    private String name;

    @ApiModelProperty(value = "变量名")
    private String key;

    @ApiModelProperty(value = "变量值")
    private String value;
}
