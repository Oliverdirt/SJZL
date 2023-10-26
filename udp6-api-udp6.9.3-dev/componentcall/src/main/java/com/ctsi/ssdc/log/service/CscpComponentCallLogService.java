package com.ctsi.ssdc.log.service;


import com.ctsi.ssdc.log.domain.CscpComponentCallLog;
import com.ctsi.ssdc.log.domain.CscpComponentCallLogExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

/**
 * Service Interface for managing CscpComponentCallLog.
 *
 * @author hx
 * @date 2022-03-15 21:35:20
 *
 */

public interface CscpComponentCallLogService
	extends StrengthenBaseService<CscpComponentCallLog,Long , CscpComponentCallLogExample>{

	/**
	* 批量删除
	* @param ids
	*/
	void deleteByIds(Long[] ids);

	void deleteById(Long id);

}
