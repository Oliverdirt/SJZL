package com.ctsi.ssdc.model;

import lombok.Data;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

import java.util.List;

/**
 * @author zhaoliangliang
 * @date 2021/9/14
 */
@Data
public class BoundSqlHelper {

    private BoundSql boundSql;

    private String sql;

    private String primaryKey;

    private TypeHandler typeHandler;

    private Configuration configuration;

    //是否已经包含主键
    private boolean isAleadyIncludePrimaryKey;

    //是否批量操作
    private boolean isInsertBatch;

    private List<String> primaryKeyList;
}
