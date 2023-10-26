package com.ctsi.ssdc.customize.service.impl;

import com.ctsi.ssdc.customize.domain.CscpHxDformColumn;
import com.ctsi.ssdc.customize.domain.CscpHxDformTable;
import com.ctsi.ssdc.customize.service.CscpHxDInfoSchemaMysqlService;
import com.ctsi.ssdc.customize.service.CscpHxDTableManagerService;
import com.ctsi.ssdc.customize.util.MySqlDTableUtil;
import com.ctsi.ssdc.gen.domain.CscpHxInfoSchemaMysql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/11/14 15:04  by xx
 */
@Service
public class CscpHxDTableManagerServiceImpl implements CscpHxDTableManagerService {

    @Autowired
    private CscpHxDInfoSchemaMysqlService cscpHxInfoSchemaMysqlService;

    @Override
    public void syncDatabase(CscpHxDformTable cscpHxFormTable, List<CscpHxDformColumn> cscpHxFormColumns) {
        if(isTableExists(cscpHxFormTable)){
            updateTable(cscpHxFormTable,cscpHxFormColumns);
        } else {
            //如果表不存在，则新建表
            createTableIfNotExists(cscpHxFormTable,cscpHxFormColumns);
        }
    }

    @Override
    public boolean isTableExists(CscpHxDformTable cscpHxDformTable) {
        MySqlDTableUtil dTableUtil = new MySqlDTableUtil();
        dTableUtil .setCscpHxDformTable(cscpHxDformTable);
        return cscpHxInfoSchemaMysqlService.isTableExists(dTableUtil );
    }

    @Override
    public void updateTable(CscpHxDformTable cscpHxDformTable, List<CscpHxDformColumn> cscpHxDformColumns) {
        MySqlDTableUtil dTableUtil = new MySqlDTableUtil();
        dTableUtil.setCscpHxDformColumns(cscpHxDformColumns);
        dTableUtil.setCscpHxDformTable(cscpHxDformTable);
        List<CscpHxInfoSchemaMysql> oldInformationSchemaMysqlLists = cscpHxInfoSchemaMysqlService.getTableFieldInfo(dTableUtil);
        dTableUtil.setOriginalSchemaMysql(oldInformationSchemaMysqlLists);
        cscpHxInfoSchemaMysqlService.alterTableFields(dTableUtil);
    }

    @Override
    public void createTableIfNotExists(CscpHxDformTable cscpHxDformTable, List<CscpHxDformColumn> cscpHxDformColumns) {
        MySqlDTableUtil tableUtil = new MySqlDTableUtil();
        tableUtil.setCscpHxDformTable(cscpHxDformTable);
        tableUtil.setCscpHxDformColumns(cscpHxDformColumns);
        cscpHxInfoSchemaMysqlService.createTable(tableUtil);
    }

    @Override
    public void deleteTableIfExists(CscpHxDformTable cscpHxFormTable) {
        MySqlDTableUtil tableUtil= new MySqlDTableUtil();
        tableUtil.setCscpHxDformTable(cscpHxFormTable);
        cscpHxInfoSchemaMysqlService.dropTable(tableUtil);
    }
}
