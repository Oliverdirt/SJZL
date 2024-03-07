package com.ctsi.ssdc.admin.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * SystemDic 实体类
 *
 * @author hx
 * @date 2023-05-18 15:00:17
 */
@Data
public class SystemDict {


    /**
     * ID
     */
    @TableField(value = "ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "NAME")
    private String name;

    /**
     * 系统编码
     */
    @TableField(value = "CODE")
    private String code;

    /**
     * 待办事项URL
     */
    @TableField(value = "TO_DO_URL")
    private String toDoUrl;

    /**
     * 我的申请URL
     */
    @TableField(value = "APPLICATION_URL")
    private String applicationUrl;

    /**
     * 消息提醒URL
     */
    @TableField(value = "MESSAGE_REMIND_URL")
    private String messageRemindUrl;

    /**
     * 通知URL
     */
    @TableField(value = "NOTICE_URL")
    private String noticeUrl;

    /**
     * JsonStr
     */
    @TableField(value = "JSON_STR")
    private String jsonStr;


}
