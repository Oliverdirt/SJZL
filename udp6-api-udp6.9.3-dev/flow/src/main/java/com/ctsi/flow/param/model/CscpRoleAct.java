package com.ctsi.flow.param.model;

import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 *  CscpRoleAct 实体类
 *
 * @author hx
 * @date 2022-07-26 15:53:56
 *
 */

@ApiModel(description = "CscpRoleAct")
public class CscpRoleAct  implements Serializable{
    /**
    * id
    *
    */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long id;
    /**
    * 角色id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long roleId;
    /**
    * 流程模板id
    *
    */

    private String procDefId;
    /**
    * 流程名称
    *
    */

    private String procDefName;
    /**
    * 表单id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long formId;
    /**
    * DefinedId
    *
    */

    private String definedId;

    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }
    public void setRoleId(Long roleId){
        this.roleId = roleId;
    }
    public Long getRoleId(){
        return roleId;
    }
    public void setProcDefId(String procDefId){
        this.procDefId = procDefId;
    }
    public String getProcDefId(){
        return procDefId;
    }
    public void setProcDefName(String procDefName){
        this.procDefName = procDefName;
    }
    public String getProcDefName(){
        return procDefName;
    }
    public void setFormId(Long formId){
        this.formId = formId;
    }
    public Long getFormId(){
        return formId;
    }
    public void setDefinedId(String definedId){
        this.definedId = definedId;
    }
    public String getDefinedId(){
        return definedId;
    }



    @Override
    public boolean equals(Object that){
        if(this==that){
            return true;
        }
        if(that==null){
            return false;
        }
        if(getClass()!=that.getClass()){
            return false;
        }
        CscpRoleAct other=(CscpRoleAct)that;
        return (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getRoleId() ==null?this.getRoleId() !=null:this.getRoleId().equals(other.getRoleId()))
                && (this.getProcDefId() ==null?this.getProcDefId() !=null:this.getProcDefId().equals(other.getProcDefId()))
                && (this.getProcDefName() ==null?this.getProcDefName() !=null:this.getProcDefName().equals(other.getProcDefName()))
                && (this.getFormId() ==null?this.getFormId() !=null:this.getFormId().equals(other.getFormId()))
                && (this.getDefinedId() ==null?this.getDefinedId() !=null:this.getDefinedId().equals(other.getDefinedId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        result = prime * result + ((getProcDefId() == null) ? 0 : getProcDefId().hashCode());
        result = prime * result + ((getProcDefName() == null) ? 0 : getProcDefName().hashCode());
        result = prime * result + ((getFormId() == null) ? 0 : getFormId().hashCode());
        result = prime * result + ((getDefinedId() == null) ? 0 : getDefinedId().hashCode());
        return result;
    }
}
