package com.ctsi.ssdc.base.system.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * @author
 */
@Data
public class FrameInfoBo {

    /**
     * id
     */
    private String id;

    /**
     * 系统标题
     */
    @NotNull(message = "系统标题不能为空")
    private String title;

    /**
     * 平台名称
     */
    @NotNull(message = "平台名称不能为空")
    private String name;

    /**
     * LOGO_URL图标文件id
     */
    @NotNull(message = "LOGO_URL图标文件id不能为空")
    private BigInteger logoUrlId;

    /**
     * 登录背景图片文件id
     */
    @NotNull(message = "登录背景图片文件id不能为空")
    private BigInteger backgroundUrlId;

    /**
     * 框架类型（0：平台；1：子系统）
     */
    @NotNull(message = "框架类型不能为空")
    private Integer frameType;

    @NotNull(message = "系统id")
    private Long systemId;

    private String deptId;

}
