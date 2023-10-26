package com.ctsi.ssdc.gen.service;



import com.ctsi.ssdc.gen.domain.CscpHxFormColumn;
import com.ctsi.ssdc.model.PageResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.domain.CscpHxFormTableExample;
import com.ctsi.ssdc.service.StrengthenBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Interface for managing CscpHxFormTable.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpHxFormTableService 
	extends StrengthenBaseService<CscpHxFormTable, Long, CscpHxFormTableExample>{


    /**
    * GET  /cscpHxFormTables : get the cscpHxFormTables firstStringBaseColumn.
    */
    PageResult<CscpHxFormTable> findFirstStringColumn(String tableName ,Pageable pageable);

    @Override
    CscpHxFormTable insert(CscpHxFormTable cscpHxFormTable);

    @Override
    CscpHxFormTable update(CscpHxFormTable cscpHxFormTable);

    @Transactional(rollbackFor = Exception.class)
    CscpHxFormTable createTable(CscpHxFormTable cscpHxFormTable, List<CscpHxFormColumn> columns);

    @Override
    void delete(Long aLong);

    @Override
    PageResult<CscpHxFormTable> findAll();

    /**
     * 同步数据库
     * @param id
     * @return
     */
    CscpHxFormTable syncDatabase(Long id);

    CscpHxFormTable syncDatabaseWithModelDesign(Long id);

    /**
     * 根据example查询是否存在
     * @param cscpHxFormTableExample
     * @return
     */
    long countByExample(CscpHxFormTableExample cscpHxFormTableExample);

    /**
     * 查找一个表单的所有信息
     * @param tableId
     * @return
     */
    CscpHxFormTable findOneAll(Long tableId);


    /**
     * 批量删除
     * @param ids
     *
     */
    void deleteByIds(Long[] ids);

}
