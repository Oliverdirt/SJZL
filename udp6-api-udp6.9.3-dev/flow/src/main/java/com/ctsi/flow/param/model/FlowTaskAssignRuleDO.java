package com.ctsi.flow.param.model;



import lombok.*;

import java.util.Set;

/**
 * @Description: 任务分配的规则表，用于自定义配置每个任务的负责人、候选人的分配规则。
 * @Author: sunsheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowTaskAssignRuleDO extends BaseDO {

    /**
     * {@link #processDefinitionId} 空串，用于标识属于流程模型，而不属于流程定义
     * 不使用空串的原因，Oracle 针对空串，会处理成 null，进而导致无法检索
     */
    public static final String PROCESS_DEFINITION_ID_NULL = "DEFAULT";

    /**
     * 编号
     */
    private Long id;

    /**
     * 流程模型编号
     *
     * 关联 Model 的 id 属性
     */
    private String modelId;
    /**
     * 流程定义编号
     *
     * 关联 ProcessDefinition 的 id 属性
     */
    private String processDefinitionId;
    /**
     * 流程任务的定义 Key
     *
     * 关联 Task 的 taskDefinitionKey 属性
     */
    private String taskDefinitionKey;

    /**
     * 规则类型
     *
     */
    private Integer type;
    /**
     * 规则值数组，一般关联指定表的编号
     * 根据 type 不同，对应的值是不同的：
     */
    private Set<Long> options;

}
