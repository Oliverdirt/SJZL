package com.ctsi.ssdc.gen.service;



import com.ctsi.ssdc.model.PageResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.gen.domain.CscpHxFormColumn;
import com.ctsi.ssdc.gen.domain.CscpHxFormColumnExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.List;

/**
 * Service Interface for managing CscpHxFormColumn.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpHxFormColumnService 
	extends StrengthenBaseService<CscpHxFormColumn, Long, CscpHxFormColumnExample>{


    /**
    * GET  /cscpHxFormColumns : get the cscpHxFormColumns firstStringBaseColumn.
    */
    PageResult<CscpHxFormColumn> findFirstStringColumn(String columnName ,Pageable pageable);


    /**
     * 根据表单id删除字段
     * @param id
     * @return
     */
    Integer deleteByTable(Long id);


    /**
     * 根据表单id查找表单字段列表
     * @param id
     * @return
     */
    List<CscpHxFormColumn> getFieldListByTable(Long id);

    /**
     * 根据表单id批量删除column
     * @param tableIds
     */
    void deleteByTableIds(List<Long> tableIds);

}
