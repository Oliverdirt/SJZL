package com.ctsi.ssdc.gen.service;

import com.ctsi.ssdc.gen.domain.CscpCustomizeVform;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVformExample;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.service.StrengthenBaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing CscpCustomizeVform.
 *
 * @author hx
 * @date 2022-05-23 09:59:33
 *
 */

public interface CscpCustomizeVformService
	extends StrengthenBaseService<CscpCustomizeVform,Long , CscpCustomizeVformExample>{

	/**
	* 批量删除
	* @param formIds
	*/
	void deleteByIds(Long[] formIds);

	AjaxResult deleteById(Long formId);

    CscpCustomizeVform queryCscpCustomizeVform(Long formId);

	PageResult<CscpCustomizeVform> getCscpCustomizeVformsPageList(CscpCustomizeVform form, Pageable pageable);

	List<CscpCustomizeVform> getCscpCustomizeVformsListAll();

	CscpCustomizeVform getCscpCustomizeVformsByFormName(String formName);

	List<Long> selectFormIdsByMainFormId(List<Long> mainFormIds);

	List<CscpCustomizeVform> queryChildCscpCustomizeVforms(Long[] mainFormId);

    CscpCustomizeVform getMainCscpCustomizeVform(Long formId);

	List<CscpCustomizeVform> selectByFormType(String formType);

	void updateDelFlagByFormId(Long formId);
}
