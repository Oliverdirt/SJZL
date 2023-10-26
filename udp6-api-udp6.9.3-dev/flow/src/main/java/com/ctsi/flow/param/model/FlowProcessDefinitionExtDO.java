package com.ctsi.flow.param.model;

import lombok.*;

import java.util.List;

/**
 * @Description: Bpm 流程定义的拓展表
 * @Author: sunsheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowProcessDefinitionExtDO extends BaseDO {

    /**
     * 编号
     */
    private Long id;
    /**
     * 流程定义的编号
     *
     * 关联 ProcessDefinition 的 id 属性
     */
    private String processDefinitionId;
    /**
     * 流程模型的编号
     *
     * 关联 Model 的 id 属性
     */
    private String modelId;
    /**
     * 描述
     */
    private String description;

    /**
     * 表单类型
     *
     */
    private Integer formType;
    /**
     * 动态表单编号
     */
    private Long formId;
    /**
     * 表单的配置
     */
    private String formConf;
    /**
     * 表单项的数组
     *
     */
    private List<String> formFields;
    /**
     * 自定义表单的提交路径，使用 Vue 的路由地址
     */
    private String formCustomCreatePath;
    /**
     * 自定义表单的查看路径，使用 Vue 的路由地址
     */
    private String formCustomViewPath;


}
