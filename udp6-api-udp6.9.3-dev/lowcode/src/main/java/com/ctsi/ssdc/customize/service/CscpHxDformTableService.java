package com.ctsi.ssdc.customize.service;

import com.ctsi.ssdc.customize.domain.CscpHxDformColumn;
import com.ctsi.ssdc.customize.domain.CscpHxDformTable;
import com.ctsi.ssdc.customize.domain.CscpHxDformTableExample;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.service.StrengthenBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/11/14 11:14  by xx
 */

public interface CscpHxDformTableService extends StrengthenBaseService<CscpHxDformTable, Long, CscpHxDformTableExample> {
    /**
     * GET  /CscpHxDformTables : get the CscpHxDformTables firstStringBaseColumn.
     */
//    PageResult<CscpHxDformTable> findFirstStringColumn(String tableName , Pageable pageable);

    @Override
    CscpHxDformTable insert(CscpHxDformTable cscpHxDformTable);

    @Override
    CscpHxDformTable update(CscpHxDformTable cscpHxDformTable);

    @Transactional(rollbackFor = Exception.class)
    CscpHxDformTable createTable(CscpHxDformTable cscpHxDformTable, List<CscpHxDformColumn> columns);

    @Override
    void delete(Long aLong);

    @Override
    PageResult<CscpHxDformTable> findAll();

    /**
     * 同步数据库
     * @param id
     * @return
     */
    CscpHxDformTable syncDatabase(Long id);

    CscpHxDformTable syncDatabaseWithModelDesign(Long id);

    /**
     * 根据example查询是否存在
     * @param cscpHxDformTableExample
     * @return
     */
    long countByExample(CscpHxDformTableExample cscpHxDformTableExample);

    /**
     * 查找一个表单的所有信息
     * @param tableId
     * @return
     */
    CscpHxDformTable findOneAll(Long tableId);

    /**
     * 查找所有子表单的信息
     * @param mainTableId
     * @return
     */
    List<CscpHxDformTable> findsubAll(Long mainTableId);

    /**
     * 批量删除
     * @param ids
     *
     */
    void deleteByIds(Long[] ids);

}
