package com.ctsi.ssdc.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctsi.ssdc.admin.domain.CscpDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author Len
 * @Date 2023/4/23 14:23
 */
@Mapper
public interface DeptMapper extends BaseMapper<CscpDept> {

    /**
     * 查询非撤销机构
     * @param currentUserId
     * @return
     */
    List<CscpDept> selectDeptInfoList(@Param("currentUserId") Long currentUserId);

    /**
     * 获取子机构id
     * @param parentId
     * @return
     */
    List<Long> selectDeptByParentId(@Param("parentId") Long parentId);
}
