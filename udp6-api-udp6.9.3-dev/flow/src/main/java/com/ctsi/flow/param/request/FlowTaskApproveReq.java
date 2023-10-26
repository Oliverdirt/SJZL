package com.ctsi.flow.param.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 19:27
 * @description ：
 */

@ApiModel("管理后台 - 通过流程任务的 Request VO")
@Data
public class FlowTaskApproveReq {

    @ApiModelProperty(value = "任务编号", required = true, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    @ApiModelProperty(value = "审批意见", required = true, example = "不错不错！")
    @NotEmpty(message = "审批意见不能为空")
    private String reason;


    @ApiModelProperty(value = "流程表参数", example = "键值对")
    private Map<String, Object> flowMap;

    @ApiModelProperty(value = "下一步处理人", example = "1")
    private String nextHandler;

    @ApiModelProperty(value = "指定路线key", example = "key")
    private String key;

    @ApiModelProperty(value = "指定路线value", example = "value")
    private String value;

    @ApiModelProperty(value = "决策 ", example = "0")
    private String vote;

}
