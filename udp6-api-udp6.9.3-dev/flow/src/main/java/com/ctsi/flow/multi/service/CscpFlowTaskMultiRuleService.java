package com.ctsi.flow.multi.service;

import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRule;
import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRuleExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

/**
 * Service Interface for managing CscpFlowTaskMultiRule.
 *
 * @author hx
 * @date 2022-10-24 16:49:41
 *
 */

public interface CscpFlowTaskMultiRuleService
	extends StrengthenBaseService<CscpFlowTaskMultiRule,Long , CscpFlowTaskMultiRuleExample>{

	/**
	* 批量删除
	* @param ids
	*/
	void deleteByIds(Long[] ids);

	void deleteById(Long id);

    void deleteMultiByKey(String taskKey);

	CscpFlowTaskMultiRule selectMultiByKey(String taskKey);

	CscpFlowTaskMultiRule updateMulti(CscpFlowTaskMultiRule cscpFlowTaskMultiRule);
}
