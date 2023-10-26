package com.ctsi.ssdc.gen.util;

import com.ctsi.ssdc.gen.domain.CscpHxFormColumn;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.domain.CscpHxInfoSchemaMysql;
import com.ctsi.ssdc.enums.MysqlTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQL数据库表操作工具类
 */
public class MySqlTableUtil {
    private static final Logger log = LoggerFactory.getLogger(MySqlTableUtil.class);
    private static final String TABLE_NAME = "tableName";
    private static final String NOT_NULL = "NOT NULL";
    private static final String DEFAULT = "DEFAULT";
    private static final String COLUMN_COMMENT = "COMMENT";

    private String dbName;

    private CscpHxFormTable cscpHxFormTable;

    private CscpHxFormColumn singleCscpHxFormColumn;//用于判断字段是否存在

    private List<CscpHxFormColumn> cscpHxFormColumns;

    private List<CscpHxInfoSchemaMysql> schemaMysqlList;

    private List<CscpHxInfoSchemaMysql> originalSchemaMysql;

    private List<String> addFieldsSql;

    private List<String> changeFieldsSql;

    private List<String> dropFieldsSql;

    private List<String> addPkFieldsSql;
    private List<String> dropPkFieldsSql;

    public CscpHxFormTable getCscpHxFormTable() {
        return cscpHxFormTable;
    }

    public void setCscpHxFormTable(CscpHxFormTable cscpHxFormTable) {
        this.cscpHxFormTable = cscpHxFormTable;
    }

    public List<CscpHxFormColumn> getCscpHxFormColumns() {
        return cscpHxFormColumns;
    }

    public void setCscpHxFormColumns(List<CscpHxFormColumn> cscpHxFormColumns) {
        this.cscpHxFormColumns = cscpHxFormColumns;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }


    public CscpHxFormColumn getSingleCscpHxFormColumn() {
        return singleCscpHxFormColumn;
    }

    public void setSingleCscpHxFormColumn(CscpHxFormColumn singleCscpHxFormColumn) {
        this.singleCscpHxFormColumn = singleCscpHxFormColumn;
    }

    public List<CscpHxInfoSchemaMysql> getSchemaMysqlList() {
        return schemaMysqlList;
    }

    public void setSchemaMysqlList(List<CscpHxInfoSchemaMysql> schemaMysqlList) {
        this.schemaMysqlList = schemaMysqlList;
    }

    public List<CscpHxInfoSchemaMysql> getOriginalSchemaMysql() {
        return originalSchemaMysql;
    }

    public void setOriginalSchemaMysql(List<CscpHxInfoSchemaMysql> originalSchemaMysql) {
        this.originalSchemaMysql = originalSchemaMysql;
    }

    public List<String> getAddFieldsSql() {
        return addFieldsSql;
    }

    public void setAddFieldsSql(List<String> addFieldsSql) {
        this.addFieldsSql = addFieldsSql;
    }

    public List<String> getChangeFieldsSql() {
        return changeFieldsSql;
    }

    public void setChangeFieldsSql(List<String> changeFieldsSql) {
        this.changeFieldsSql = changeFieldsSql;
    }

    public List<String> getDropFieldsSql() {
        return dropFieldsSql;
    }

    public void setDropFieldsSql(List<String> dropFieldsSql) {
        this.dropFieldsSql = dropFieldsSql;
    }

    //将字段信息转换成InformationSchemaMysql形式
    public void convertToSechmaMysql(){
        List<CscpHxInfoSchemaMysql> tableColumns = new ArrayList<>();
        for(int i = 0; i < cscpHxFormColumns.size(); i++){
            CscpHxFormColumn column = cscpHxFormColumns.get(i);
            CscpHxInfoSchemaMysql schemaMysql = new CscpHxInfoSchemaMysql();
            schemaMysql.setColumnName(column.getColumnName());
            if(Integer.valueOf(0).equals(column.getIsNull())) {
                schemaMysql.setIsNullable("NO");
            }
            if(!"".equals(column.getDefaultValue()) && column.getDefaultValue() != null) {
                schemaMysql.setColumnDefault(column.getDefaultValue());
            }
           /* if(null == column.getFieldNameOld()){
                schemaMysql.setOriginalTableName("");
            }else if(!column.getFieldNameOld().equals(column.getFieldName())) {//当FormField的原名与现名不同时，再赋予InformationSchemaMysql原有名
                schemaMysql.setOriginalTableName(column.getFieldNameOld());
            }*/
            if (Integer.valueOf(1).equals(column.getIsPk())){
                schemaMysql.setColumnKey("PRI");
            }
            if (StringUtils.isNotBlank(column.getColumnComment())){
                schemaMysql.setColumnComment(column.getColumnComment());
            }
            schemaMysql.setDataType(getFieldType(column));
            tableColumns.add(schemaMysql);
        }
        setSchemaMysqlList(tableColumns);
    }

    //将InformationSchemaMysql转换成FormField形式
    public List<CscpHxFormColumn> convertToForm(List<CscpHxInfoSchemaMysql> infoList){
        List<CscpHxFormColumn> list = new ArrayList<>();
        for (CscpHxInfoSchemaMysql info : infoList){
            CscpHxFormColumn column = new CscpHxFormColumn();
            column.setColumnName(info.getColumnName());
            column.setDefaultValue(info.getColumnDefault());
            if (StringUtils.isNotBlank(info.getIsNullable()) && info.getIsNullable().contains("NO")){
                column.setIsNull(0);
            }
            if (StringUtils.isNotBlank(info.getColumnKey()) && info.getColumnKey().contains("PRI")){
                column.setIsPk(1);
            }
            String type = info.getColumnType().toLowerCase();
            Integer typeNum = null;
            for (MysqlTypeEnum mysqlType : MysqlTypeEnum.class.getEnumConstants()){
                if (mysqlType.getName().equals(type)){
                    typeNum = mysqlType.getNum();
                }
            }
            column.setColumnType(typeNum);
            list.add(column);
        }
       return list;
    }

    /**
     * 比较两个InformationSchemaMysql是否存在联系，以及进行何种修改
     * @param newInformationSchemaMysql
     * @param oldInformationSchemaMysql
     * @return
     */
    private String compareTwoInformationSchemaMysqls(CscpHxInfoSchemaMysql newInformationSchemaMysql,
                                                     CscpHxInfoSchemaMysql oldInformationSchemaMysql) {
        StringBuilder sb = new StringBuilder();

        sb.append(""+oldInformationSchemaMysql.getColumnName()+"").append(" ").
                append(""+newInformationSchemaMysql.getColumnName()+"").append(" ").
                append(newInformationSchemaMysql.getDataType()).append(" ");
        if ("NO".equals(newInformationSchemaMysql.getIsNullable())){
            sb.append(NOT_NULL).append(" ");
        }

        if (!"PRI".equals(oldInformationSchemaMysql.getColumnKey()) && null != newInformationSchemaMysql.getColumnDefault()) {
            if ("CURRENT_TIMESTAMP".equals(newInformationSchemaMysql.getColumnDefault())) {
                sb.append(DEFAULT).append(" ").append(newInformationSchemaMysql.getColumnDefault());
            } else {
                sb.append(DEFAULT).append(" '").append(newInformationSchemaMysql.getColumnDefault()).append("' ");
            }
        }

        if (StringUtils.isNotBlank(newInformationSchemaMysql.getColumnComment())){
//            sb.append(COLUMN_COMMENT).append(" ").append("\'"+newInformationSchemaMysql.getColumnComment()+"\'").append(" ");
        }
        // 取消id自增，采用雪花算法生成id
/*        if (StringUtils.isNotBlank(oldInformationSchemaMysql.getExtra()) && oldInformationSchemaMysql.getExtra().contains("auto_increment")){
            sb.append(" ").append("auto_increment");
        }*/
        if (null != sb.toString() && !"".equals(sb.toString())) {
            changeFieldsSql.add(sb.toString());
        }
        // 判断主键
        if("PRI".equals(newInformationSchemaMysql.getColumnKey()) && !"PRI".equals(oldInformationSchemaMysql.getColumnKey())){
            addPkFieldsSql.add(newInformationSchemaMysql.getColumnName());
        }

        // 判断主键修改 dropPkFieldsSql
        if(!"PRI".equals(newInformationSchemaMysql.getColumnKey())&&"PRI".equals(oldInformationSchemaMysql.getColumnKey())){
            dropPkFieldsSql.add(newInformationSchemaMysql.getColumnName());
        }

        return sb.toString();
    }

    private void getAllAddFiels(){
        //获取新增列表
        for(int i = 0; i < schemaMysqlList.size(); i++){
            boolean isAdd = true;
            CscpHxInfoSchemaMysql newSchemaMysql = schemaMysqlList.get(i);
            String newTableName = newSchemaMysql.getColumnName();
            String tableOldName = newSchemaMysql.getOriginalTableName();
            if(tableOldName==null){
                tableOldName = "";
            }
            for(int j = 0; j < originalSchemaMysql.size(); j++){
                if(tableOldName.equals(originalSchemaMysql.get(j).getColumnName())||
                        ("".equals(tableOldName)&& newTableName.equals(originalSchemaMysql.get(j).getColumnName()))){
                    isAdd = false;
                    break;
                }
            }
            if(isAdd) {
                StringBuilder addBulider = new StringBuilder();
                addBulider.append(""+newSchemaMysql.getColumnName()+"").append(" ").
                        append(newSchemaMysql.getDataType()).append(" ");
                if("NO".equals(newSchemaMysql.getIsNullable())) {
                    addBulider.append(NOT_NULL).append(" ");
                }
                if (!"PRI".equals(newSchemaMysql.getColumnKey()) && null != newSchemaMysql.getColumnDefault()) {
                    if ("CURRENT_TIMESTAMP".equals(newSchemaMysql.getColumnDefault())) {
                        addBulider.append(DEFAULT).append(" ").append(newSchemaMysql.getColumnDefault());
                    } else {
                        addBulider.append(DEFAULT).append(" '").append(newSchemaMysql.getColumnDefault()).append("' ");
                    }
                }
                if (StringUtils.isNotBlank(newSchemaMysql.getColumnComment())){
//                    addBulider.append(COLUMN_COMMENT).append(" ").append("\'"+newSchemaMysql.getColumnComment()+"\'").append(" ");
                }
//                else if (!"PRI".equals(newSchemaMysql.getColumnKey())){
//                    if (!"NO".equals(newSchemaMysql.getIsNullable())){ //设置可为空字段默认值为null
//                        addBulider.append(DEFAULT).append(" ").append(newSchemaMysql.getColumnDefault());
//                    }
//                }
                addFieldsSql.add(addBulider.toString());


                // 判断主键
                if("PRI".equals(newSchemaMysql.getColumnKey())){
                    addPkFieldsSql.add(newSchemaMysql.getColumnName());
                }
            }
        }
    }

    private void getAllDropFiels() {
        //获取删除列表
        for (int i = 0; i < originalSchemaMysql.size(); i++) {
            boolean isDelete = true;
            CscpHxInfoSchemaMysql oldSchemaMysql = originalSchemaMysql.get(i);
            String oldTableName = oldSchemaMysql.getColumnName();
            for (int j = 0; j < schemaMysqlList.size(); j++) {
                if (oldTableName.equals(schemaMysqlList.get(j).getOriginalTableName()) ||
                        (oldTableName.equals(schemaMysqlList.get(j).getColumnName()) && ("".equals(schemaMysqlList.get(j).getOriginalTableName()) || null == schemaMysqlList.get(j).getOriginalTableName()))) {
                    isDelete = false;
                    break;
                }
            }
            if (isDelete) {
                dropFieldsSql.add("" + oldTableName + "");
                // 判断主键修改 dropPkFieldsSql
                if("PRI".equals(oldSchemaMysql.getColumnKey())){
                    dropPkFieldsSql.add(oldTableName);
                }
            }
        }
    }

    private void getAllChangeFiels(){
        //获取修改列表
        for(int i = 0; i < originalSchemaMysql.size(); i++){
            CscpHxInfoSchemaMysql oldSchemaMysql = originalSchemaMysql.get(i);
            for (int j = 0; j < schemaMysqlList.size(); j++){
                CscpHxInfoSchemaMysql newSchemaMysql = schemaMysqlList.get(j);
                if(oldSchemaMysql.getColumnName().equals(newSchemaMysql.getOriginalTableName())
                        || (!oldSchemaMysql.getColumnName().equals(newSchemaMysql.getOriginalTableName())&&oldSchemaMysql.getColumnName().equals(newSchemaMysql.getColumnName()))){
                    compareTwoInformationSchemaMysqls(newSchemaMysql,oldSchemaMysql);
                    break;
                }

            }
        }
    }

    /**
     * 功能：获取更新表字段的SQL中的语句
     * @return
     */
    public Map<String,Object> getUpdateField(){
        changeFieldsSql = new ArrayList<>();
        addFieldsSql = new ArrayList<>();
        dropFieldsSql = new ArrayList<>();
        addPkFieldsSql = new ArrayList<>();
        dropPkFieldsSql = new ArrayList<>();
        //dropPkFieldsSql

        this.convertToSechmaMysql();
        //获取修改列表
        getAllChangeFiels();
        //获取删除列表
        getAllDropFiels();
        //获取新增列表
        getAllAddFiels();
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put(TABLE_NAME,""+cscpHxFormTable.getTableName()+"");
        objectMap.put("changeFields",changeFieldsSql);
        objectMap.put("addFields",addFieldsSql);
        objectMap.put("dropFields",dropFieldsSql);

        objectMap.put("changeFieldsSize",changeFieldsSql.size());
        objectMap.put("addFieldsSize",addFieldsSql.size());
        objectMap.put("dropFieldsSize",dropFieldsSql.size());
        objectMap.put("addPkFields",addPkFieldsSql);
        objectMap.put("dropPkFields",dropPkFieldsSql);
        objectMap.put("addPkFieldsSize",addPkFieldsSql.size());        objectMap.put("addPkFields",addPkFieldsSql);
        objectMap.put("dropPkFieldsSize",dropPkFieldsSql.size());
        return objectMap;
    }

    public Map<String,Object> getCreateTableSql(){

        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put(TABLE_NAME,cscpHxFormTable.getTableName());

        List<String> fieldsList = new ArrayList<>();

        for(CscpHxFormColumn cscpHxFormColumn : this.cscpHxFormColumns){
            if (ObjectUtils.isEmpty(cscpHxFormColumn)) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(""+cscpHxFormColumn.getColumnName()+"").append(" ");
            sb.append(getFieldType(cscpHxFormColumn)).append(" ");
            if(Integer.valueOf(0).equals(cscpHxFormColumn.getIsNull())){
                sb.append(NOT_NULL).append(" ");
            }
            //若为主键不设置默认值
            if(StringUtils.isNotBlank(cscpHxFormColumn.getDefaultValue()) && !Integer.valueOf(1).equals(cscpHxFormColumn.getIsPk())){
                //排除不能添加逗号的,嗯现在只针对 CURRENT_TIMESTAMP
                if ("CURRENT_TIMESTAMP".equals(cscpHxFormColumn.getDefaultValue())){
                    sb.append(DEFAULT).append(" ").append(cscpHxFormColumn.getDefaultValue());
                }else {
                    sb.append(DEFAULT).append(" '").append(cscpHxFormColumn.getDefaultValue()).append("' ");
                }
            }

            if (StringUtils.isNotBlank(cscpHxFormColumn.getColumnComment())) {
//                sb.append(COLUMN_COMMENT).append(" ").append("\'"+cscpHxFormColumn.getColumnComment()+"\'").append(" ");
            }

            if(Integer.valueOf(1).equals(cscpHxFormColumn.getIsPk())){
                objectMap.put("primaryKey",cscpHxFormColumn.getColumnName());
                //取消id自增，采用雪花算法生成id
//                sb.append(" ").append("auto_increment");
            }
            //目前仅支持单主键自增
            fieldsList.add(sb.toString());
        }

        objectMap.put("fields",fieldsList);

        return objectMap;
    }

    public Map<String,Object> getTableNameMap(){
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put(TABLE_NAME,cscpHxFormTable.getTableName());
        return objectMap;
    }

    public Map<String,Object> getDbTableName(){
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put(TABLE_NAME, cscpHxFormTable.getTableName());
        objectMap.put("dbName", dbName );
        return objectMap;
    }

    public Map<String,Object> getDbTableContent(){
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put(TABLE_NAME, cscpHxFormTable.getTableName());
        objectMap.put("tableContent", cscpHxFormTable.getTableContent() );
        return objectMap;
    }

    public Map<String,Object> getTableColumnName(){
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put(TABLE_NAME,"'" + cscpHxFormTable.getTableName() + "'");
        objectMap.put("columnName","'" + singleCscpHxFormColumn.getColumnName() + "'");
        return objectMap;
    }

    //获取字段类型，
    //目前只对float、double、bigdecimal、char、varchar进行了长度拼接
    private String getFieldType(CscpHxFormColumn cscpHxFormColumn){
        StringBuilder sb = new StringBuilder();
        if (cscpHxFormColumn.getColumnType() ==null) {
            sb.append(MysqlTypeEnum.NUMBER.getName());
            handlerIntLength(cscpHxFormColumn, sb);
        }else {
            switch (cscpHxFormColumn.getColumnType()) {
                case 1:
                    sb.append(MysqlTypeEnum.TINYINT.getName());
                    handlerIntLength(cscpHxFormColumn, sb);
                    break;
                case 2:
                    sb.append(MysqlTypeEnum.SMALLINT.getName());
                    handlerIntLength(cscpHxFormColumn, sb);
                    break;
                case 3:
                    sb.append(MysqlTypeEnum.MEDIUMINT.getName());
                    handlerIntLength(cscpHxFormColumn, sb);
                    break;
                case 4:
                    sb.append(MysqlTypeEnum.INT.getName());
                    handlerIntLength(cscpHxFormColumn, sb);
                    break;
                case 5:
                    sb.append(MysqlTypeEnum.BIGINT.getName());
                    handlerIntLength(cscpHxFormColumn, sb);
                    break;
                case 6:
                    sb.append(MysqlTypeEnum.FLOAT.getName()).append("(").append(cscpHxFormColumn.getColumnLength())
                            .append(",").append(cscpHxFormColumn.getPointLength()).append(")");
                    break;
                case 7:
                    sb.append(MysqlTypeEnum.DOUBLE.getName()).append("(").append(cscpHxFormColumn.getColumnLength())
                            .append(",").append(cscpHxFormColumn.getPointLength()).append(")");
                    break;
                case 8:
                    sb.append(MysqlTypeEnum.DECIMAL.getName()).append("(").append(cscpHxFormColumn.getColumnLength())
                            .append(",").append(cscpHxFormColumn.getPointLength()).append(")");
                    break;
                case 9:
                    sb.append(MysqlTypeEnum.DATE.getName());
                    break;
//            case 10:
//                sb.append(MysqlTypeEnum.TIME.getName());
//                break;
//            case 11:
//                sb.append(MysqlTypeEnum.YEAR.getName());
//                break;
                case 12:
                    sb.append(MysqlTypeEnum.DATETIME.getName());
                    break;
                case 13:
                    sb.append(MysqlTypeEnum.TIMESTAMP.getName());
                    break;
                case 14:
                    appendStringType(cscpHxFormColumn, sb, MysqlTypeEnum.CHAR);
                    break;
                case 15:
                    appendStringType(cscpHxFormColumn, sb, MysqlTypeEnum.VARCHAR);
                    break;
//            case 16:
//                sb.append(MysqlTypeEnum.TINYBLOB.getName());
//                break;
                case 17:
                    sb.append(MysqlTypeEnum.TINYTEXT.getName());
                    break;
//            case 18:
//                sb.append(MysqlTypeEnum.BLOB.getName());
//                break;
                case 19:
                    sb.append(MysqlTypeEnum.TEXT.getName());
                    break;
//            case 20:
//                sb.append(MysqlTypeEnum.MEDIUMBLOB.getName());
//                break;
                case 21:
                    sb.append(MysqlTypeEnum.MEDIUMTEXT.getName());
                    break;
//            case 22:
//                sb.append(MysqlTypeEnum.LONGBLOB.getName());
//                break;
                case 23:
                    sb.append(MysqlTypeEnum.LONGTEXT.getName());
                    break;
                default:
                    log.info("未找到对应字段类型");
            }
        }
        return sb.toString();
    }

    private void handlerIntLength(CscpHxFormColumn cscpHxFormColumn, StringBuilder sb) {
        if(null !=cscpHxFormColumn.getColumnLength()){
            sb.append("(").append(cscpHxFormColumn.getColumnLength()).append(")");
        }
    }

    private void appendStringType(CscpHxFormColumn cscpHxFormColumn, StringBuilder sb, MysqlTypeEnum varchar) {
        if (null != cscpHxFormColumn.getColumnLength()) {
            sb.append(varchar.getName()).append("(").append(cscpHxFormColumn.getColumnLength()).append(")");
        } else {
            sb.append(varchar.getName()).append("(255) ");
        }
    }
}
