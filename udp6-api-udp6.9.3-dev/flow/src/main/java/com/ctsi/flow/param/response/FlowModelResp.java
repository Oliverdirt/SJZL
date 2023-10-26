package com.ctsi.flow.param.response;

import com.ctsi.flow.param.FlowModelBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-08 10:59
 * @description ：流程模型返回对象
 */

@ApiModel("流程模型返回对象")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowModelResp extends FlowModelBase {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "BPMN XML", required = true)
    private String bpmnXml;

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;
}
