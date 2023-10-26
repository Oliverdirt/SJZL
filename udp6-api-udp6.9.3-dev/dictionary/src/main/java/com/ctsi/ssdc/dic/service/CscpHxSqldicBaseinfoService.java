package com.ctsi.ssdc.dic.service;



import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.dic.domain.CscpHxSqldicBaseinfo;
import com.ctsi.ssdc.dic.domain.CscpHxSqldicBaseinfoExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

/**
 * Service Interface for managing CscpHxSqldicBaseinfo.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpHxSqldicBaseinfoService 
	extends StrengthenBaseService<CscpHxSqldicBaseinfo, Long, CscpHxSqldicBaseinfoExample>{


    /**
    * GET  /cscpHxSqldicBaseinfos : get the cscpHxSqldicBaseinfos firstStringBaseColumn.
    */
    PageResult<CscpHxSqldicBaseinfo> findFirstStringColumn(String dicSelectSql ,Pageable pageable);

}
