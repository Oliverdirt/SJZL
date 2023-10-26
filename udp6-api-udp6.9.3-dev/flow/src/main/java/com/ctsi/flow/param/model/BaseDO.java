package com.ctsi.flow.param.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-12 16:53
 * @description ：
 */

@Data
public abstract class BaseDO implements Serializable {

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后更新时间
     */
    private Date updateTime;
    /**
     * 创建者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String creator;
    /**
     * 更新者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String updater;
    /**
     * 是否删除
     */
    private Boolean deleted;

}
