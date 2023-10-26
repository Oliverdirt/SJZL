package com.ctsi.flow.param.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-08 10:43
 * @description ：流程模型修改参数
 */

@ApiModel("流程模型修改请求参数")
@Data
public class FlowModelUpdateReq {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    @NotEmpty(message = "编号不能为空")
    private String id;

    @ApiModelProperty(value = "流程名称", example = "请假流程")
    private String name;

    @ApiModelProperty(value = "流程描述", example = "我是描述")
    private String description;

    @ApiModelProperty(value = "BPMN XML", required = true)
    private String bpmnXml;

    @ApiModelProperty(value = "表单编号", example = "1024")
    private Long formId;

    @ApiModelProperty(value = "可撤回状态", required = true, example = "1")
    private Integer backState;

    @ApiModelProperty(value = "自定义表单的路径，使用 Vue 的路由地址", example = "/bpm/oa/leave/create")
    private String formCustomCreatePath;

    @ApiModelProperty(value = "表单类型", example = "1")
    private String formType;

}
