package com.ctsi.ssdc.dic.service;

import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import com.ctsi.ssdc.dic.domain.CscpHxDicCustom;
import com.ctsi.ssdc.dic.domain.CscpHxDicCustomExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

/**
 * Service Interface for managing CscpHxDicCustom.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpHxDicCustomService 
	extends StrengthenBaseService<CscpHxDicCustom, Long, CscpHxDicCustomExample>{


    /**
    * GET  /cscpHxDicCustoms : get the cscpHxDicCustoms firstStringBaseColumn.
    */
    PageResult<CscpHxDicCustom> findFirstStringColumn(String dicName ,Pageable pageable);

    /**
     * 新增
     * @param cscpHxDicCustom
     * @return
     */
    @Override
    CscpHxDicCustom insert(CscpHxDicCustom cscpHxDicCustom);

    /**
     * 修改
     * @param cscpHxDicCustom
     * @return
     */
    @Override
    CscpHxDicCustom update(CscpHxDicCustom cscpHxDicCustom);

    /**
     * 删除自定义字典
     * @param aLong
     */
    @Override
    void delete(Long aLong);
}
