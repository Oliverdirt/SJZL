package com.ctsi.ssdc.base.commonfunction.domain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctsi.ssdc.annocation.AutoId;
import lombok.Data;

import java.io.Serializable;
import java.sql.Clob;
import java.time.LocalDateTime;

@Data
@TableName("COMMON_FUNCTION_INFO")
public class CommonFunction implements Serializable {

    /**
     * 数据库中表格主键ID
     */
    @AutoId(primaryKey = "id")
    private Long id;

    /**
     * 功能名称
     */
    @TableField(value = "TITLE")
    private String title;

    /**
     * 路径名称
     */
    @TableField(value = "NAME")
    private String name;

    /**
     * 模块ID
     */
    @TableField(value = "MODEL_ID")
    private Long modelId ;

    /**
     * 模块名称
     */
    @TableField(exist = false)
    private String modelName;


    /**
     * 系统ID
     */
    @TableField(value="SYSTEM_ID")
    private Long systemId;

    /**
     * 系统名称
     */
    @TableField(exist = false)
    private String systemName;

    /**
     * 背景颜色
     */
    @TableField(value="BG_COLOR")
    private String bgColor;

    /**
     *多个文件ID
     */
    @TableField(value="FILE_IDS")
    private String fileIds;

    /**
     * 创建ID
     */
    @TableField(value="CREATE_ID")
    private Long createId;

    /**
     * 创建时间
     */
    @TableField(value="CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新ID
     */
    @TableField(value="UPDATE_ID")
    private Long updateId;

    /**
     * 更新时间
     */
    @TableField(value="UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 删除
     */
    @TableField(value="DEL_FLAG")
    private Integer delFlag;

    /**
     * 图片路径
     */
    @TableField(value="IMG_PATH")
    private Clob imgPath;

    /**
     * 菜单ID
     */
    @TableField(value="MENU_ID")
    private String menuId;

    /**
     * 状态
     */
    @TableField(value="STATUS")
    private Integer status;

    /**
     * 关联菜单
     */
    @TableField(value="MENU_NAME")
    private String menuName;
}
