package com.ctsi.ssdc.gen.repository;

import com.ctsi.ssdc.gen.domain.CopyTableInfo;
import com.ctsi.ssdc.gen.domain.CscpHxInfoSchemaMysql;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

@Mapper
public interface CscpHxInfoSchemaMysqlRepository {

    /**
     * 功能：生成表
     * @param map table名称及table各个field名称属性
     * @return affectlines
     */
    @Update("<script> create table if not exists `${tableName}`(<foreach collection='fields' item='k' index='index' open='' separator=',' close=''> ${k} </foreach> ,PRIMARY KEY(`${primaryKey}`));</script>"

    )
    int executeCreateTable(Map<String , Object> map);


    /**
     * 功能：初始化数据表
     * @param
     * @return affectlines
     */
    @Update("<script> " +
            "CREATE TABLE  ${tableName} ( id number(20) NOT NULL," +
            "  instance_id varchar(64) DEFAULT NULL," +
            "  PRIMARY KEY (id)" +
            ");" +
            "</script>"
    )
    int initFormTable(Map<String , Object> map);



    /**
     * 功能：更新表结构
     * @param map table名称及更新的field的名称属性
     * @return affectlines
     */
//addPkFieldsSql
    @Update("<script>ALTER TABLE ${tableName}" +
            "<foreach collection='changeFields' item='k' index='index' open='' separator=',' close=''>change ${k} </foreach>" +
            "<if test='changeFieldsSize > 0 and (dropFieldsSize > 0 or addFieldsSize > 0)'> ,</if>" +
            "<foreach collection='dropFields' item='l' index='index' open='' separator=',' close=''>drop ${l}</foreach>" +
            "<if test='dropFieldsSize > 0 and addFieldsSize > 0'>,</if>" +
            "<foreach collection='addFields' item='n' index='index' open='' separator=',' close=''>add ${n}</foreach>" +
            "<if test='addPkFieldsSize > 0'>, ADD PRIMARY KEY (</if>" +
            "<foreach collection='addPkFields' item='n' index='index' open='' separator=',' close=''>`${n}`</foreach>" +
            "<if test='addPkFieldsSize > 0'>)</if>" +
            "<if test='dropPkFieldsSize > 0'>,DROP PRIMARY KEY</if>" +
            ";" +
            "</script>")
    int executeAlterTable(Map<String , Object> map);

    /**
     *功能：删除表
     * @param map table名称
     * @return affectlines
     */
    @Delete("<script>DROP TABLE IF EXISTS `${tableName}`;</script>")
    int executeDropTable(Map<String , Object> map);



    /**
     *功能：删除表
     * @param
     * @return affectlines
     */
    @Delete("<script>DROP TABLE IF EXISTS `${tableName}`;</script>")
    int executeDropTableByTableName(@Param("tableName") String tableName);

    /**
     * 功能：判断表是否存在
     * @param map table名称及数据库名称
     * @return 若表存在，返回1，返回其他值表示表不存在todo-information_schema
     */
    @Select("select count(1) from user_tables where TABLE_NAME = #{tableName,jdbcType=VARCHAR}")
    int executeIsTableExists(Map<String , Object> map);


    /**
     * 功能：获取表中主键信息
     * @param map table名称及数据库名称
     * @return 表主键名称todo-information_schema
     */
    @Select("select column_name from all_cons_columns where table_name = #{tableName,jdbcType=VARCHAR};")
    String getTablePrimaryKey(Map<String , Object> map);

    /**
     * 功能：根据table名称及数据库名从information_schema的columns表中获取表的字段信息
     * @param map table名称及数据库名称
     * @return 表中所有字段todo-information_schema
     */
    @Select("select " +
            "table_name, 'udp6hx' as table_schema, nullable is_nullable, data_default column_default,'' column_comment," +
            "data_type data_type,data_length character_maximum_length,data_length character_octet_length," +
            "data_type column_type,data_precision numeric_precision,data_scale numeric_scale,'' character_set_name,1 ordinal_position," +
            "'' collation_name,column_id column_key,column_name,'' extra from user_tab_cols " +
            "where table_name = #{tableName,jdbcType=VARCHAR}")
    @Results({
            @Result( column="table_name" ,property="tableName", jdbcType= JdbcType.VARCHAR),
            @Result( column="table_schema" ,property="tableSchema", jdbcType=JdbcType.VARCHAR),
            @Result( column="is_nullable" ,property="isNullable", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_default" ,property="columnDefault", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_comment" ,property="columnComment", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_type" ,property="columnType", jdbcType=JdbcType.INTEGER),
            @Result( column="data_type" ,property="dataType", jdbcType=JdbcType.VARCHAR),
            @Result( column="character_maximum_length" ,property="characterMaximumLength", jdbcType=JdbcType.VARCHAR),
            @Result( column="character_octet_length" ,property="characterOctetLength", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_type" ,property="columnType", jdbcType=JdbcType.INTEGER),
            @Result( column="numeric_precision" ,property="numericPrecision", jdbcType=JdbcType.VARCHAR),
            @Result( column="numeric_scale" ,property="numericScale", jdbcType=JdbcType.VARCHAR),
            @Result( column="character_set_name" ,property="characterSetName", jdbcType=JdbcType.VARCHAR),
            @Result( column="collation_name" ,property="collationName", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_key" ,property="columnKey", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_name" ,property="columnName", jdbcType=JdbcType.VARCHAR),
            @Result( column="extra" ,property="extra", jdbcType=JdbcType.VARCHAR),
            @Result( column="ordinal_position" ,property="ordinalPosition", jdbcType=JdbcType.VARCHAR)
    })
    List<CscpHxInfoSchemaMysql> getTableFields(Map<String , Object> map);


    /**
     * 功能：根据table名称及数据库名从information_schema的columns表中获取表的字段信息
     * @param
     * @return 表中所有字段todo-information_schema
     */
    @Select({
            "<script>",
            "select ",
                    "table_name, 'udp6hx' as table_schema, nullable is_nullable, data_default column_default,'' column_comment,",
                    "data_type data_type,data_length character_maximum_length,data_length character_octet_length,",
                    "data_type column_type,data_precision numeric_precision,data_scale numeric_scale,'' character_set_name,1 ordinal_position,",
                    "'' collation_name,column_id column_key,column_name,'' extra from user_tab_cols ",
            "where table_name in " ,
            "<foreach collection=\"tableNames\" item=\"tableName\" open=\"(\" separator=\",\" close=\")\">" ,
            "#{tableName} ",
            "</foreach>" ,
            "</script>"})
    @Results({
            @Result( column="table_name" ,property="tableName", jdbcType= JdbcType.VARCHAR),
            @Result( column="table_schema" ,property="tableSchema", jdbcType=JdbcType.VARCHAR),
            @Result( column="is_nullable" ,property="isNullable", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_default" ,property="columnDefault", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_comment" ,property="columnComment", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_type" ,property="columnType", jdbcType=JdbcType.INTEGER),
            @Result( column="data_type" ,property="dataType", jdbcType=JdbcType.VARCHAR),
            @Result( column="character_maximum_length" ,property="characterMaximumLength", jdbcType=JdbcType.VARCHAR),
            @Result( column="character_octet_length" ,property="characterOctetLength", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_type" ,property="columnType", jdbcType=JdbcType.INTEGER),
            @Result( column="numeric_precision" ,property="numericPrecision", jdbcType=JdbcType.VARCHAR),
            @Result( column="numeric_scale" ,property="numericScale", jdbcType=JdbcType.VARCHAR),
            @Result( column="character_set_name" ,property="characterSetName", jdbcType=JdbcType.VARCHAR),
            @Result( column="collation_name" ,property="collationName", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_key" ,property="columnKey", jdbcType=JdbcType.VARCHAR),
            @Result( column="column_name" ,property="columnName", jdbcType=JdbcType.VARCHAR),
            @Result( column="extra" ,property="extra", jdbcType=JdbcType.VARCHAR),
            @Result( column="ordinal_position" ,property="ordinalPosition", jdbcType=JdbcType.VARCHAR)
    })
    List<CscpHxInfoSchemaMysql> getTableFieldsBatch(@Param("tableNames") List<String> tableNames,@Param("dbName") String dbName);

    /**
     * 删除主键字段
     * @param map
     */
    @Update("<script>" +
            "ALTER TABLE ${tableName} drop primary key;"+
            "</script>")
    void dropKeyTableField(Map<String, Object> map);

    /**
     * 功能：查询表中的字段是否存在
     * @param map table名称及数据库名称
     * @return 表主键名称todo-information_schema
     */
    @Select("select count(*) from user_tab_cols where table_name = #{tableName,jdbcType=VARCHAR} and column_name=#{columnName,jdbcType=VARCHAR}")
    int executeIsColumnExists(Map<String , Object> map);

    @Select("select table_name FROM user_tables")
    List<String> getTables(String dbName);

    @Select("select * from ${tableName} where id=#{id,jdbcType=INTEGER}")
    Map<String, Object> getRecord(@Param("tableName") String tableName,@Param("id") Integer id);

    @Select("select count(*) from ${tableName}")
    int getRecordTotal(@Param("tableName") String tableName);



    //todo-information_schema
    @Select("select table_name as table_name,comments table_comment FROM user_tab_comments ")
    List<Map<String, String>> getTablesInfo(String dbName);

    //todo-information_schema
    @Select("select view_name as table_name FROM all_views")
    List<Map<String, String>> getViewsInfo(String dbName);


    /**
     * 复制表
     * @param copyTableInfo
     */
    @Update("<script>" +
            "create table ${newTableName} like ${copyTableName};"+
            "</script>")
    void copyTable(CopyTableInfo copyTableInfo);

    /**
     * 备注重命名
     * @param copyTableInfo
     */
    @Update("<script>" +
            "alter table ${newTableName} comment '${newTableComment}';"+
            "</script>")
    void renameComment(CopyTableInfo copyTableInfo);

}
