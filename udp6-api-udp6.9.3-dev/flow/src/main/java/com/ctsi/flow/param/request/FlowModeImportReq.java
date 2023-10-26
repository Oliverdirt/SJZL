package com.ctsi.flow.param.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-11 9:25
 * @description ：导入模型请求入参
 */

@ApiModel("流程导入参数，相比新建多一个文件参数")
@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
public class FlowModeImportReq {

    @ApiModelProperty(value = "BPMN 文件", required = true)
    @NotNull(message = "BPMN 文件不能为空")
    private MultipartFile bpmnFile;

    @ApiModelProperty(value = "流程描述", example = "这是一个请假单")
    private String description;
}
