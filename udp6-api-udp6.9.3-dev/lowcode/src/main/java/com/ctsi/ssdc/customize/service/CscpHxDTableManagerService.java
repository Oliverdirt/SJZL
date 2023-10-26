package com.ctsi.ssdc.customize.service;

import com.ctsi.ssdc.customize.domain.CscpHxDformColumn;
import com.ctsi.ssdc.customize.domain.CscpHxDformTable;

import java.util.List;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/11/14 15:03  by xx
 */
public interface CscpHxDTableManagerService {
    /**
     * 同步数据库，包括新建表和更新表，但不包括删除表操作
     * 注意：表的主键一旦确定，不能再更改
     */
    void syncDatabase(CscpHxDformTable cscpHxFormTable, List<CscpHxDformColumn> cscpHxFormColumns);


    boolean isTableExists(CscpHxDformTable cscpHxDformTable);

    void updateTable(CscpHxDformTable cscpHxDformTable, List<CscpHxDformColumn> cscpHxDformColumns);

    void createTableIfNotExists(CscpHxDformTable cscpHxDformTable, List<CscpHxDformColumn> cscpHxDformColumns);

    void deleteTableIfExists(CscpHxDformTable cscpHxFormTable);
}
