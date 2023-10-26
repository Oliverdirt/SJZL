package com.ctsi.ssdc.gen.service.impl;

import com.ctsi.ssdc.gen.domain.CopyTableInfo;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.domain.CscpHxInfoSchemaMysql;
import com.ctsi.ssdc.gen.repository.CscpHxInfoSchemaMysqlRepository;
import com.ctsi.ssdc.gen.service.CscpHxInfoSchemaMysqlService;
import com.ctsi.ssdc.gen.service.CscpHxTableManagerService;
import com.ctsi.ssdc.gen.util.MySqlTableUtil;
import com.ctsi.ssdc.model.AjaxResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MySQL数据库信息实现类
 */
@Service
public class CscpHxInfoSchemaMysqlServiceImpl implements CscpHxInfoSchemaMysqlService {

    @Value("${spring.datasource.db-name}")
    private String dbName;

    @Value("${spring.datasource.db-filter:''}")
    private String dbFilter;

    @Autowired
    private CscpHxInfoSchemaMysqlRepository informationSchemaMysqlRepository;

    @Autowired
    private CscpHxTableManagerService cscpHxTableManagerService;


    @Override
    public void createTable(MySqlTableUtil tableInfo) {
        informationSchemaMysqlRepository.executeCreateTable(tableInfo.getCreateTableSql());
    }

    @Override
    public void dropTable(MySqlTableUtil tableInfo) {
        informationSchemaMysqlRepository.executeDropTable(tableInfo.getTableNameMap());
    }

    @Override
    public void alterTableFields(MySqlTableUtil tableInfo) {
        informationSchemaMysqlRepository.executeAlterTable(tableInfo.getUpdateField());
    }

    @Override
    public boolean isTableExists(MySqlTableUtil tableInfo) {
        tableInfo.setDbName(dbName);
        return informationSchemaMysqlRepository.executeIsTableExists(tableInfo.getDbTableName()) == 1;
    }

    @Override
    public String getTablePrimaryKey(MySqlTableUtil tableInfo) {
        tableInfo.setDbName(dbName);
        return informationSchemaMysqlRepository.getTablePrimaryKey(tableInfo.getDbTableName());
    }

    @Override
    public List<CscpHxInfoSchemaMysql> getTableFieldInfo(MySqlTableUtil tableInfo) {
        tableInfo.setDbName(dbName);
        return informationSchemaMysqlRepository.getTableFields(tableInfo.getDbTableName());
    }

    /**
     * 获取表的字段信息；
     * \-Map tableInfo
     * \--dbName  可没有
     * \-- tableName
     *
     * @param tableInfo
     * @return
     */
    @Override
    public List<CscpHxInfoSchemaMysql> getTableFieldInfoByMap(Map tableInfo) {
        if (null == tableInfo.get("dbName")) {
            tableInfo.put("dbName", dbName);
        }
        return informationSchemaMysqlRepository.getTableFields(tableInfo);
    }

    /**
     * 获取表的字段信息；
     * \-Map tableInfo
     * \--dbName  可没有
     * \-- tableName
     *
     * @return
     */
    @Override
    public List<CscpHxInfoSchemaMysql> getTableFieldInfoByMapBatch(List<String> tableNames,String dbNames) {
        if (StringUtils.isEmpty(dbNames)){
            dbNames =  dbName;
        }
        return informationSchemaMysqlRepository.getTableFieldsBatch(tableNames,dbNames);
    }




    @Override
    public void dropTablePrimaryKey(MySqlTableUtil tableInfo) {
        informationSchemaMysqlRepository.dropKeyTableField(tableInfo.getTableNameMap());
    }

    @Override
    public boolean isColumnExists(MySqlTableUtil tableInfo) {
        return informationSchemaMysqlRepository.executeIsColumnExists(tableInfo.getTableColumnName()) == 1;
    }

    /**
     * 获取当前数据库所有的表
     *
     * @param
     * @return
     */
    @Override
    public List getTables() {
        return informationSchemaMysqlRepository.getTables(dbName);
    }

    /**
     * 获取一张表的单条记录
     *
     * @param tableInfo
     * @param id
     * @return
     */
    @Override
    public Map getSingleRecord(MySqlTableUtil tableInfo, Integer id) {
        String tableName = tableInfo.getCscpHxFormTable().getTableName();
        return informationSchemaMysqlRepository.getRecord(tableName, id);
    }

    /**
     * 获取数据库表基本信息，表名和备注
     * 并对系统表进行过滤
     *
     * @return
     */
    @Override
    public List getTablesInfo() {
        List<Map<String, String>> tables = informationSchemaMysqlRepository.getTablesInfo(dbName);
        //过滤系统表
        return tables.stream().filter(t -> !t.get("table_name").matches(dbFilter)).collect(Collectors.toList());
    }

    /**
     * 获取数据库视图基本信息，视图名称
     *
     * @return
     */
    @Override
    public List getViewsInfo() {
        //过滤系统表
        return informationSchemaMysqlRepository.getViewsInfo(dbName);
    }

    @Override
    public AjaxResult createOrUpdateFormTable(CscpHxFormTable cscpHxFormTable) {
        if (cscpHxFormTable.getTableName().matches(dbFilter)) {
            // 系统表不可在线更新
            return AjaxResult.error(400, "系统表不可在线更新");
        }
        // 判断表是否存在
        MySqlTableUtil mySqlTableUtil = new MySqlTableUtil();
        mySqlTableUtil.setCscpHxFormTable(cscpHxFormTable);
        mySqlTableUtil.setDbName(dbName);
        boolean isTableExists = informationSchemaMysqlRepository.executeIsTableExists(mySqlTableUtil.getDbTableName()) != 0;
        if (isTableExists) {
            cscpHxTableManagerService.updateTable(cscpHxFormTable, cscpHxFormTable.getColumns());
            return AjaxResult.success(cscpHxFormTable.getTableName() + "更新成功");
        } else {
            //如果表不存在，则新建表
            cscpHxTableManagerService.createTableIfNotExists(cscpHxFormTable, cscpHxFormTable.getColumns());
            return AjaxResult.success(cscpHxFormTable.getTableName() + "创建成功");
        }

    }

    @Override
    public AjaxResult deleteFormTable(String tableName) {
        if (tableName.matches(dbFilter)) {
            // 系统表不可删除
            return AjaxResult.error(400, "系统表无法删除");
        }
        boolean isNoTableExists = isNoTableExists(tableName);
        if (isNoTableExists) {
            // 数据表不存在
            return AjaxResult.error(400, "数据表不存在");
        }


        // 判断当前表是否有数据
        int recordTotal = informationSchemaMysqlRepository.getRecordTotal(tableName);
        if (recordTotal > 0) {
            // 数据表中存在数据无法删除
            return AjaxResult.error(400, "数据表中存在数据无法删除");
        }
        // 执行删除操作
        int dropTableByTableName = informationSchemaMysqlRepository.executeDropTableByTableName(tableName);
        if (dropTableByTableName == 0) {
            return AjaxResult.success(tableName + "删除成功");
        } else {
            return AjaxResult.error(tableName + "删除失败");
        }
    }

    private boolean isNoTableExists(String tableName) {
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableName);
        map.put("dbName", dbName);
        // 判断表是否存在
        return informationSchemaMysqlRepository.executeIsTableExists(map) != 1;
    }

    @Override
    public AjaxResult initFormTable(CscpHxFormTable cscpHxFormTable) {
        MySqlTableUtil mySqlTableUtil = new MySqlTableUtil();
        mySqlTableUtil.setCscpHxFormTable(cscpHxFormTable);
        mySqlTableUtil.setDbName(dbName);
        // 判断表是否存在
        boolean isTableExists = informationSchemaMysqlRepository.executeIsTableExists(mySqlTableUtil.getDbTableName()) != 1;
        if (!isTableExists) {
            // 数据表不存在
            return AjaxResult.error(400, "数据表已存在");
        }
        // 初始化数据表
        informationSchemaMysqlRepository.initFormTable(mySqlTableUtil.getDbTableContent());
        return AjaxResult.success(cscpHxFormTable.getTableName() + "创建成功");
    }

    @Override
    public AjaxResult copyTable(CopyTableInfo copyTableInfo) {
        // 判断目标表是否存在
        boolean tableNoExists = isNoTableExists(copyTableInfo.getCopyTableName());
        if (tableNoExists) {
            // 数据表不存在
            return AjaxResult.error("目标数据表不存在", copyTableInfo);
        }
        // 判断新表是否存在
        boolean newTableNoExists = isNoTableExists(copyTableInfo.getNewTableName());
        if (!newTableNoExists) {
            // 数据表不存在
            return AjaxResult.error("新数据表已存在", copyTableInfo);
        }

        // 复制数据表
        informationSchemaMysqlRepository.copyTable(copyTableInfo);
        informationSchemaMysqlRepository.renameComment(copyTableInfo);

        return AjaxResult.success("数据表复制成功");
    }

    @Override
    public boolean isTableExists(String tableName) {
        return !isNoTableExists(tableName);
    }

}
