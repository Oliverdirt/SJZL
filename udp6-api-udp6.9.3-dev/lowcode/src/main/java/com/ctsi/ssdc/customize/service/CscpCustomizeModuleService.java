package com.ctsi.ssdc.customize.service;

import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.customize.domain.CscpCustomizeModule;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModuleExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.Map;

/**
 * Service Interface for managing CscpCustomizeModule.
 *
 * @author hx
 * @date 2022-08-29 16:34:09
 *
 */

public interface CscpCustomizeModuleService
	extends StrengthenBaseService<CscpCustomizeModule,Long , CscpCustomizeModuleExample>{

	/**
	* 批量删除
	* @param moduleIds
	*/
	void deleteByIds(Long[] moduleIds);

	void deleteById(Long moduleId);

	void deleteAll(Long[] moduleId);
	void deleteOne(Long moduleId);

	void updateByModuleId(Map<String,Object> map);
}
