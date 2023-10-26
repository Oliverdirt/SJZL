package com.ctsi.flow.param.response;

import com.ctsi.flow.param.FlowModelBase;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-08 10:55
 * @description ：
 */

@ApiModel("流程模型分页中的每一项")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowModelPageItemResp extends FlowModelBase {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "表单名字", example = "请假表单")
    private String formName;

    @ApiModelProperty(value = "表单类型", example = "自定义表单")
    private String formType;

    @ApiModelProperty(value = "创建时间", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 最新部署的流程定义
     */
    private ProcessDefinition processDefinition;

    @ApiModel("流程定义")
    @Data
    public static class ProcessDefinition {

        @ApiModelProperty(value = "编号", required = true, example = "1024")
        private String id;

        @ApiModelProperty(value = "版本", required = true, example = "1")
        private Integer version;

        @ApiModelProperty(value = "部署时间", required = true)
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date deploymentTime;

        @ApiModelProperty(value = "中断状态", required = true, example = "1", notes = "参见 SuspensionState 枚举")
        private Integer suspensionState;

    }

    @ApiModelProperty(value = "可撤回状态", required = true, example = "1")
    private Integer backState;
}
