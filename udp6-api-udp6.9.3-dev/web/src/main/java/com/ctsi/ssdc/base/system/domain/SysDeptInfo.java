package com.ctsi.ssdc.base.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctsi.ssdc.annocation.AutoId;
import lombok.Data;

import java.math.BigInteger;

/**
 * <p>
 *
 * </p>
 *
 * @author lxs
 * @since 2023-04-21
 */
@TableName("SYS_DEPT_INFO")
@Data
public class SysDeptInfo {


    /**
     * id
     */
    @AutoId(primaryKey = "id")
    private Long id;

    /**
     * 创建者
     */
    @TableField("SYSTEM_ID")
    private Long systemId;
    /**
     * 创建者
     */
    @TableField("DEPT_ID")
    private Long deptId;


}
