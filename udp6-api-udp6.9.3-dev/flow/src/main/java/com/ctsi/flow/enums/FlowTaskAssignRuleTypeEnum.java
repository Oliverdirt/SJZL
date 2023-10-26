package com.ctsi.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-27 10:27
 * @description ：
 */

@Getter
@AllArgsConstructor
public enum FlowTaskAssignRuleTypeEnum {
    // 用户
    /**
     * 用户
     */
    USER(1, "用户"),

    /**
     * 角色
     */
    // 角色
    ROLE(2, "角色"),

    // 部门成员
    DEPT_MEMBER(3,"部门的成员"),
    // 自定义脚本
    SCRIPT(4, "自定义脚本");

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String desc;
}
