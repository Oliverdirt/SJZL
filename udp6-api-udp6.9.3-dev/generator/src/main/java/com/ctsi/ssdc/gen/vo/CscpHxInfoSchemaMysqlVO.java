package com.ctsi.ssdc.gen.vo;

import com.ctsi.ssdc.gen.exception.BusinessException;
import com.ctsi.ssdc.enums.MysqlTypeEnum;
import org.zalando.problem.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 辅助类
 */
public class CscpHxInfoSchemaMysqlVO {

    private static final Pattern COLUMN_TYPE_PATTERN = Pattern.compile(".*int\\((?<num>\\d{1,})\\)");
    /**
     * 辅助字段，存储字段原名，用于判断字段是否被改名
     * 表information_schema中不存在该表
     */
    private String originalTableName;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 库名
     */
    private String tableSchema;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段备注
     */
    private String columnComment;

    /**
     * 字段位置的排序
     */
    private String ordinalPosition;

    /**
     * 字段排序Int
     */
    private Integer ordinalPositionInt;
    /**
     * 是否可以为null
     */
    private String isNullable;

    /**
     * 是否可为空
     */
    private Integer isNullableInt;

    /**
     * 字段类型
     */
    private String dataType;

    /**
     * 字段类型
     */
    private Integer dataTypeInt;


    /**
     * 字段最大字符数
     * 假如字段设置为varchar(50)，那么这一列记录的值就是50。
     *该列只适用于二进制数据，字符，文本，图像数据。其他类型数据比如int，float，datetime等，在该列显示为NULL。
     */
    private String characterMaximumLength;

    /**
     * 字段的最大字节数。
     *和最大字符数一样，只适用于二进制数据，字符，文本，图像数据，其他类型显示为NULL。
     *和最大字符数的数值有比例关系，和字符集有关。比如UTF8类型的表，最大字节数就是最大字符数的3倍。
     */
    private String characterOctetLength;

    /**
     * 类型加长度拼接的字符串，例如varchar(100)
     */
    private String columnType;

    /**
     * 长度
     * 数字精度。
     * 适用于各种数字类型比如int，float之类的。
     * 如果字段设置为int(10)，那么在该列保存的数值是9，少一位。
     * 如果字段设置为float(10,3)，那么在该列保存的数值是10。
     * 非数字类型显示为在该列NULL。
     */
    private String numericPrecision;
    /**
     * 小数点数
     * 和数字精度一样，适用于各种数字类型比如int，float之类。
     * 如果字段设置为int(10)，那么在该列保存的数值是0，代表没有小数。
     * 如果字段设置为float(10,3)，那么在该列保存的数值是3。
     * 非数字类型显示为在该列NULL。
     */
    private String numericScale;

    /**
     * 字段字符集名称。比如utf8。
     */
    private String characterSetName;

    /**
     * 字符集排序规则。
     * 比如utf8_general_ci，是不区分大小写一种排序规则。utf8_general_cs，是区分大小写的排序规则。
     */
    private String collationName;

    /**
     * 索引类型。
     * 可包含的值有PRI，代表主键，UNI，代表唯一键，MUL，可重复。
     */
    private String columnKey;

    /**
     * 字段默认值
     */
    private String columnDefault;

    /**
     * 其他信息
     * 比如主键的auto_increment
     */
    private  String extra;


    /**
     * 字段长度
     */
    private Long length;

    /**
     * 小数点长度
     */
    private Integer pointLength;

    /**
     * 是否主键
     */
    private Integer isKey;


    private Integer isPk;

    public Integer getIsPk() {
        return isPk;
    }

    public void setIsPk(Integer isPk) {
        this.isPk = isPk;
    }

    /**
     * 将数据库属性值进行转换
     */
    public void convertAttribute(){
        this.ordinalPositionInt = Integer.valueOf(this.ordinalPosition);
        this.isNullableInt = this.isNullable.contains("NO")?0:1;
        this.dataTypeInt = MysqlTypeEnum.getNumByName(this.dataType);
        if (null != this.characterMaximumLength){
            try {
                this.length =Long.parseLong(this.characterMaximumLength);
            } catch (NumberFormatException e) {
                throw new BusinessException("数字格式异常！", Status.INTERNAL_SERVER_ERROR, e.getMessage(), "数字格式异常", null);
            }
        }else if (null != this.numericPrecision && "0".equals(this.numericScale )&& this.columnType.matches(".*int\\((?<num>\\d{1,})\\)")){
//            this.length = Long.parseLong(this.numericPrecision) + 1;
//            Pattern pattern = Pattern.compile(".*int\\((?<num>\\d{1,})\\)");
//            Matcher m = pattern.matcher(this.columnType);
            Matcher m = COLUMN_TYPE_PATTERN.matcher(this.columnType);
            m.find();
            this.length = Long.parseLong(m.group("num"));
        }else if (null != this.numericPrecision && null != this.numericScale){
            this.length = Long.parseLong(this.numericPrecision);
            this.pointLength = Integer.parseInt(this.numericScale);
        }
        if (null != this.columnKey){
            this.isKey = this.columnKey.contains("PRI")?1:0;
            this.isPk = this.isKey;
        }
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public Integer getOrdinalPositionInt() {
        return ordinalPositionInt;
    }

    public void setOrdinalPositionInt(Integer ordinalPositionInt) {
        this.ordinalPositionInt = ordinalPositionInt;
    }

    public Integer getIsNullableInt() {
        return isNullableInt;
    }

    public void setIsNullableInt(Integer isNullableInt) {
        this.isNullableInt = isNullableInt;
    }

    public Integer getDataTypeInt() {
        return dataTypeInt;
    }

    public void setDataTypeInt(Integer dataTypeInt) {
        this.dataTypeInt = dataTypeInt;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Integer getPointLength() {
        return pointLength;
    }

    public void setPointLength(Integer pointLength) {
        this.pointLength = pointLength;
    }

    public Integer getIsKey() {
        return isKey;
    }

    public void setIsKey(Integer isKey) {
        this.isKey = isKey;
    }

    public String getOriginalTableName() {
        return originalTableName;
    }

    public void setOriginalTableName(String originalTableName) {
        this.originalTableName = originalTableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(String ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(String characterMaximumLength) {
        this.characterMaximumLength = characterMaximumLength;
    }

    public String getCharacterOctetLength() {
        return characterOctetLength;
    }

    public void setCharacterOctetLength(String characterOctetLength) {
        this.characterOctetLength = characterOctetLength;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getNumericPrecision() {
        return numericPrecision;
    }

    public void setNumericPrecision(String numericPrecision) {
        this.numericPrecision = numericPrecision;
    }

    public String getNumericScale() {
        return numericScale;
    }

    public void setNumericScale(String numericScale) {
        this.numericScale = numericScale;
    }

    public String getCharacterSetName() {
        return characterSetName;
    }

    public void setCharacterSetName(String characterSetName) {
        this.characterSetName = characterSetName;
    }

    public String getCollationName() {
        return collationName;
    }

    public void setCollationName(String collationName) {
        this.collationName = collationName;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
