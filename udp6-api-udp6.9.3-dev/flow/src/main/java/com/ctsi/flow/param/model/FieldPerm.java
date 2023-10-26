package com.ctsi.flow.param.model;

import java.util.Objects;

/**
 * @Description:
 * @Author: sunsheng
 **/
public class FieldPerm {
    String fieldId;
    String editFlag;
    String showFlag;

    public FieldPerm(String fieldId, String editFlag, String showFlag) {
        this.fieldId = fieldId;
        this.editFlag = editFlag;
        this.showFlag = showFlag;
    }

    public FieldPerm() {
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FieldPerm fieldPerm = (FieldPerm) o;
        return Objects.equals(fieldId, fieldPerm.fieldId) && Objects.equals(editFlag, fieldPerm.editFlag) && Objects.equals(showFlag, fieldPerm.showFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldId, editFlag, showFlag);
    }
}
