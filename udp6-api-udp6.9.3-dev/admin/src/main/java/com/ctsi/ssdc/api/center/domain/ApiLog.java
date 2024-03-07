package com.ctsi.ssdc.api.center.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctsi.ssdc.api.center.domain.dto.BaseDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description API调用日志
 * @Author Len
 * @Date 2023/6/5 16:02
 */
@TableName(value = "API_LOG")
@Data
public class ApiLog extends BaseDTO {

    @TableField(value = "ID")
    private Long id;

    /**
     * 所属系统
     */
    @TableField(value = "SYSTEM_ID")
    private Long systemId;

    /**
     * 内部流水号
     */
    @TableField(value = "INSIDE_NO")
    private String insideNo;

    /**
     * API名称
     */
    @TableField(value = "API_NAME")
    private String apiName;

    /**
     * API方法名称
     */
    @TableField(value = "API_METHOD")
    private String apiMethod;

    /**
     * 调用流程
     */
    @TableField(value = "PROCESS")
    private String process;

    /**
     * 调用IP
     */
    @TableField(value = "IP_ADDRESS")
    private String ipAddress;

    /**
     * 外部流水号
     */
    @TableField(value = "OUTSIDE_NO")
    private String outsideNo;

    /**
     * 调用时间
     */
    @TableField(value = "REQUEST_TIME")
    private LocalDateTime requestTime;

    /**
     * 请求参数
     */
    @TableField(value = "REQUEST_PARAMS")
    private String requestParams;

    /**
     * 返回结果
     */
    @TableField(value = "RESPONSE_RESULT")
    private String responseResult;

    /**
     * 返回编码
     */
    @TableField(value = "RESPONSE_CODE")
    private String responseCode;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private String startTime;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private String endTime;

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
     * API地址
     */
    @TableField(value = "API_ADDRESS")
    private String apiAddress;
    /**
     * 响应时间
     */
    @TableField(value = "RESPONSE_TIME")
    private String responseTime;

    /**
     * 执行时间
     */
    @TableField(value = "EXECUTE_TIME")
    private Long executeTime;

    /**
     * 严重性等级值
     */
    @TableField(exist = false)
    private String severityValue;
    /**
     * 严重性等级
     */
    @TableField(exist = false)
    private Integer severity;

}
