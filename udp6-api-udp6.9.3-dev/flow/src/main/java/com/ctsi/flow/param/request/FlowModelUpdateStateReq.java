package com.ctsi.flow.param.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 16:01
 * @description ：修改状态入参
 */



@ApiModel("管理后台 - 流程模型更新状态项")
@Data
public class FlowModelUpdateStateReq {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    @NotNull(message = "编号不能为空")
    private String id;

    @ApiModelProperty(value = "状态", required = true, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer state;
}
