package com.ctsi.ssdc.gen.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 代码生成通用常量
 *
 * @author hx
 */
public class GenConstants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";
    public static String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 单表（增删改查）
     */
    public static final String TPL_CRUD = "crud";

    /**
     * 树表（增删改查）
     */
    public static final String TPL_TREE = "tree";

    /**
     * 主子表（增删改查）
     */
    public static final String TPL_SUB = "sub";

    /**
     * 树编码字段
     */
    public static final String TREE_CODE = "treeCode";

    /**
     * 树父编码字段
     */
    public static final String TREE_PARENT_CODE = "treeParentCode";

    /**
     * 树名称字段
     */
    public static final String TREE_NAME = "treeName";

    /**
     * 上级菜单ID字段
     */
    public static final String PARENT_MENU_ID = "parentMenuId";

    /**
     * 上级菜单名称字段
     */
    public static final String PARENT_MENU_NAME = "parentMenuName";

    /**
     * 数据库字符串类型
     */
    private static String[] COLUMNTYPE_STR = {"char", "varchar", "nvarchar", "varchar2"};

    public static final String[] columntypeStr() {
        return COLUMNTYPE_STR.clone();
    }

    /**
     * 数据库文本类型
     */
    private static String[] COLUMNTYPE_TEXT = {"tinytext", "text", "mediumtext", "longtext"};

    public static final String[] columntypeText() {
        return COLUMNTYPE_TEXT.clone();
    }

    /**
     * 数据库时间类型
     */
    private static String[] COLUMNTYPE_TIME = {"datetime", "time", "date", "timestamp"};

    public static final String[] columntypeTime() {
        return COLUMNTYPE_TIME.clone();
    }

    /**
     * 数据库数字类型
     */
    private static final String[] COLUMNTYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "double", "decimal"};

    public static final String[] columntypeNumber() {
        return COLUMNTYPE_NUMBER.clone();
    }

    /**
     * 页面不需要编辑字段
     */
    private static final String[] COLUMNNAME_NOT_EDIT = {"id", "create_by", "create_time", "del_flag"};

    public static final String[] columntypeNotEdit() {
        return COLUMNNAME_NOT_EDIT.clone();
    }

    /**
     * 页面不需要显示的列表字段
     */
    private static final String[] COLUMNNAME_NOT_LIST = {"id", "create_by", "create_time", "del_flag", "update_by",
            "update_time"};

    public static final String[] columntypeNotList() {
        return COLUMNNAME_NOT_LIST.clone();
    }

    /**
     * 页面不需要查询字段
     */
    private static final String[] COLUMNNAME_NOT_QUERY = {"id", "create_by", "create_time", "del_flag", "update_by",
            "update_time", "remark"};

    public static final String[] columntypeNotQuery() {
        return COLUMNNAME_NOT_QUERY.clone();
    }

    /**
     * Entity基类字段
     */
    private static final String[] BASE_ENTITY = {"createBy", "createTime", "updateBy", "updateTime", "remark"};

    public static final String[] baseEntity() {
        return BASE_ENTITY.clone();
    }

    /**
     * Tree基类字段
     */
    private static final String[] TREE_ENTITY = {"parentName", "parentId", "orderNum", "ancestors", "children"};

    public static final String[] treeEntity() {
        return TREE_ENTITY.clone();
    }

    /**
     * 文本框
     */
    public static final String HTML_INPUT = "input";

    /**
     * 文本域
     */
    public static final String HTML_TEXTAREA = "textarea";

    /**
     * 下拉框
     */
    public static final String HTML_SELECT = "select";

    /**
     * 单选框
     */
    public static final String HTML_RADIO = "radio";

    /**
     * 复选框
     */
    public static final String HTML_CHECKBOX = "checkbox";

    /**
     * 日期控件
     */
    public static final String HTML_DATETIME = "datetime";

    /**
     * 图片上传控件
     */
    public static final String HTML_IMAGE_UPLOAD = "imageUpload";

    /**
     * 文件上传控件
     */
    public static final String HTML_FILE_UPLOAD = "fileUpload";

    /**
     * 富文本控件
     */
    public static final String HTML_EDITOR = "editor";

    /**
     * 字符串类型
     */
    public static final String TYPE_STRING = "String";

    /**
     * 整型
     */
    public static final String TYPE_INTEGER = "Integer";

    /**
     * 长整型
     */
    public static final String TYPE_LONG = "Long";

    /**
     * 浮点型
     */
    public static final String TYPE_DOUBLE = "Double";

    /**
     * 高精度计算类型
     */
    public static final String TYPE_BIGDECIMAL = "BigDecimal";

    /**
     * 时间类型
     */
    public static final String TYPE_DATE = "Date";
    /**
     * 时间类型
     */
    public static final String TYPE_ZONED_DATE_TIME = "ZonedDateTime";

    /**
     * 模糊查询
     */
    public static final String QUERY_LIKE = "LIKE";

    /**
     * 需要
     */
    public static final String REQUIRE = "1";

    /**
     * 内置模板前缀
     */
    public static final String VM = "vm";


    /**
     * 单表模板名列表
     */
    public static final List<String> SINGLE_TABLE_TEMPLATE_LIST;

    static {
        ArrayList<String> tempList = new ArrayList<>(Arrays.asList("Controller.java.vm", "Domain.java.vm",
                "DomainExample.java.vm", "Service.java.vm", "ServiceImpl.java.vm",
                "Repository.java.vm", "SqlProvider.java.vm", "SingleForm.vue.vm", "Mapper.java.vm", "Mapper.xml.vm"));
        SINGLE_TABLE_TEMPLATE_LIST = Collections.unmodifiableList(tempList);
    }

    /**
     * 主子表模板名列表
     */
    public static final List<String> MAIN_SUB_TABLE_TEMPLATE_LIST;

    static {
        ArrayList<String> subTempList = new ArrayList<>(Arrays.asList("Controller.java.vm", "Domain.java.vm",
                "DomainExample.java.vm", "Service.java.vm", "ServiceImpl.java.vm", "Repository.java.vm",
                "SqlProvider.java.vm", "MainForm.vue.vm", "SubForm.vue.vm", "SubController.java.vm",
                "SubDomain.java.vm", "SubDomainExample.java.vm", "SubService.java.vm", "SubServiceImpl.java.vm",
                "SubRepository.java.vm", "SubSqlProvider.java.vm", "Mapper.java.vm", "Mapper.xml.vm", "SubMapper.java.vm", "SubMapper.xml.vm"));
        MAIN_SUB_TABLE_TEMPLATE_LIST = Collections.unmodifiableList(subTempList);
    }

    /**
     * 树表模板名列表
     */
    public static final List<String> TREE_TABLE_TEMPLATE_LIST;

    static {
        ArrayList<String> treeTempList = new ArrayList<>(Arrays.asList("Controller.java.vm", "Domain.java.vm",
                "DomainExample.java.vm", "Service.java.vm", "ServiceImpl.java.vm", "Repository.java.vm",
                "SqlProvider.java.vm", "TreeForm.vue.vm", "Mapper.java.vm", "Mapper.xml.vm"));
        TREE_TABLE_TEMPLATE_LIST = Collections.unmodifiableList(treeTempList);
    }
}
