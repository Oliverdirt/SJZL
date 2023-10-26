package com.ctsi.flow.param.request;

import com.ctsi.flow.param.FlowTaskAssignRuleBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 18:46
 * @description ：
 */

@ApiModel("管理后台 - 流程任务分配规则的创建项")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowTaskAssignRuleCreateReq extends FlowTaskAssignRuleBase {

    @ApiModelProperty(value = "流程模型的编号", required = true, example = "1024")
    @NotEmpty(message = "流程模型的编号不能为空")
    private String modelId;

    @ApiModelProperty(value = "流程任务定义的编号", required = true, example = "2048")
    @NotEmpty(message = "流程任务定义的编号不能为空")
    private String taskDefinitionKey;
}
