package com.ctsi.ssdc.customize.service;

import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.customize.domain.CscpCustomizeModelType;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModelTypeExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

/**
 * Service Interface for managing CscpCustomizeModelType.
 *
 * @author hx
 * @date 2022-08-29 16:33:42
 *
 */

public interface CscpCustomizeModelTypeService
	extends StrengthenBaseService<CscpCustomizeModelType,Long , CscpCustomizeModelTypeExample>{

	/**
	* 批量删除
	* @param modelTypeIds
	*/
	void deleteByIds(Long[] modelTypeIds);

	void deleteById(Long modelTypeId);

}
