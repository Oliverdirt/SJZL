package com.ctsi.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分配规则的候选人脚本枚举
 * @author sunsheng
 */
@Getter
@AllArgsConstructor
public enum FlowTaskRuleScriptEnum {
    /**
     * @Description: 流程发起人
     **/
    START_USER("10", "流程发起人");

    /**
     * 脚本编号
     */
    private final String id;
    /**
     * 脚本描述
     */
    private final String desc;

}
