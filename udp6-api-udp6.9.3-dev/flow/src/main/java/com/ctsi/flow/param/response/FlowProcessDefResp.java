package com.ctsi.flow.param.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 17:00
 * @description ：流程定义模型
 */

@ApiModel("管理后台 - 流程定义项")
@Data
public class FlowProcessDefResp {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "版本", required = true, example = "1")
    private Integer version;

    @ApiModelProperty(value = "流程名称", required = true)
    @NotEmpty(message = "流程名称不能为空")
    private String name;

    @ApiModelProperty(value = "流程描述", example = "我是描述")
    private String description;

    @ApiModelProperty(value = "流程分类", notes = "参见 bpm_model_category 数据字典", example = "1")
    private String category;

    @ApiModelProperty(value = "表单编号", example = "1024")
    private Long formId;

    @ApiModelProperty(value = "表单的配置", required = true)
    private String formConf;

    @ApiModelProperty(value = "中断状态", required = true, example = "1", notes = "参见 SuspensionState 枚举")
    private Integer suspensionState;
}
