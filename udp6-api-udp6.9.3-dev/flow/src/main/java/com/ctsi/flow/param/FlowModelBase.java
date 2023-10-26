package com.ctsi.flow.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-08 15:45
 * @description ：
 */

@ApiModel("流程定义基础信息")
@Data
public class FlowModelBase {

    @ApiModelProperty(value = "流程标识", required = true, example = "leave")
    @NotEmpty(message = "流程标识不能为空")
    private String key;

    @ApiModelProperty(value = "流程名称", required = true, example = "请假流程")
    @NotEmpty(message = "流程名称不能为空")
    private String name;

    @ApiModelProperty(value = "流程描述", example = "这是一个请假流程")
    private String description;

    @ApiModelProperty(value = "表单编号", example = "1024")
    private Long formId;

}
