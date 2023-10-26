package com.ctsi.flow.param.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

/**
 * Bpm 流程实例的拓展表
 * 主要解决 Activiti ProcessInstance 和 HistoricProcessInstance 不支持拓展字段，所以新建拓展表
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowProcessInstanceExtDO extends BaseDO {

    /**
     * 编号，自增
     */
    private Long id;
    /**
     * 发起流程的用户编号
     *
     * 冗余 HistoricProcessInstance 的 startUserId 属性
     */
    private Long startUserId;
    /**
     * 流程实例的名字
     *
     * 冗余 ProcessInstance 的 name 属性，用于筛选
     */
    private String name;
    /**
     * 流程实例的编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
    /**
     * 流程定义的编号
     *
     * 关联 ProcessDefinition 的 id 属性
     */
    private String processDefinitionId;
    /**
     * 流程分类
     *
     * 冗余 ProcessDefinition 的 category 属性
     * 数据字典 bpm_model_category
     */
    private String category;
    /**
     * 流程实例的状态
     */
    private Integer status;
    /**
     * 流程实例的结果
     */
    private Integer result;
    /**
     * 结束时间
     *
     * 冗余 HistoricProcessInstance 的 endTime 属性
     */
    private Date endTime;

    /**
     * 提交的表单值
     */
    private Map<String, Object> formVariables;

}
