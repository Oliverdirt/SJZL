package com.ctsi.flow.param.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 19:06
 * @description ：
 */

@ApiModel("管理后台 - 流程实例的取消项")
@Data
public class FlowProcessInstanceCancelReq {

    @ApiModelProperty(value = "流程实例的编号", required = true, example = "1024")
    @NotEmpty(message = "流程实例的编号不能为空")
    private String id;

    @ApiModelProperty(value = "取消原因", required = true, example = "不请假了！")
    @NotEmpty(message = "取消原因不能为空")
    private String reason;
}
