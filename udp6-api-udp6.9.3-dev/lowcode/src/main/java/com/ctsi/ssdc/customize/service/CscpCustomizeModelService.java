package com.ctsi.ssdc.customize.service;

import com.ctsi.ssdc.model.PageResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.customize.domain.CscpCustomizeModel;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModelExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing CscpCustomizeModel.
 *
 * @author hx
 * @date 2022-08-31 09:40:19
 *
 */

public interface CscpCustomizeModelService
	extends StrengthenBaseService<CscpCustomizeModel,Long , CscpCustomizeModelExample>{

	/**
	* 批量删除
	* @param modelIds
	*/
	void deleteByIds(Long[] modelIds);

	void deleteById(Long modelId);

	List<CscpCustomizeModel> selectByModuleId(Long moduleId);

	List<CscpCustomizeModel> selectByModuleIds(List<Long> moduleIds);

	Map<String,Object> delAll(Long[] modelIds);
	Map<String,Object> delOne(Long modelIds);
}
