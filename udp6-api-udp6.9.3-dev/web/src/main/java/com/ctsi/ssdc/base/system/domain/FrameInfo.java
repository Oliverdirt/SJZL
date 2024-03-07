package com.ctsi.ssdc.base.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctsi.ssdc.annocation.AutoId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author lxs
 * @since 2023-04-21
 */
@TableName("FRAME_INFO")
@Data
public class FrameInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 路由id
     */
    @AutoId(primaryKey = "id")
    private Long id;

    /**
     * 系统标题
     */
    @TableField("TITLE")
    private String title;

    /**
     * 平台名称
     */
    @TableField("NAME")
    private String name;
    /**
     * LOGO_URL图标文件id
     */
    @TableField("LOGO_URL")
    private BigInteger logoUrlId;

    /**
     * 登录背景图片文件id
     */
    @TableField("BACKGROUND_URL")
    private BigInteger backgroundUrlId;

    /**
     * 框架类型（0：平台；1：子系统）
     */
    @TableField("FRAME_TYPE")
    private Integer frameType;

    /**
     * 创建者
     */
    @TableField("CREATE_ID")
    private BigInteger createId;
    /**
     * 创建者
     */
    @TableField("SYSTEM_ID")
    private BigInteger systemId;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改者
     */
    @TableField("UPDATE_ID")
    private BigInteger updateId;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 删除标记：0：有效；1：无效
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;
}
