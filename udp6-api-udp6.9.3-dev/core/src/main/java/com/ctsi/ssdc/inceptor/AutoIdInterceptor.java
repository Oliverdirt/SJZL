package com.ctsi.ssdc.inceptor;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.model.BoundSqlHelper;
import com.ctsi.ssdc.process.BaseAutoIdProcess;
import com.ctsi.ssdc.process.SnowFlakeAutoIdProcess;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.type.LongTypeHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mybatis主键自动填充插件
 * @author zhaoliangliang
 * @date 2021/9/9
 */
@Component
@Intercepts(value = {@Signature(type = Executor.class, method = "update",
        args = {MappedStatement.class, Object.class})})
public class AutoIdInterceptor implements Interceptor {

    // key为类名，value：为id执行器
    private Map<String, List<BaseAutoIdProcess>> idProcessMap = new ConcurrentHashMap<>();

    //key为类名，value为主键名
    private Map<String, String> primaryKeyMap = new ConcurrentHashMap<>();

    //批量插入分割符 ---判断是否是批量插入的标志
    private static final String INSERT_BATCH_KEY_SPILTE = ".";


    /**
     * 插件执行的拦截方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        //args数组对应对象就是上面@Signature注解中args对应的对应类型
        MappedStatement mappedStatement = (MappedStatement) args[0];
        //实体对象
        Object entity = args[1];

        //如果是插入方法的话
        if ("insert".equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
            //设值主键值以及改变插入语句
            BoundSqlHelper boundSqlHelper = prepareAutoIdAndNewSql(mappedStatement, entity);
            //判断待插入实体类是否已经包含主键ID
            if (!boundSqlHelper.isAleadyIncludePrimaryKey()) {
                //重置SQL
                resetSql2Invocation(invocation, boundSqlHelper, entity);
            }
        }

        return invocation.proceed();
    }


    ///**
    // * 数据库表名
    // */
    //private String getTableName(String sql){
    //    sql= sql.toLowerCase();
    //
    //    int intoIndex = StringUtils.ordinalIndexOf(sql, "into", 1);
    //
    //    int firstIndextKey=StringUtils.ordinalIndexOf(sql, "(", 1);
    //    return sql.substring(intoIndex+4, firstIndextKey)
    //            .replace("\n", "").replace(" ","");
    //}

    /**
     * 获取插入列们
     */
    //private List getColumns(String sql){
    //    sql= sql.toLowerCase();
    //    int firstIndextKey=StringUtils.ordinalIndexOf(sql, "(", 1);
    //    int firstIndextKey2=StringUtils.ordinalIndexOf(sql, ")", 1);
    //
    //    //return sql.substring(firstIndextKey+1, firstIndextKey2)
    //    //        .replace("\n", "").replace(" ","");
    //
    //    return null;
    //}

    /**
     * 设值自增ID并返回主键值
     *
     * @param entity
     */
    private String setAutoIdAndReturnPrimaryKey(Object entity) throws Exception {
        List<BaseAutoIdProcess> baseAutoIdProcessList = getIdProcessList(entity);
        for (BaseAutoIdProcess baseAutoIdProcess : baseAutoIdProcessList) {
            baseAutoIdProcess.process(entity);
        }
        return baseAutoIdProcessList.get(0).getPrimaryKey();
    }

    /**
     * 设值主键值以及改变插入语句
     *
     * @return
     */
    private BoundSqlHelper prepareAutoIdAndNewSql(
            MappedStatement mappedStatement, Object entity) throws Exception {
        //得到原来的SQL语句
        BoundSql boundSql = mappedStatement.getBoundSql(entity);
        String originSql = boundSql.getSql();

        //获取表名，操作的的列名
        MySqlStatementParser mySqlStatementParser = new MySqlStatementParser(originSql);
        SQLStatement statement = mySqlStatementParser.parseStatement();
        SQLInsertStatement sqlInsertStatement = (SQLInsertStatement) statement;
        SQLExprTableSource sqlTableSource = sqlInsertStatement.getTableSource();
        List<SQLExpr> columns = sqlInsertStatement.getColumns();
        String tableName = sqlTableSource.toString();

        //构建新的sql语句
        StringBuilder newSqlColumnsSb = new StringBuilder();
        //获取待插入对象/们
        Set<Object> entitySet = getEntitySet(entity);
        Object firstEntity = entitySet.iterator().next();
        //获取数据库主键
        String primaryKey = getPrimaryKey(firstEntity);

        if (primaryKey==null||"".equals(primaryKey)){
            throw new IllegalArgumentException(" Unable to get the primary key of the " +
                    tableName +" table, please try to decorate it with the annotation @AutoId(primaryKey = \"id\")" +
                    "on  the  primary key ?");
        }

        //插入列中包含的主键列数量
        int columnsIncludePrimaryKeyCount = 0;
        //SQL参数中中是否已经包含主键
        boolean isAleadyIncludePrimaryKey = false;
        //其他待插入的键
        for (SQLExpr column : columns) {
            String sqlColumn = column.toString().replace("`", "").replace("`", "");
            sqlColumn = StringUtils.deleteWhitespace(sqlColumn);
            if (primaryKey.equals(sqlColumn)) {
                columnsIncludePrimaryKeyCount++;
                isAleadyIncludePrimaryKey = true;
                continue;
            }
            newSqlColumnsSb.append(column.toString()).append(",");
        }
        //(user_name,sex,domain,dept_name,email,mobile,create_time,update_time,id)
        newSqlColumnsSb.append(primaryKey);

        //如果主键已经存在，则无需向下执行构造新sql，沿用旧sql，并填充id值即可
        if (isAleadyIncludePrimaryKey) {
            BoundSqlHelper boundSqlHelper = new BoundSqlHelper();
            boundSqlHelper.setAleadyIncludePrimaryKey(isAleadyIncludePrimaryKey);
            for (Object o : entitySet) {
                setAutoIdAndReturnPrimaryKey(o);
            }
            return boundSqlHelper;
        }
        //构造出 insert into table (column1，column2，...) values
        StringBuilder newSqlPrefixSb = new StringBuilder();
        newSqlPrefixSb.append("insert into ").append(tableName).append("(")
                .append(newSqlColumnsSb).append(") ").append("values ");

        //构造占位符，形如(?,?,?)
        String newSqlValuePlaceholder = StringUtils.repeat("?,", columns.size() - columnsIncludePrimaryKeyCount);
        newSqlValuePlaceholder = newSqlValuePlaceholder + "?";
        StringBuilder newSqlValuePlaceholderSb = new StringBuilder();
        newSqlValuePlaceholderSb.append("(").append(newSqlValuePlaceholder).append(")");
        StringBuilder newSqlSuffixSb = new StringBuilder();

        //判断是否是批量插入操作
        boolean isInsertBatch = isBatchInsert(boundSql);
        int idIndex = 0;
        //主键参数列表
        List<String> primaryKeyList = new ArrayList<>();
        //获取批量插入操作的参数前缀:形如__frch_user_
        String insertBatchKeyPrefix = getInsertBatchKeyPrefix(boundSql);
        //看看要插入几个
        for (Object o : entitySet) {
            if (isInsertBatch) {
                String idProp = insertBatchKeyPrefix + idIndex + INSERT_BATCH_KEY_SPILTE + primaryKey;
                idIndex++;
                primaryKeyList.add(idProp);
            }
            setAutoIdAndReturnPrimaryKey(o);
            newSqlSuffixSb.append(newSqlValuePlaceholderSb).append(",");
        }
        //构造出批处理时的
        newSqlSuffixSb.delete(newSqlSuffixSb.length() - 1, newSqlSuffixSb.length());
        String newSql = newSqlPrefixSb.append(newSqlSuffixSb).toString();

        //sql辅助类设值
        BoundSqlHelper boundSqlHelper = new BoundSqlHelper();
        boundSqlHelper.setPrimaryKey(primaryKey);
        boundSqlHelper.setTypeHandler(new LongTypeHandler());
        boundSqlHelper.setBoundSql(boundSql);
        boundSqlHelper.setSql(newSql);
        boundSqlHelper.setConfiguration(mappedStatement.getConfiguration());
        boundSqlHelper.setInsertBatch(isInsertBatch);
        boundSqlHelper.setPrimaryKeyList(primaryKeyList);
        System.out.println(newSql);
        return boundSqlHelper;
    }

    /**
     * 获取数据库主键列的主键名
     *
     * @param entity
     * @return
     */
    private String getPrimaryKey(Object entity) {
        String primaryKey = primaryKeyMap.get(entity.getClass().getName());
        //map中没有就找一下
        if (StringUtils.isEmpty(primaryKey)) {
            synchronized (this) {
                for (Field field : entity.getClass().getDeclaredFields()) { //获取所有本类省名的字段，不包括父类生成的
                    AutoId autoId = field.getAnnotation(AutoId.class);
                    if (!ObjectUtils.isEmpty(autoId)) {
                        primaryKey = autoId.primaryKey(); //数据库主键列名称
                        primaryKeyMap.put(entity.getClass().getName(), primaryKey);
                        break;
                    }
                }
            }
        }

        return primaryKey;
    }

    /**
     * 判断是否是批量插入，如果是批量插入，其ParameterMapping的key形如：__frch_item_0.userName
     *
     * @param boundSql
     * @return
     */
    private boolean isBatchInsert(BoundSql boundSql) {
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (!CollectionUtils.isEmpty(parameterMappings)) {
            ParameterMapping parameterMapping = parameterMappings.get(0);
            String prop = parameterMapping.getProperty();
            return prop.contains(INSERT_BATCH_KEY_SPILTE);
        }
        return false;
    }

    /**
     * 获取批量插入key前缀，比如批量插入的key为__frch_user_0.userName
     *
     * @return 返回__frch_user_
     */
    private String getInsertBatchKeyPrefix(BoundSql boundSql) {
        boolean isInsertBatch = isBatchInsert(boundSql);
        if (!isInsertBatch) {
            return null;
        }
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (!CollectionUtils.isEmpty(parameterMappings)) {
            ParameterMapping parameterMapping = parameterMappings.get(0);
            String prop = parameterMapping.getProperty();
            //截取出来
            String prefix = StringUtils.substring(prop, 0, StringUtils.lastIndexOf(prop, INSERT_BATCH_KEY_SPILTE));
            String insertBatchKeyPrefix = StringUtils.substring(prefix, 0, prefix.length() - 1);
            return insertBatchKeyPrefix;
        }

        return null;
    }


    /**
     * 获取id执行器列表
     *
     * @param entity
     * @return
     */
    private List<BaseAutoIdProcess> getIdProcessList(Object entity) {
        String className = entity.getClass().getName();
        List<BaseAutoIdProcess> idProcesses = idProcessMap.get(className);
        if (ObjectUtils.isEmpty(idProcesses)) {
            synchronized (this) {
                List<BaseAutoIdProcess> finalIdProcesses = new ArrayList<>();
                ReflectionUtils.doWithFields(entity.getClass(), field -> {
                    ReflectionUtils.makeAccessible(field);
                    AutoId autoId = field.getAnnotation(AutoId.class);
                    if (!ObjectUtils.isEmpty(autoId)) {
                        Class<? extends BaseAutoIdProcess> idProcessClass = autoId.type();
                        try {
                            Constructor<? extends BaseAutoIdProcess> constructor =
                                    idProcessClass.getConstructor(Field.class);
                            BaseAutoIdProcess autoIdProcess = constructor.newInstance(field);
                            autoIdProcess.setPrimaryKey(autoId.primaryKey());
                            finalIdProcesses.add(autoIdProcess);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                idProcessMap.put(className, finalIdProcesses);
            }
        }

        return idProcessMap.get(className);
    }


    /**
     * entity是需要插入的实体数据,它可能是对象,也可能是批量插入的对象。
     * 如果是单个对象,那么entity就是当前对象
     * 如果是批量插入对象，那么entity就是一个map集合,key值为"list",value为ArrayList集合对象
     */
    private Set<Object> getEntitySet(Object entity) {
        //待插入对象
        Set<Object> set = new HashSet<>();
        if (entity instanceof Map) {
            //批量插入对象
            Collection values = (Collection) ((Map) entity).get("list");
            System.out.println("values = " + values);
            for (Object value : values) {
                if (value instanceof Collection) {
                    set.addAll((Collection) value);
                } else {
                    set.add(value);
                }
            }
        } else {
            //单个插入对象
            set.add(entity);
        }
        return set;
    }

    /**
     * 继续执行其他的插件
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 重置sql
     * @param invocation
     * @param boundSqlHelper
     * @param entity
     * @throws SQLException
     */
    private void resetSql2Invocation(Invocation invocation,
                                     BoundSqlHelper boundSqlHelper, Object entity) throws SQLException {
        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        MappedStatement newStatement = newMappedStatement(statement, new BoundSqlSqlSource(boundSqlHelper));
        MetaObject msObject = MetaObject.forObject(newStatement, new DefaultObjectFactory(),
                new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        msObject.setValue("sqlSource.boundSqlHelper.boundSql.sql", boundSqlHelper.getSql());
//        for (ParameterMapping mapping : boundSqlHelper.getBoundSql().getParameterMappings()) {
//            String prop = mapping.getProperty();
//            System.out.println(prop);
//            if (boundSqlHelper.getBoundSql().hasAdditionalParameter(prop)) {
//                boundSqlHelper.getBoundSql().setAdditionalParameter(prop, boundSqlHelper.getBoundSql().getAdditionalParameter(prop));
//            }
//        }
        args[0] = newStatement;

    }

    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder =
                new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    static class BoundSqlSqlSource implements SqlSource {
        private BoundSqlHelper boundSqlHelper;

        public BoundSqlSqlSource(BoundSqlHelper boundSqlHelper) {
            this.boundSqlHelper = boundSqlHelper;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            boolean isAleadyIncludePrimaryKey = boundSqlHelper.isAleadyIncludePrimaryKey();
            if (!isAleadyIncludePrimaryKey) {
                if (boundSqlHelper.isInsertBatch()) {
                    setPrimaryParameteMappingsAndSort();
                } else {
                    setPrimaryParameterMapping(boundSqlHelper.getPrimaryKey());
                }

            }

            return boundSqlHelper.getBoundSql();
        }

        /**
         * 设置批量插入的主键key，并对parameterMappings进行按key的索引下标排序。排序主要是让id值存放到他本该的存放的位置。
         * 如果不进行排序，则id值是追加到最后
         */
        private void setPrimaryParameteMappingsAndSort() {
            List<String> primaryKeyList = boundSqlHelper.getPrimaryKeyList();
            for (String primaryKey : primaryKeyList) {
                setPrimaryParameterMapping(primaryKey);
            }

            List<ParameterMapping> parameterMappings = boundSqlHelper.getBoundSql().getParameterMappings();
            Collections.sort(parameterMappings, (o1, o2) -> {
                int index1 = getInsertBatchKeyIndex(o1.getProperty());
                int index2 = getInsertBatchKeyIndex(o2.getProperty());
                return index1 - index2;
            });
        }

        private void setPrimaryParameterMapping(String primaryKey) {
            ParameterMapping parameterMapping = new ParameterMapping.Builder(boundSqlHelper.getConfiguration(),
                    primaryKey, boundSqlHelper.getTypeHandler()).build();
            boundSqlHelper.getBoundSql().getParameterMappings().add(parameterMapping);
        }


        /**
         * 获取批量插入前缀索引，形如：__frch_user_0.userName其索引就为0
         *
         * @param prop
         * @return
         */
        private int getInsertBatchKeyIndex(String prop) {
            String prefix = StringUtils.substring(prop, 0, StringUtils.lastIndexOf(prop, INSERT_BATCH_KEY_SPILTE));
            String insertBatchKeyIndex = StringUtils.substring(prefix, prefix.length() - 1, prefix.length());
            return Integer.parseInt(insertBatchKeyIndex);
        }


    }

    @Override
    public void setProperties(Properties properties) {

    }


}
