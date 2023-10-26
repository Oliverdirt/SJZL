package com.ctsi.flow.param.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 19:05
 * @description ：
 */

@ApiModel("管理后台 - 流程实例的创建项")
@Data
public class FlowProcessInstanceCreateReq {

    @ApiModelProperty(value = "流程定义的编号", required = true, example = "1024")
    @NotEmpty(message = "流程定义编号不能为空")
    private String processDefinitionId;

    @ApiModelProperty(value = "变量实例")
    private Map<String, Object> variables;

    @ApiModelProperty(value = "业务表id")
    private Long formId;
}
