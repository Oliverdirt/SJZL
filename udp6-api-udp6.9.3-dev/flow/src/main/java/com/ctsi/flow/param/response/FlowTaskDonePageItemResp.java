package com.ctsi.flow.param.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 19:22
 * @description ：
 */

@ApiModel("管理后台 - 流程任务的 Done 已完成的分页项")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowTaskDonePageItemResp extends FlowTaskTodoPageItemResp {

    @ApiModelProperty(value = "结束时间", required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "持续时间", required = true, example = "1000")
    private Long durationInMillis;

    @ApiModelProperty(value = "表单ID", required = true, example = "1551387661475536898")
    private String formId;

    @ApiModelProperty(value = "任务结果", required = true, notes = "参见 bpm_process_instance_result", example = "2")
    private Integer result;
    @ApiModelProperty(value = "审批建议", required = true, example = "不请假了！")
    private String reason;

}
