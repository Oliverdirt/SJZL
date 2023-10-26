package com.ctsi.flow.server.roleact;

import com.ctsi.flow.param.model.CscpRoleAct;
import com.ctsi.flow.param.model.CscpRoleActExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.List;

/**
 * Service Interface for managing CscpRoleAct.
 *
 * @author hx
 * @date 2022-07-26 15:53:56
 *
 */

public interface CscpRoleActService
	extends StrengthenBaseService<CscpRoleAct,Long , CscpRoleActExample>{

	/**
	* 批量删除
	* @param ids
	*/
	void deleteByIds(Long[] ids);

	void deleteById(Long id);

	List<CscpRoleAct> findByProcDefId(String id);
	void deleteByProcDefId(String id);
	/**
	 * 批量插入
	 *
	 * */
	int insertList(List<CscpRoleAct> record);
	List<CscpRoleAct> findByRoleIds(List<Long> list,Integer page,Integer size);

}
