package com.ctsi.ssdc.gen.service;

import com.ctsi.ssdc.gen.domain.CscpCustomizeVfield;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVfieldExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.List;

/**
 * Service Interface for managing CscpCustomizeVfield.
 *
 * @author hx
 * @date 2022-05-23 09:59:35
 *
 */

public interface CscpCustomizeVfieldService
	extends StrengthenBaseService<CscpCustomizeVfield,Long , CscpCustomizeVfieldExample>{

	/**
	* 批量删除
	* @param fieldIds
	*/
	void deleteByIds(Long[] fieldIds);

	void deleteById(Long fieldId);

	void saveCscpCustomizeVfields(List<CscpCustomizeVfield> cscpCustomizeVfields);

	void updateCscpCustomizeVfields(List<CscpCustomizeVfield> cscpCustomizeVfields);

	List<CscpCustomizeVfield> getListByFormId(Long formId);

	List<CscpCustomizeVfield> getListByFormIds(Long[] formId);

}
