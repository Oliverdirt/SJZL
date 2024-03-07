package com.ctsi.ssdc.base.system.domain.vo;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * 系统框架模块 VO
 */
@Data
public class FrameInfoVo {
    /**
     * 路由id
     */
    private Long id;

    /**
     * 系统标题
     */
    private String title;

    /**
     * 系统标题
     */
    private String name;

    /**
     * LOGO_URL图标文件id
     */
    private String logoUrl;

    /**
     * 登录背景图片文件id
     */
    private String backgroundUrl;

    /**
     * 框架类型（0：平台；1：子系统）
     */
    private Integer frameType;
    /**
     * LOGO_URL图标文件id
     */
    private BigInteger logoUrlId;

    /**
     * 登录背景图片文件id
     */
    private BigInteger backgroundUrlId;

    private List<BigInteger> depts;

    private BigInteger systemId;

}
