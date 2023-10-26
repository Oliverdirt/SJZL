package com.ctsi.ssdc.dic.service;

import com.ctsi.ssdc.dic.domain.CscpHxDic;
import com.ctsi.ssdc.dic.domain.CscpHxDicExample;
import com.ctsi.ssdc.service.StrengthenBaseService;


/**
 * Service Interface for managing CscpHxDic.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpHxDicService
	extends StrengthenBaseService<CscpHxDic, Long, CscpHxDicExample>{
	/**
	 * 批量删除
	 * @param dicIds
	 */
	void deleteByIds(Long[] dicIds);
	/**
	 * 删除
	 * @param dicId
	 */
	void deleteById(Long dicId);

	/**
	 *
	 * @param cscpHxDic
	 * @return
	 */
	int checkCscpHxDicName(CscpHxDic cscpHxDic);
	/**
	 *
	 * @param cscpHxDic
	 * @return
	 */
	int checkCscpHxDicCode(CscpHxDic cscpHxDic);
}
