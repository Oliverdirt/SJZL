package com.ctsi.flow.param.request;

import com.ctsi.flow.param.FlowTaskAssignRuleBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 18:50
 * @description ：
 */

@ApiModel("管理后台 - 流程任务分配规则的更新项")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowTaskAssignRuleUpdateReq extends FlowTaskAssignRuleBase {

    @ApiModelProperty(value = "任务分配规则的编号", required = true, example = "1024")
    @NotNull(message = "任务分配规则的编号不能为空")
    private Long id;

}
