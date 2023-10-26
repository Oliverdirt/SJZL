package com.ctsi.flow.param.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-09-05 17:19
 * @description ：
 */

@ApiModel("流程占比图返参")
@Data
public class FlowPropMessageResp {

    private Long processInstanceDown;

    private Long processInstanceTodo;

    private String procCountProp;

    private Long taskDown;

    private Long taskTodo;

    private String taskCountProp;

}
