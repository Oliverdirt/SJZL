package com.ctsi.flow.param.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 19:12
 * @description ：
 */

@ApiModel("管理后台 - 流程实例的项")
@Data
public class FlowProcessInstanceResp {

    @ApiModelProperty(value = "流程实例的编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "流程名称", required = true, example = "清假流程")
    private String name;

    @ApiModelProperty(value = "提交时间", required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "结束时间", required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "结果标记", required = true)
    private Integer result;
    /**
     * 流程定义
     */
    private ProcessDefinition processDefinition;

    @ApiModel("流程定义信息")
    @Data
    public static class ProcessDefinition {

        @ApiModelProperty(value = "编号", required = true, example = "1024")
        private String id;

        @ApiModelProperty(value = "表单编号", example = "1024", notes = "在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空")
        private Long formId;

        @ApiModelProperty(value = "表单的配置", required = true)
        private String formConf;

        @ApiModelProperty(value = "BPMN XML", required = true)
        private String bpmnXml;

    }
}
