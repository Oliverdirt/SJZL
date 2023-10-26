package com.ctsi.flow.param.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 17:03
 * @description ：
 */

@ApiModel("管理后台 - 流程定义的分页项")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowProcessDefPageItemResp extends FlowProcessDefResp {

    @ApiModelProperty(value = "表单名字", example = "请假表单")
    private String formName;

    @ApiModelProperty(value = "部署时间", required = true)
    private Date deploymentTime;
}
