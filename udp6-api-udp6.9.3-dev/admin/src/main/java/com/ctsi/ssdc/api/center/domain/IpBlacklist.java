package com.ctsi.ssdc.api.center.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.api.center.domain.dto.BaseDTO;
import lombok.Data;

/**
 * @Description IP黑名单
 * @Author Len
 * @Date 2023/6/8 8:31
 */
@Data
@TableName(value = "IP_BLACKLIST")
public class IpBlacklist extends BaseDTO {

    @AutoId(primaryKey = "id")
    private Long id;

    /**
     * IP地址
     */
    @TableField(value = "IP_ADDRESS")
    private String ipAddress;

    /**
     * 描述
     */
    @TableField(value = "COMMENTS")
    private String comments;
}
