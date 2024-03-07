package com.ctsi.ssdc.api.center.mapper;

import com.ctsi.ssdc.api.center.domain.dto.DeptDTO;
import com.ctsi.ssdc.api.center.domain.dto.DicInfoDTO;
import com.ctsi.ssdc.api.center.domain.dto.SystemDictDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author Len
 * @Date 2023/6/6 15:07
 */
@Mapper
public interface SystemDao {

    List<SystemDictDTO> getSystemDictInfo();

    List<DeptDTO> getDeptInfo();

    List<DicInfoDTO> getDicInfoByDicType(@Param("dicType") String dicType);
}
