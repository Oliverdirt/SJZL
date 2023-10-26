package com.ctsi.monitor.service;

import com.ctsi.ssdc.model.PageResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.ctsi.monitor.domain.MonitorCpu;
import com.ctsi.monitor.domain.MonitorCpuExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.Date;
import java.util.List;

/**
 * Service Interface for managing MonitorCpu.
 *
 * @author hx
 * @date 2022-05-27 11:21:43
 *
 */

public interface MonitorCpuService
	extends StrengthenBaseService<MonitorCpu,Long , MonitorCpuExample>{

	/**
	* 批量删除
	* @param ids
	*/
	void deleteByIds(Long[] ids);

	void deleteById(Long id);

	List<MonitorCpu> selectByTime(Date startTime, Date endTime);

	List<MonitorCpu> getMsgUp10();

	void truncateCpu();

}
