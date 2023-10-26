package com.ctsi.ssdc.sensitiveword.service;

import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordType;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordTypeExample;
import com.ctsi.ssdc.service.StrengthenBaseService;
import java.util.List;

/**
 * Service Interface for managing CscpSensitiveWordType.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpSensitiveWordTypeService 
	extends StrengthenBaseService<CscpSensitiveWordType, Long, CscpSensitiveWordTypeExample>{

    AjaxResult deleteByIds(Long[] ids);

    AjaxResult deleteBysenTypeId(Long senTypeId);

    List<CscpSensitiveWordType> getSensitiveWordTypesListNonempty();
}
