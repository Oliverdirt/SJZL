package com.ctsi.ssdc.gen.service;

import com.ctsi.ssdc.gen.domain.CscpHxFormColumn;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;

import java.util.List;

/**
 * 表单管理接口
 */
public interface CscpHxTableManagerService {
    /**
     * 功能：新建一个表，是否创建交由syncDatabase()判断
     * @param cscpHxFormTable 表信息
     * @param cscpHxFormColumns 表的字段信息
     */
    void createTableIfNotExists(CscpHxFormTable cscpHxFormTable, List<CscpHxFormColumn> cscpHxFormColumns);

    /**
     * 功能：根据输入信息，更新数据库表
     * @param cscpHxFormTable 表信息
     * @param cscpHxFormColumns 表的字段信息
     */
    void updateTable(CscpHxFormTable cscpHxFormTable, List<CscpHxFormColumn> cscpHxFormColumns);

    /**
     * 功能：根据表名判断表是否存在
     * @param cscpHxFormTable
     * @return
     */
    boolean isTableExists(CscpHxFormTable cscpHxFormTable);

    /**
     * 功能：判断表中的某个字段是否存在
     * @param cscpHxFormTable
     * @return
     */
    boolean isColunmExists(CscpHxFormTable cscpHxFormTable, CscpHxFormColumn cscpHxFormColumn);

    /**
     * 删除表操作，单独使用
     * @param cscpHxFormTable
     */
    void deleteTableIfExists(CscpHxFormTable cscpHxFormTable);

    /**
     * 同步数据库，包括新建表和更新表，但不包括删除表操作
     * 注意：表的主键一旦确定，不能再更改
     */
    void syncDatabase(CscpHxFormTable cscpHxFormTable, List<CscpHxFormColumn> cscpHxFormColumns);
}
