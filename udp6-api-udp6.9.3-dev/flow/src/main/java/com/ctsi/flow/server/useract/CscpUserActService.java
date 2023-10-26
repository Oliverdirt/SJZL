package com.ctsi.flow.server.useract;

import com.ctsi.flow.param.model.CscpFlowProcessDefExt;
import com.ctsi.flow.param.model.CscpUserAct;
import com.ctsi.flow.param.model.CscpUserActExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Interface for managing CscpUserAct.
 *
 * @author hx
 * @date 2022-07-26 15:55:12
 *
 */

public interface CscpUserActService
	extends StrengthenBaseService<CscpUserAct,Long , CscpUserActExample>{

	/**
	* 批量删除
	* @param ids
	*/
	void deleteByIds(Long[] ids);

	void deleteById(Long id);

	List<CscpUserAct> findByProcDefId(String id);
	void deleteByProcDefId(String id);
	int insertList(List<CscpUserAct> record);
	List<CscpUserAct> findByUserId(Long id);
	int updateByProDefId(String proDefId, String isCollect, Long userId, ZonedDateTime collectTime);
	List<CscpUserAct> findMyCollect(Long id);
	CscpUserAct selectByUserIdAndProDefId(Long userId,String proDefId);
	List<CscpFlowProcessDefExt> selectByModelId(String modelId);
	List<String> listAllDeployIdByProcessDefinitionId(String modelId);
}
