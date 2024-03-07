package com.ctsi.ssdc.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctsi.ssdc.admin.domain.CscpDeptUser;
import com.ctsi.ssdc.admin.domain.dto.CscpDeptUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author Len
 * @Date 2023/7/11 9:50
 */
@Mapper
public interface CscpDeptUserMapper extends BaseMapper<CscpDeptUser> {

    List<CscpDeptUserDTO> findAll(@Param("deptIds") List<Long> deptIds);

}
