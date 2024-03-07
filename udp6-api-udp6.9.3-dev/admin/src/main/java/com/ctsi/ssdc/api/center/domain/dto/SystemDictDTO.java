package com.ctsi.ssdc.api.center.domain.dto;

import lombok.Data;

/**
 * @Description 系统信息
 * @Author Len
 * @Date 2023/6/6 15:09
 */
@Data
public class SystemDictDTO {

    private Long id;

    /**
     * 系统名称
     */
    private String name;

    private String code;

}
