package com.ctsi.ssdc.gen.domain;


import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TemplateEntity implements Serializable {
    /**
     * 主键值
     */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long pkValue;

    /**
     * 主键字段
     */
    private String pkField;

    /**
     * 表名
     */
    private String tableName;
    /**
     * 值列表
     */
    private List<Object> values;

    private List<List<Object>> valuesList;
    /**
     * 字段列表
     */
    private List<Object> list;

    /**
     * 查询列表字段
     */
    private List<String> queryFields;
    /**
     * 查询类型
     */
    private List<String> queryTypes;

    /**
     * 查询列表值
     */
    private List<Object> queryValues;

    /**
     * 操作类型 add/update/delete/select
     */
    private String operation;

    /**
     * 排序规则 desc asc
     */
    private String orderByClause;


    /**
     * 排序字段
     */
    private String[] sort;




}
