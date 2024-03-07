package com.ctsi.ssdc.base.system.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 框架 - 查询 BO
 * @author
 */
@Data
public class FrameInfoUpdateBo {

    /**
     * 框架类型（0：平台；1：子系统）
     */
    @NotNull(message = "框架类型 不能为空")
    private Integer frameType;
}
