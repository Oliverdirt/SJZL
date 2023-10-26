package com.ctsi.flow.param.model;

import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;

public class Act implements Serializable {

    /**
     * 1 与角色绑定  2 与用户绑定
     * */
    private String type;
    /**
     * 绑定id
     *
     * */
    private List<Long> relation;
    /**
     * 流程模板id
     * */
    @JsonSerialize(using = LongtoStringSerialize.class)
    private String procDefId;
    /**
     * 流程模板名称
     *
     * */
    private String procDefName;
    /**
     * 表单id
     * */
    private Long formId;

    /**
     * 流程id
     *
     * */
    private String processId;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcDefName() {
        return procDefName;
    }

    public void setProcDefName(String procDefName) {
        this.procDefName = procDefName;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Long> getRelation() {
        return relation;
    }

    public void setRelation(List<Long> relation) {
        this.relation = relation;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }
}
