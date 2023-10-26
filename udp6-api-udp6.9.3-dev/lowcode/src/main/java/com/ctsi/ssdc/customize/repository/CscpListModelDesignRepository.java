package com.ctsi.ssdc.customize.repository;

import com.ctsi.ssdc.gen.domain.TablePkMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/8/26 15:44  by xx
 */
@Mapper
public interface CscpListModelDesignRepository {
    @Select("select count(*) from cscp_hx_dform_table where table_name = #{tableName}")
    int selectCount(String tableName);

    @Update("update cscp_customize_model set table_id=#{tableId} where table_name = #{tableName}")
    int updateModelInfo(@Param("tableId") String tableId, @Param("tableName") String tableName);

    @Select("select count(1) from ${tableName}")
    int qryDateByTableName(@Param("tableName") String tableName);

    @Select("select table_name from cscp_hx_dform_table where table_id = #{tableId}")
    String qryTableNameByTableId(@Param("tableId") String tableId);

    @Select({"<script>",
            "select * from ${tableName}",
            "<if test = '#{orderColumn} != null'>" ,
            " order by ${orderColumn} desc</if>",
            "</script>"
    })
    List<Map> queryAllPage(Map<String, Object> queryMap);

    @Select({"<script>",
            "select * from ${tableName}",
            "<where>",
            "${queryParams}",
            "</where>",
            "<if test = '#{orderColumn} != null'>" +
                    " order by ${orderColumn}</if>",
            "</script>"
    })
    List<Map> queryTemplatePage(Map<String, Object> queryMap);


    @Select({"<script>",
            "select " +
                    "table_name tableName, " +
                    "GROUP_CONCAT( column_name SEPARATOR ',' ) pkColumn " +
                    "from cscp_hx_dform_column p1,cscp_hx_dform_table p2 where p1.table_id = p2.table_id and and p1.is_pk = 1 " +
                    "and table_name in "+
                    "<foreach collection=\"tableNameList\" item=\"tableName\" open=\"(\" separator=\",\" close=\")\">" +
                    "#{tableName}" +
                    "</foreach>"+
                    "</script>"
    })
    List<TablePkMessage> queryTablePkList(List<String> tableNameList, List<String> tableSchemaList);

    @Select({"select table_id from cscp_hx_dform_table where main_table_id = #{mainTableId}"})
    List<Long> selectChildTable(Long mainTableId);
}
