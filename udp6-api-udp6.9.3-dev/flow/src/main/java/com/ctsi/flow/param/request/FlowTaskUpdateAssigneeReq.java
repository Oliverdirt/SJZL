package com.ctsi.flow.param.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 19:29
 * @description ：
 */

@ApiModel("管理后台 - 流程任务的更新负责人的 Request VO")
@Data
public class FlowTaskUpdateAssigneeReq {

    @ApiModelProperty(value = "任务编号", required = true, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    @ApiModelProperty(value = "新审批人的用户编号", required = true, example = "2048")
    @NotNull(message = "新审批人的用户编号不能为空")
    private Long assigneeUserId;
}
