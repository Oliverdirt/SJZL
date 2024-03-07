package com.ctsi.ssdc.admin.domain.dto;

import lombok.Data;

/**
 * @Description 机构人员
 * @Author Len
 * @Date 2023/4/25 9:48
 */
@Data
public class CscpDeptUserDTO {

    private String id;

    /**
     * 关联账号id
     */
    private String userId;

    /**
     * 关联账号idValue
     */
    private String userIdValue;


    /**
     * 真实姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户名
     */
    private String username;

    /**
     * 证件类型
     */
    private String cardType;

    /**
     * 证件号码
     */
    private String idCard;

    /**
     * 性别：0：男；1：女
     */
    private String sex;

    /**
     * 年龄
     */
    private String age;

    /**
     * 政治面貌
     */
    private String political;

    /**
     * 民族
     */
    private String nation;

    /**
     * 在职状态
     */
    private String jobStatus;

    /**
     * 职级
     */
    private String jobLevel;

    /**
     * 所属单位
     */
    private String deptId;
    /**
     * 所属单位
     */
    private String deptName;

    private String preId;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 创建者id
     */
    private String createId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改者id
     */
    private String updateId;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 所属租户
     */
    private String tenantId;

}
