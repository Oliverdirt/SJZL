package com.ctsi.flow.param.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-08 10:22
 * @description ：模型创建请求参数
 */

@ApiModel("流程模型创建请求参数")
@Data
public class FlowModelCreateReq {

    @ApiModelProperty(value = "流程标识", required = true, example = "leave")
    @NotEmpty(message = "流程标识不能为空")
    private String key;

    @ApiModelProperty(value = "流程名称", required = true, example = "请假单")
    @NotEmpty(message = "流程名称不能为空")
    private String name;

    @ApiModelProperty(value = "流程描述", example = "这是一个请假单")
    private String description;

}
