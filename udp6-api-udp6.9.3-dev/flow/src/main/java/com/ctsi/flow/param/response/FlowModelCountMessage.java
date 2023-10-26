package com.ctsi.flow.param.response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-09-02 15:59
 * @description ：模型信息
 */


@ApiModel("流程模板项信息")
@Data
public class FlowModelCountMessage {

    private String name;

    private String deploymentId;

    private Long processInstanceDown;

    private Long processInstanceAll;

    private String procCountProp;

    private Long taskDown;

    private Long taskAll;

    private String taskCountProp;
}
