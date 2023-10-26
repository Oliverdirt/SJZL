package com.ctsi.flow.approve.service;

import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

import com.ctsi.flow.approve.domain.Approve;
import com.ctsi.flow.approve.domain.ApproveExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

/**
 * Service Interface for managing Approve.
 *
 * @author hx
 * @date 2022-11-03 19:50:16
 *
 */

public interface ApproveService
	extends StrengthenBaseService<Approve,Long , ApproveExample>{

	/**
	* 批量删除
	* @param ids
	*/
	void deleteByIds(Long[] ids);

	void deleteById(Long id);

}
