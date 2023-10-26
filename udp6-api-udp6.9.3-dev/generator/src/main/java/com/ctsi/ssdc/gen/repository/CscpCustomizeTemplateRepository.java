package com.ctsi.ssdc.gen.repository;

import com.ctsi.ssdc.gen.domain.TablePkMessage;
import com.ctsi.ssdc.gen.domain.TemplateEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

@Mapper
public interface CscpCustomizeTemplateRepository {
    @Select({"<script>",
            "select * from ${tableName}",
            "<where>",
            "${queryParams}",
            "</where>",
            "<if test = '#{orderColumn} != null'>" +
                    " order by ${orderColumn}</if>",
            "</script>"
    })
    List<Map> queryTemplatePage(Map<String , Object>  queryMap);

    @Select({"<script>",
            "select * from ${tableName}",
            "<if test = '#{orderColumn} != null'>" ,
                    " order by ${orderColumn} desc</if>",
            "</script>"
    })
    List<Map> queryAllPage(Map<String , Object>  queryMap);





    @Select({"<script>",
            "select * from ${tableName} ",
            "<where>",
            "${idQuery}",
            "</where>",
            "</script>"
    })
    @Options(statementType = StatementType.STATEMENT)
    Map queryTemplate(Map<String , Object> queryMap);

    @Delete({"<script>",
            "delete from ${tableName} ",
            "<where>",
            "${idDel}",
            "</where>",
            "</script>"
    })
    @Options(statementType = StatementType.STATEMENT)
    int delTemplate(Map<String, Object> delMap);

    @Delete({
            "<script>",
            "delete from ${tableName} ",
            "<where>",
            "${idsDel}",
            "</where>",
            "</script>"
    })
    @Options(statementType = StatementType.STATEMENT)
    int batchDelTemplate(Map<String, Object> delMap);

    @Select({"<script>",
            "select " +
                    "${orderColumn} from ${tableName}",
            "</script>"
    })
    List<Map> queryAllPageSql(Map<String , Object>  queryMap);


    @Select({"<script>",
                "select " +
                " ${sqHeader} from ${tableName}"+
                " ${conditions}" +
                " order by ${orderByClause}" +
                " limit ${startIndex} , ${endIndex}" +
             "</script>"
    })
    List<Map> queryAllPageSqls(Map<String , Object>  queryMap);


    @Select({"<script>",
                "select count(1) " +
                " from ${tableName}"+
                " ${conditions}" +
             "</script>"
    })
    int queryAllCount(Map<String , Object>  queryMap);

    @Delete({
            "<script>",
            "delete t2 from ${tableNames} ",
            "<where>",
            "${fkRelationShip}",
            "${mainTableIds}",
            "</where>",
            "</script>"
    })
    @Options(statementType = StatementType.STATEMENT)
    int batchDelChildTemplate(Map<String, Object> delMap);

    @Insert({"<script>",
            "insert into ${tableName} ",
            "<foreach collection='list' item='field' index='index1' open='(' separator=',' close=')'> " ,
            "${field}",
            "</foreach>",
            "values",
            "<foreach collection='valuesList' item='vals' index='index2' separator=','>" ,
                "<foreach collection='vals' item='val' index='index3' open='(' separator=',' close=')'> " ,
                "'${val}'",
                "</foreach>",
            "</foreach>",
            "</script>"
    })
    @Options(statementType = StatementType.STATEMENT)
    int addTemplateBatch(TemplateEntity templateEntity);

    @Insert({"<script>",
            "insert into ${tableName} ",
            "<foreach collection='list' item='field' index='index1' open='(' separator=',' close=')'> " ,
            "${field}",
            "</foreach>",
            "values",
            "<foreach collection='values' item='val' index='index2' open='(' separator=',' close=')'> " ,
            "'${val}'",
            "</foreach>",
            "</script>"
    })
    @Options(statementType = StatementType.STATEMENT)
    int addTemplate(TemplateEntity templateEntity);

    @Update({
            "<script>",
            "update ${tableName} ",
            "<set>",
            "<foreach collection='list' item='field' index='index' open=' ' separator=',' close=' '> " ,
            "${list[index]}='${values[index]}'",
            "</foreach>",
            "</set>",
            "where ${pkField} = ${pkValue}",
            "</script>"
    })
    @Options(statementType = StatementType.STATEMENT)
    int updateTemplate(TemplateEntity templateEntity);

    @Update({
            "<script>",
            "update ${paramMap.tableName} t1 , ${paramMap.mainTableName} t2 set t1.${paramMap.fkName} = '${paramMap.fkValue}' ",
            "where t1.${paramMap.fkName} = t2.${paramMap.fkName} ",
             "and t2.${paramMap.mainPkFeild} = '${paramMap.mainPkValue}' ",
            "</script>"
    })
    @Options(statementType = StatementType.STATEMENT)
    int updateChildTemplateBatch(@Param("paramMap") Map<String,Object> paramMap);

    //todo-information_schema
    @Select({"<script>",
                "select " +
                "table_name tableName, " +
                "'' as tableSchema, " +
                "column_name pkColumn " +
                "from all_cons_columns where table_name in "+
                "<foreach collection=\"tableNameList\" item=\"tableName\" open=\"(\" separator=\",\" close=\")\">" +
                "#{tableName}" +
                "</foreach>" +
//                "  and table_schema in "+
//                "<foreach collection=\"tableSchemaList\" item=\"tableSchema\" open=\"(\" separator=\",\" close=\")\">" +
//                "#{tableSchema}" +
//                "</foreach>" +
            "</script>"
    })
    List<TablePkMessage> queryTablePkList(@Param("tableNameList") List<String> tableNameList,
                                          @Param("tableSchemaList") List<String> tableSchemaList);
}
