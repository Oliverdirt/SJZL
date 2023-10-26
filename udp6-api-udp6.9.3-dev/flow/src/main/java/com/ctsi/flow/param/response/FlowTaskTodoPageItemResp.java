package com.ctsi.flow.param.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 19:20
 * @description ：
 */

@ApiModel("管理后台 - 流程任务的 Running 进行中的分页项")
@Data
public class FlowTaskTodoPageItemResp {

    @ApiModelProperty(value = "任务编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "任务名字", required = true, example = "请假单")
    private String name;

    @ApiModelProperty(value = "任务标识", required = true, example = "Activiti_1gr435")
    private String taskCode;

    @ApiModelProperty(value = "表单ID", required = true, example = "1551387661475536898")
    private String formId;

    @ApiModelProperty(value = "接收时间", required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date claimTime;

    @ApiModelProperty(value = "创建时间", required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "激活状态", required = true, example = "1", notes = "参见 SuspensionState 枚举")
    private Integer suspensionState;

    @ApiModelProperty(value = "是否需要签收", required = true, example = "1", notes = "参见 SuspensionState 枚举")
    private Integer claimFalg;

    /**
     * 所属流程实例
     */
    private ProcessInstance processInstance;

    @ApiModelProperty(value = "流程定义的标识", required = true, example = "2048")
    @Transient
    private String processDefinitionKey;

    @Data
    @ApiModel("流程实例")
    public static class ProcessInstance {

        @ApiModelProperty(value = "流程实例编号", required = true, example = "1024")
        private String id;

        @ApiModelProperty(value = "流程实例名称", required = true, example = "请假单")
        private String name;

        @ApiModelProperty(value = "发起人的用户编号", required = true, example = "1024")
        private Long startUserId;

        @ApiModelProperty(value = "发起人的用户昵称", required = true, example = "gc")
        private String startUserNickname;

        @ApiModelProperty(value = "流程定义的编号", required = true, example = "2048")
        private String processDefinitionId;
    }
}
