package com.ctsi.ssdc.gen.service.impl;


import com.ctsi.ssdc.gen.domain.CscpHxFormColumn;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.domain.CscpHxInfoSchemaMysql;
import com.ctsi.ssdc.gen.service.CscpHxInfoSchemaMysqlService;
import com.ctsi.ssdc.gen.service.CscpHxTableManagerService;
import com.ctsi.ssdc.gen.util.MySqlTableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表单管理接口实现类
 */
@Service
public class CscpHxTableManagerServiceImpl implements CscpHxTableManagerService {

    @Autowired
    private CscpHxInfoSchemaMysqlService cscpHxInfoSchemaMysqlService;

    /**
     * 功能：新建一个表，是否创建交由syncDatabase()判断
     *
     * @param cscpHxFormTable  表信息
     * @param cscpHxFormColumns 表的字段信息
     */
    @Override
    public void createTableIfNotExists(CscpHxFormTable cscpHxFormTable, List<CscpHxFormColumn> cscpHxFormColumns) {
        MySqlTableUtil tableUtil = new MySqlTableUtil();
        tableUtil.setCscpHxFormTable(cscpHxFormTable);
        tableUtil.setCscpHxFormColumns(cscpHxFormColumns);
        cscpHxInfoSchemaMysqlService.createTable(tableUtil);
    }

    /**
     * 功能：根据输入信息，更新数据库表
     *
     * @param cscpHxFormTable  表信息
     * @param cscpHxFormColumns 表的字段信息
     */
    @Override
    public void updateTable(CscpHxFormTable cscpHxFormTable, List<CscpHxFormColumn> cscpHxFormColumns) {
        MySqlTableUtil mySqlTableUtil = new MySqlTableUtil();
        mySqlTableUtil.setCscpHxFormColumns(cscpHxFormColumns);
        mySqlTableUtil.setCscpHxFormTable(cscpHxFormTable);
        List<CscpHxInfoSchemaMysql> oldInformationSchemaMysqlLists = cscpHxInfoSchemaMysqlService.getTableFieldInfo(mySqlTableUtil);
        mySqlTableUtil.setOriginalSchemaMysql(oldInformationSchemaMysqlLists);
        cscpHxInfoSchemaMysqlService.alterTableFields(mySqlTableUtil);
    }

    /**
     * 功能：根据表名判断表是否存在
     *
     * @param cscpHxFormTable
     * @return
     */
    @Override
    public boolean isTableExists(CscpHxFormTable cscpHxFormTable) {
        MySqlTableUtil mySqlTableUtil= new MySqlTableUtil();
        mySqlTableUtil.setCscpHxFormTable(cscpHxFormTable);
        return cscpHxInfoSchemaMysqlService.isTableExists(mySqlTableUtil);
    }

    /**
     * 功能：判断表中的某个字段是否存在
     *
     * @param cscpHxFormTable  表信息
     * @param cscpHxFormColumn 表的字段信息
     * @return
     */
    @Override
    public boolean isColunmExists(CscpHxFormTable cscpHxFormTable, CscpHxFormColumn cscpHxFormColumn) {
        MySqlTableUtil mySqlTableUtil = new MySqlTableUtil();
        mySqlTableUtil.setCscpHxFormTable(cscpHxFormTable);
        mySqlTableUtil.setSingleCscpHxFormColumn(cscpHxFormColumn);
        return cscpHxInfoSchemaMysqlService.isColumnExists(mySqlTableUtil);
    }

    /**
     * 删除表操作，单独使用
     *
     * @param cscpHxFormTable
     */
    @Override
    public void deleteTableIfExists(CscpHxFormTable cscpHxFormTable) {
        MySqlTableUtil mySqlTableUtil= new MySqlTableUtil();
        mySqlTableUtil.setCscpHxFormTable(cscpHxFormTable);
        cscpHxInfoSchemaMysqlService.dropTable(mySqlTableUtil);
    }

    /**
     * 同步数据库，包括新建表和更新表，但不包括删除表操作
     * 注意：表的主键一旦确定，不能再更改
     *
     * @param cscpHxFormTable  表信息
     * @param cscpHxFormColumns 表的字段信息
     */
    @Override
    public void syncDatabase(CscpHxFormTable cscpHxFormTable, List<CscpHxFormColumn> cscpHxFormColumns) {
        if(isTableExists(cscpHxFormTable)){
            updateTable(cscpHxFormTable,cscpHxFormColumns);
        } else {
            //如果表不存在，则新建表
            createTableIfNotExists(cscpHxFormTable,cscpHxFormColumns);
        }
    }
}
