package com.ctsi.flow.param.request;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-14 10:53
 * @description ：
 */

@Data
public class FlowProcessDefCreateReq {

    // ========== 模型相关 ==========

    /**
     * 流程模型的编号
     */
    @NotEmpty(message = "流程模型编号不能为空")
    private String modelId;
    /**
     * 流程标识
     */
    @NotEmpty(message = "流程标识不能为空")
    private String key;
    /**
     * 流程名称
     */
    @NotEmpty(message = "流程名称不能为空")
    private String name;
    /**
     * 流程描述
     */
    private String description;
    /**
     * 流程分类
     * 参见 bpm_model_category 数据字典
     */
    private String category;
    /**
     * BPMN XML
     */
    @NotEmpty(message = "BPMN XML 不能为空")
    private byte[] bpmnBytes;

    // ========== 表单相关 ==========

    /**
     * 表单类型
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
