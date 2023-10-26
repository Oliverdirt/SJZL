package com.ctsi.flow.param.response;

import lombok.Data;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-13 16:57
 * @description ：
 */

@Data
public class FlowModelMetaInfoResp {

    /**
     * 流程描述
     */
    private String description;

    /**
     * 表单编号
     */
    private Long formId;

    /**
     * 表单编号
     */
    private Integer backState;
}
