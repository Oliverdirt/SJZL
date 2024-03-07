package com.ctsi.ssdc.base.commonfunction.domain.vo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Clob;
import java.time.LocalDateTime;

@Data
@TableName("COMMON_FUNCTION_INFO")
public class CommonFunctionVo{
    /**
     * 数据库中表格主键ID
     */
    private Long id;

    /**
     * 功能名称
     */
    private String title;

    /**
     * 路径名称
     */
    private String name;

    /**
     * 模块ID
     */
    private Long modelId ;

//    /**
//     * 模块名称
//     */
//    private String modelName;


    /**
     * 系统ID
     */
    private Long systemId;

//    /**
//     * 系统名称
//     */
//    private String systemName;

    /**
     * 背景颜色
     */
    private String bgColor;

    /**
     *多个文件ID
     */
    private String fileIds;

    /**
     * 创建ID
     */
    private Long createId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新ID
     */
    private Long updateId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除
     */
    private Integer delFlag;

    /**
     * 图片路径
     */
    private Clob imgPath;

    /**
     * 菜单ID
     */
    private String menuId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 关联菜单
     */
    private String menuName;
}
