package com.ctsi.ssdc.api.center.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description
 * @Author Len
 * @Date 2023/6/5 15:45
 */
@Data
public class BaseDTO {

    /**
     * 创建者
     */
    @TableField(value = "CREATE_ID")
    private Long createId;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 修改者
     */
    @TableField(value = "UPDATE_ID")
    private Long updateId;

    /**
     * 修改时间
     */
    @TableField(value = "UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 删除标记：0：有效；1：无效
     */
    @TableField(value = "DEL_FLAG")
    private Integer delFlag;

}
