package com.ctsi.flow.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-12 15:01
 * @description ：表单基础信息类
 */

@Data
public class FlowFormBase {

    @ApiModelProperty(value = "表单名称", required = true, example = "请假表单")
    @NotNull(message = "表单名称不能为空")
    private String name;

    @ApiModelProperty(value = "表单状态", required = true, notes = "参见 CommonStatusEnum 枚举", example = "1")
    @NotNull(message = "表单状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "备注", example = "我是备注")
    private String remark;
}
