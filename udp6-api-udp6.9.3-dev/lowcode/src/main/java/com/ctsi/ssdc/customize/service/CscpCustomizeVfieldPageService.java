package com.ctsi.ssdc.customize.service;

import com.ctsi.ssdc.customize.domain.CscpCustomizeVfieldPage;
import com.ctsi.ssdc.customize.domain.CscpCustomizeVfieldPageExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.List;

/**
 * Service Interface for managing CscpCustomizeVfield.
 *
 * @author hx
 * @date 2022-05-23 09:59:35
 *
 */

public interface CscpCustomizeVfieldPageService
	extends StrengthenBaseService<CscpCustomizeVfieldPage,Long , CscpCustomizeVfieldPageExample>{

	/**
	* 批量删除
	* @param fieldIds
	*/
	void deleteByIds(Long[] fieldIds);

	void deleteById(Long fieldId);

	void saveCscpCustomizeVfieldPages(List<CscpCustomizeVfieldPage> cscpCustomizeVfields);

	void updateCscpCustomizeVfieldPages(List<CscpCustomizeVfieldPage> cscpCustomizeVfields);

	List<CscpCustomizeVfieldPage> getListByPageId(Long pageId);

	List<CscpCustomizeVfieldPage> getListByPageIds(Long[] pageIds);

	void deleteByPageIdBatch(List<Long> pageIds);

}
