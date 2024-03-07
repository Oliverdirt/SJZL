package com.ctsi.ssdc.admin.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.process.CurrentTimeMillisAutoIdProcess;
import lombok.Data;

import java.util.Date;

/**
 * @Description 机构人员
 * @Author Len
 * @Date 2023/4/25 9:48
 */
@Data
public class CscpDeptUser {

    @AutoId(primaryKey = "id", type = CurrentTimeMillisAutoIdProcess.class)
    private Long id;

    /**
     * 关联账号id
     */
    @TableField("USER_ID")
    private Long userId;


    /**
     * 真实姓名
     */
    @TableField("NAME")
    private String name;

    /**
     * 手机号
     */
    @TableField("MOBILE")
    private String mobile;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 证件类型
     */
    @TableField("CARD_TYPE")
    private Integer cardType;

    /**
     * 证件号码
     */
    @TableField("ID_CARD")
    private String idCard;

    /**
     * 性别：0：男；1：女
     */
    @TableField("SEX")
    private Integer sex;

    /**
     * 年龄
     */
    @TableField("AGE")
    private Integer age;

    /**
     * 政治面貌
     */
    @TableField("POLITICAL")
    private Integer political;

    /**
     * 民族
     */
    @TableField("NATION")
    private Integer nation;

    /**
     * 在职状态
     */
    @TableField("JOB_STATUS")
    private Integer jobStatus;

    /**
     * 职级
     */
    @TableField("JOB_LEVEL")
    private Integer jobLevel;

    /**
     * 所属单位，多个时，逗号分割
     */
    @TableField("DEPT_ID")
    private String deptId;

    @TableField("pre_id")
    private Double preId;

    /**
     * 出生日期
     */
    @TableField("BIRTHDAY")
    private String birthday;

    /**
     * 删除标识 0正常 1已删除
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 创建者id
     */
    @TableField("CREATE_ID")
    private Long createId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改者id
     */
    @TableField("UPDATE_ID")
    private Long updateId;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 所属租户
     */
    @TableField("tenant_id")
    private Long tenantId;

}
