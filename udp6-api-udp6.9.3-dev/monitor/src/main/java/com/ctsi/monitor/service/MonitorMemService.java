package com.ctsi.monitor.service;

import com.ctsi.ssdc.model.PageResult;
import com.ctsi.monitor.domain.MonitorCpu;
import com.ctsi.monitor.domain.MonitorJvm;
import org.springframework.data.domain.Pageable;

import com.ctsi.monitor.domain.MonitorMem;
import com.ctsi.monitor.domain.MonitorMemExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.Date;
import java.util.List;

/**
 * Service Interface for managing MonitorMem.
 *
 * @author hx
 * @date 2022-05-27 14:01:32
 *
 */

public interface MonitorMemService
	extends StrengthenBaseService<MonitorMem,Long , MonitorMemExample>{

	/**
	* 批量删除
	* @param ids
	*/
	void deleteByIds(Long[] ids);

	void deleteById(Long id);

	List<MonitorMem> selectByTime(Date startTime, Date endTime);

	List<MonitorMem> getMsgUp10();

	void truncateMem();

}
