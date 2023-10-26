package com.ctsi.ssdc.quartz.service;

import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.quartz.domain.CscpJobLog;
import com.ctsi.ssdc.quartz.domain.CscpJobLogExample;
import org.springframework.data.domain.Pageable;


import com.ctsi.ssdc.service.StrengthenBaseService;

/**
 * Service Interface for managing CscpJobLog.
 *
 * @author hx
 * @date 2022-05-30 17:05:20
 *
 */

public interface CscpJobLogService
	extends StrengthenBaseService<CscpJobLog,Long , CscpJobLogExample>{

	/**
	* 批量删除
	* @param jobLogIds
	*/
	void deleteByIds(Long[] jobLogIds);

	void deleteById(Long jobLogId);

}
