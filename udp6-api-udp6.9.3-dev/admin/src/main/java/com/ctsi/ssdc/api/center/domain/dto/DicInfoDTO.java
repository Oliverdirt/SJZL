package com.ctsi.ssdc.api.center.domain.dto;

import lombok.Data;

/**
 * @Description
 * @Author Len
 * @Date 2023/6/6 16:21
 */
@Data
public class DicInfoDTO {

    private Long id;

    private String dicValue;

    private String dicCode;

    private String dicType;

    private String comments;

}
