package com.ctsi.flow.param.response;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * @author ：guoyanpei
 * @date ：Created in 2022-09-02 15:22
 * @description ：流程描述信息
 */

@ApiModel("流程概览返参")
@Data
public class FlowMessageResp {

    private Long modelCount;

    private Long processInstanceCount;

    private PageInfo<FlowModelCountMessage> flowModelCountMessageList;

}
