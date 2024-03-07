package com.ctsi.ssdc.api.center.service;

import com.ctsi.ssdc.api.center.domain.dto.DeptDTO;
import com.ctsi.ssdc.api.center.domain.dto.DicInfoDTO;
import com.ctsi.ssdc.api.center.domain.dto.SystemDictDTO;

import java.util.List;

/**
 * @Description
 * @Author Len
 * @Date 2023/6/6 15:06
 */
public interface SystemService {

    List<SystemDictDTO> getSystemDictInfo();

    List<DeptDTO> getDeptInfo();

    List<DicInfoDTO> getDicInfoByDicType(String dicType);
}
