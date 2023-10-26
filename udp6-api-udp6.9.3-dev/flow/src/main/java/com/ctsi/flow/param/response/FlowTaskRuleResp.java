package com.ctsi.flow.param.response;

import com.ctsi.flow.param.FlowTaskAssignRuleBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 10:23
 * @description ：流程任务规则返回列表项
 */


@ApiModel("流程任务规则返回")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowTaskRuleResp  extends FlowTaskAssignRuleBase {

    @ApiModelProperty(value = "任务分配规则的编号", required = true, example = "1024")
    private Long id;

    @ApiModelProperty(value = "流程模型的编号", required = true, example = "2048")
    private String modelId;

    @ApiModelProperty(value = "流程定义的编号", required = true, example = "4096")
    private String processDefinitionId;

    @ApiModelProperty(value = "流程任务定义的编号", required = true, example = "2048")
    private String taskDefinitionKey;

    @ApiModelProperty(value = "流程任务定义的名字", required = true, example = "领导审批")
    private String taskDefinitionName;

}
