package com.ctsi.monitor.service;

import com.ctsi.monitor.domain.MonitorJvmExample;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.monitor.domain.MonitorCpu;
import com.ctsi.monitor.domain.MonitorMem;
import org.springframework.data.domain.Pageable;

import com.ctsi.monitor.domain.MonitorJvm;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.Date;
import java.util.List;

/**
 * Service Interface for managing MonitorJvm.
 *
 * @author hx
 * @date 2022-05-27 11:49:49
 *
 */

public interface MonitorJvmService
	extends StrengthenBaseService<MonitorJvm,Long , MonitorJvmExample>{

	/**
	* 批量删除
	* @param ids
	*/
	void deleteByIds(Long[] ids);

	void deleteById(Long id);

	List<MonitorJvm> selectByTime(Date startTime, Date endTime);

	List<MonitorJvm> getMsgUp10();

	void truncateJvm();

}
