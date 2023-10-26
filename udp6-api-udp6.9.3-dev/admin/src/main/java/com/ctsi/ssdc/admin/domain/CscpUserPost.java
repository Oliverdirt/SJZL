package com.ctsi.ssdc.admin.domain;

import java.io.Serializable;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import com.ctsi.ssdc.annocation.AutoId;
/**
 *  CscpUserPost 实体类
 *
 * @author hx
 * @date 2022-08-29 10:56:22
 *
 */

@ApiModel(description = "CscpUserPost")
public class CscpUserPost  implements Serializable{
    /**
    * 主键id
    *
    */
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long id;
    /**
    * 用户id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long userId;
    /**
    * 岗位id
    *
    */
    @JsonSerialize(using = LongtoStringSerialize.class)

    private Long postId;



    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }
    public void setUserId(Long userId){
        this.userId = userId;
    }
    public Long getUserId(){
        return userId;
    }
    public void setPostId(Long postId){
        this.postId = postId;
    }
    public Long getPostId(){
        return postId;
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
        CscpUserPost other=(CscpUserPost)that;
        return (this.getId() ==null?this.getId() !=null:this.getId().equals(other.getId()))
                && (this.getUserId() ==null?this.getUserId() !=null:this.getUserId().equals(other.getUserId()))
                && (this.getPostId() ==null?this.getPostId() !=null:this.getPostId().equals(other.getPostId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        return result;
    }
}
