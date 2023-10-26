package com.ctsi.ssdc.gen.service;


import com.ctsi.ssdc.gen.domain.CopyTableInfo;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.domain.CscpHxInfoSchemaMysql;
import com.ctsi.ssdc.gen.util.MySqlTableUtil;
import com.ctsi.ssdc.model.AjaxResult;

import java.util.List;
import java.util.Map;

/**
 * MySQL数据库信息接口
 */
public interface CscpHxInfoSchemaMysqlService {
    /**
     * 功能：根据传入数据，新建表
     * @param tableInfo
     */
    void createTable(MySqlTableUtil tableInfo);

    /**
     * 功能：根据表名删除表
     * 删除表操作的返回值affects lines一般为0，不能通过返回值判断操作是否成功
     * @param tableInfo
     */
    void dropTable(MySqlTableUtil tableInfo);

    /**
     * 功能：根据传入数据，更新表结构
     * @param tableInfo
     */
    void alterTableFields(MySqlTableUtil tableInfo);

    /**
     * 功能：根据表名，判断表是否存在
     * @param tableInfo
     * @return
     */
    boolean isTableExists(MySqlTableUtil tableInfo);

    /**
     * 功能：根据表名，获取其对应的主键值
     * @param tableInfo
     * @return
     */
    String getTablePrimaryKey(MySqlTableUtil tableInfo);

    /**
     * 功能：从InformationSchema中获取表的列信息
     * @param tableInfo
     * @return
     */
    List<CscpHxInfoSchemaMysql> getTableFieldInfo(MySqlTableUtil tableInfo);

    /**
     * 获取表的字段信息；
     *  \-Map tableInfo
     *     \--dbName  可没有
     *     \-- tableName
     * @param tableInfo
     * @return
     */
    List<CscpHxInfoSchemaMysql> getTableFieldInfoByMap(Map tableInfo);

    /**
     * 获取表的字段信息；
     *  \-Map tableInfo
     *     \--dbName  可没有
     *     \-- tableName
     * @param tableInfo
     * @return
     */
    List<CscpHxInfoSchemaMysql> getTableFieldInfoByMapBatch(List<String> tableNames,String dbName);

    /**
     * 功能：删除表的主键
     * @param tableInfo
     */
    void dropTablePrimaryKey(MySqlTableUtil tableInfo);

    /**
     * 功能：判断表中的字段是否存在
     * 需要先设置MySqlTableUtil的singleFormField
     * @param tableInfo
     * @return
     */
    boolean isColumnExists(MySqlTableUtil tableInfo);

    /**
     * 获取当前数据库所有的表
     * @param
     * @return
     */
    List getTables();

    /**
     * 获取一张表的单条记录
     * @param tableInfo
     * @param id
     * @return
     */
    Map getSingleRecord(MySqlTableUtil tableInfo,Integer id);

    /**
     *
     * @return 返回数据库表列表
     */
    List getTablesInfo();

    /**
     *
     * @return 返回数据库视图列表
     */
    List getViewsInfo();

    AjaxResult createOrUpdateFormTable(CscpHxFormTable cscpHxFormTable);

    AjaxResult deleteFormTable(String tableName);

    AjaxResult initFormTable(CscpHxFormTable cscpHxFormTable);

    /**
     * 复制表
     * @param copyTableInfo
     */
    AjaxResult copyTable(CopyTableInfo copyTableInfo);

    boolean isTableExists(String tableName);
}
