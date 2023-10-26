package com.ctsi.flow.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 18:48
 * @description ：
 */

@Data
public class FlowTaskAssignRuleBase {

    @ApiModelProperty(value = "规则类型", required = true, example = "bpm_task_assign_rule_type")
//    @NotNull(message = "规则类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "规则值数组", required = true, example = "1,2,3")
//    @NotNull(message = "规则值数组不能为空")
    private Set<Long> options;
}
