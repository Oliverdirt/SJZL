package com.ctsi.flow.param.model;

import lombok.*;

import java.util.List;


/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-12 16:51
 * @description ：
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowFormDO extends BaseDO {

    /**
     * 编号
     */
    private Long id;
    /**
     * 表单名
     */
    private String name;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 表单的配置
     */
    private String conf;
    /**
     * 表单项的数组
     *
     * 目前直接将 https://github.com/JakHuang/form-generator 生成的 JSON 串，直接保存
     * 定义：https://github.com/JakHuang/form-generator/issues/46
     */
    private List<String> fields;
    /**
     * 备注
     */
    private String remark;
    /**
     * 表单类型
     */
    private String formType;
}
