package com.ctsi.flow.param.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-12 19:04
 * @description ：
 */

@ApiModel("管理后台 - 流程表单精简项")
@Data
public class FlowFormSimpleResp {

    @ApiModelProperty(value = "表单编号", required = true, example = "1024")
    private Long id;

    @ApiModelProperty(value = "表单名称", required = true, example = "请假单")
    private String name;
}
