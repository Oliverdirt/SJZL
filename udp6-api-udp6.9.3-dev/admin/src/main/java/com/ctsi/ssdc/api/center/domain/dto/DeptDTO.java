package com.ctsi.ssdc.api.center.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 部门信息
 * @Author Len
 * @Date 2023/6/6 15:42
 */
@Data
public class DeptDTO {

    private Long id;

    /**
     * 上级id
     */
    private Long parentId;

    /**
     *
     */
    private String deptName;

    private List<DeptDTO> children = new ArrayList<>();

}
