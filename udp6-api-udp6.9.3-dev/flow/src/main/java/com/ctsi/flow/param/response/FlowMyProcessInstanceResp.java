package com.ctsi.flow.param.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-08-05 16:51
 * @description ：
 */

@ApiModel("我的流程实例的项")
@Data
public class FlowMyProcessInstanceResp {

    @ApiModelProperty(value = "流程名称", required = true, example = "清假流程")
    private String name;

    @ApiModelProperty(value = "提交时间", required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "表单编号", example = "1024", notes = "在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空")
    private Long formId;

    @ApiModelProperty(value = "流程实例ID", example = "a7703f7a-1494-11ed-a1dd-00ff0f407109")
    private String processInstanceId;

    @ApiModelProperty(value = "流程部署ID", example = "first:4:ee5287eb-1487-11ed-a1dd-00ff0f407109")
    private String processDeployId;

    @ApiModelProperty(value = "流程部署ID", example = "first:4:ee5287eb-1487-11ed-a1dd-00ff0f407109")
    private Boolean back;

}
