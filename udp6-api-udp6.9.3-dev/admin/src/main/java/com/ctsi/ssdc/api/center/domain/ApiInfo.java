package com.ctsi.ssdc.api.center.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.api.center.domain.dto.BaseDTO;
import lombok.Data;

/**
 * @Description API信息
 * @Author Len
 * @Date 2023/6/5 15:44
 */
@Data
@TableName(value = "API_INFO")
public class ApiInfo extends BaseDTO {

    @AutoId(primaryKey = "id")
    private Long id;

    /**
     * API名称
     */
    @TableField(value = "API_NAME")
    private String apiName;

    /**
     * 所属系统
     */
    @TableField(value = "SYSTEM_ID")
    private Long systemId;

    /**
     * 状态 0：启用；1：停用
     */
    @TableField(value = "API_STATUS")
    private Integer apiStatus;

    /**
     * 所属单位
     */
    @TableField(value = "DEPT_ID")
    private Long deptId;

    /**
     * 所属分组
     */
    @TableField(value = "GROUP_ID")
    private Long groupId;

    /**
     * 协议 HTTP/HTTPS
     */
    @TableField(value = "PROTOCOL")
    private String protocol;

    /**
     * 请求方式 GET/POST/DELETE/PUT
     */
    @TableField(value = "REQUEST_METHOD")
    private String requestMethod;

    /**
     * 接口类型
     */
    @TableField(value = "API_TYPE")
    private Integer apiType;

    /**
     * 接口类型值
     */
    @TableField(exist = false)
    private String apiTypeValue;

    /**
     * API地址
     */
    @TableField(value = "API_ADDRESS")
    private String apiAddress;

    /**
     * 描述
     */
    @TableField(value = "API_DESC")
    private String apiDesc;

    /**
     * 请求参数
     */
    @TableField(value = "REQUEST_PARAMS")
    private String requestParams;

    @TableField(exist = false)
    private String requestParam;

    /**
     * 响应参数
     */
    @TableField(value = "RESPONSE_PARAMS")
    private String responseParams;

    /**
     * 状态码列表
     */
    @TableField(value = "RESPONSE_CODE")
    private String responseCode;

    /**
     * 可用性校验 0：已确认；1：待确认
     */
    @TableField(value = "CONFIRM")
    private Integer confirm;

    /**
     * API来源：0：手动添加；1：自动获取；
     */
    @TableField(value = "SOURCE_TYPE")
    private Integer sourceType;
    /**
     * API来源：0：手动添加；1：自动获取；
     */
    @TableField(exist = false)
    private String sourceTypeValue;

}
