package com.ctsi.ssdc.customize.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.ctsi.ssdc.customize.constant.CscpCustomizeConstant;
import com.ctsi.ssdc.customize.domain.*;
import com.ctsi.ssdc.gen.domain.TablePkMessage;
import com.ctsi.ssdc.gen.domain.TemplateEntity;
import com.ctsi.ssdc.gen.repository.CscpCustomizeTemplateRepository;
import com.ctsi.ssdc.customize.repository.CscpCustomizeVpageRepository;
import com.ctsi.ssdc.customize.repository.CscpListModelDesignRepository;
import com.ctsi.ssdc.customize.service.CscpHxDTableManagerService;
import com.ctsi.ssdc.customize.service.CscpListModelDesignService;
import com.ctsi.ssdc.enums.QueryTypeEnum;
import com.ctsi.ssdc.model.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/8/26 9:55  by xx
 */
@Service
@Slf4j
public class CscpListModelDesignServiceImpl implements CscpListModelDesignService {
    @Value("${spring.datasource.db-name}")
    private String dbName;
    @Resource
    private DataSource dataSource;

    @Resource
    private CscpListModelDesignRepository cscpListModelDesignRepository;

    @Resource
    private CscpHxDTableManagerService cscpHxTableManagerService;

    @Resource
    private CscpCustomizeVpageRepository cscpCustomizeVpageRepository;

    @Resource
    private CscpCustomizeTemplateRepository customizeTemplateRepository;

    @Override
    public int checkFormTable(String tableName) {
        return cscpListModelDesignRepository.selectCount(tableName);
    }

    @Override
    public int updateModelInfo(Long tableId,String tableName) {
        return cscpListModelDesignRepository.updateModelInfo(tableId.toString(),tableName);
    }

    @Override
    public Map qryDataBaseInfo() {
        DruidDataSource druidDataSource = (DruidDataSource)dataSource;
        String url = druidDataSource.getUrl();
        Map<String,Object> map = new HashMap<>();
        String targetUrl = url.substring(0, url.indexOf("?"));
        int i = targetUrl.indexOf("//");
        int j = targetUrl.lastIndexOf(":");
        int k = targetUrl.lastIndexOf("/");
        String ip = url.substring(i+2, j);
        String port = url.substring(j+1, k);
        map.put("ipAddress",ip);
        map.put("port",port);
        map.put("dbName",dbName);
        map.put("username",druidDataSource.getUsername());
        return map;
    }


    @Override
    public Map deleteTable(CscpHxDformTable cscpHxFormTable) {
        Map<String,Object> respMap = new HashMap<>();
        // 根据tableId查询表名
        String tableName = cscpListModelDesignRepository.qryTableNameByTableId(String.valueOf(cscpHxFormTable.getTableId()));
        // 判断表中是否存在数据
        int i=0;
        try {
            i = cscpListModelDesignRepository.qryDateByTableName(tableName);
        }catch (Exception e){
        log.debug("数据表不存在");
    }
        cscpHxFormTable.setTableName(tableName);
        if (i > 0) {
            respMap.put("code","1");
            respMap.put("message","表中有数据，删除失败");
        }else {
            if (StringUtils.isNotEmpty(cscpHxFormTable.getTableName())) {
                cscpHxTableManagerService.deleteTableIfExists(cscpHxFormTable);
            }
            respMap.put("code","0");
            respMap.put("message","删除成功");
        }
        return respMap;

    }

    @Override
    public PageResult<Map> queryTemplatePage(Long pageId, TemplateEntity templateEntity, Pageable pageable) {
        CscpCustomizeVpage cscpCustomizeVpage = cscpCustomizeVpageRepository.selectByPrimaryKey(pageId);
        String tableName = cscpCustomizeVpage.getPageTable();
        List<String> queryFields = templateEntity.getQueryFields();
        List<String> queryTypes = templateEntity.getQueryTypes();
        List<Object> queryValues = templateEntity.getQueryValues();
        //查询条件为空
        List<Map> maps = null;
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("tableName",tableName);
        //设置排序字段 默认根据主键排序
        List<String> tableNameList = new ArrayList<>();
        List<String> tableSchemaList = new ArrayList<>();
        tableNameList.add(cscpCustomizeVpage.getPageTable());
        tableSchemaList.add(dbName);
        queryMap = this.buildOrderParam(tableNameList, tableSchemaList, queryMap,templateEntity);
        if (queryFields == null || queryFields.size() == 0){
            PageHelper.startPage(pageable.getPageNumber(),pageable.getPageSize());
            maps = cscpListModelDesignRepository.queryAllPage(queryMap);

        }else{
            // 构建查询条件
            StringBuilder stringBuilder = new StringBuilder();
            //查询条件非空
            for (int i = 0; i <= queryFields.size()-1; i++) {
                int queryType = Integer.parseInt(String.valueOf(queryTypes.get(i)));
                if(QueryTypeEnum.getByNum(queryType).getSign().equals(CscpCustomizeConstant.BETWEEN)){
                    String vals = (String) queryValues.get(i);
                    String[] arrVal = vals.split(CscpCustomizeConstant.QUERY_FIELD_SEPARATOR);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(queryFields.get(i));
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.GREATEROREQUALTHAN);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append("'"+arrVal[0]+"'");
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.AND);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(queryFields.get(i));
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.LESSOREQUALTHAN);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append("'"+arrVal[1]+"'");
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.AND);
                }else if(QueryTypeEnum.getByNum(queryType).getSign().equals(CscpCustomizeConstant.LIKE)){
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(queryFields.get(i));
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.LIKE);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.CONCAT);
                    stringBuilder.append("(\"%\",\"");
                    stringBuilder.append(queryValues.get(i));
                    stringBuilder.append("\",\"%\")");
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.AND);
                }else{
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(queryFields.get(i));
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(QueryTypeEnum.getByNum(queryType).getSign());
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append("'"+queryValues.get(i)+"'");
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.AND);
                }
            }
            queryMap.put("queryParams",stringBuilder.substring(0,stringBuilder.length()-4));
            PageHelper.startPage(pageable.getPageNumber(),pageable.getPageSize());
            maps = cscpListModelDesignRepository.queryTemplatePage(queryMap);
        }
        PageInfo<Map> pageInfo = new PageInfo<>(maps);
        PageResult<Map> pageResult = new PageResult<>();
        pageResult.setData(pageInfo.getList());
        pageResult.setRecordsTotal(pageInfo.getTotal());
        return pageResult;
    }

    /**
     * 排序字段设置
     * @author wbb
     * @param tableNameList 表名称列表
     * @param tableSchemaList 实例名称列表
     * @param queryMap 参数map
     * @param templateEntity 页面查询参数
     * @return 排序及查询参数
     */
    private Map<String,Object> buildOrderParam(List<String> tableNameList,
                                              List<String> tableSchemaList,
                                              Map<String,Object> queryMap,
                                              TemplateEntity templateEntity){
        //查询业务表主键以map形式返回 key 值为实例+表名  value 为 主键用逗号隔开
        Map<String,String> pkMesageMap = this.getPkColumn(tableNameList,tableSchemaList);
        StringBuilder orderColumn = new StringBuilder("1");
        //设置前台传入的排序字段
        if(templateEntity.getSort() != null && templateEntity.getSort().length > 0){
            String[] sort = templateEntity.getSort();
            for(String s : sort){
                orderColumn.append(",");
                orderColumn.append(s);

            }
        }
        //实例加表明为主键key 值
        String key = dbName + queryMap.get("tableName");
        if(pkMesageMap.containsKey(key) &&
                StringUtils.isNotEmpty(pkMesageMap.get(key))){
            orderColumn.append(",");
            //默认根据主键排序
            orderColumn.append(pkMesageMap.get(key));
        }
        //如果存在排序字段 且排序规则不为空 增加排序字段
        String orderByClause = templateEntity.getOrderByClause();
        if(StringUtils.isNotEmpty(orderByClause) &&
                StringUtils.isNotEmpty(orderColumn.toString())){
            orderColumn.append(" ");
            orderColumn.append(orderColumn);
        }
        if(StringUtils.isNotEmpty(orderColumn.toString())){
            queryMap.put("orderColumn",orderColumn.toString());
        }
        return queryMap;
    }

    /**
     * 获取业务表主键字段
     * @author wbb
     * @param tableNameList 表名称
     * @param tableSchemaList 实例名称
     * @return 表名称和实例主键名称map值 联合主键用逗号隔开
     */
    private Map<String,String> getPkColumn(List<String> tableNameList, List<String> tableSchemaList) {
        if(CollectionUtils.isEmpty(tableNameList) ||
                CollectionUtils.isEmpty(tableSchemaList)){
            return new HashMap<>();
        }
        //查询表的主键信息
        List<TablePkMessage> tablePkMessageList = cscpListModelDesignRepository.queryTablePkList(tableNameList,tableSchemaList);
        if(CollectionUtils.isEmpty(tablePkMessageList)){
            return new HashMap<>();
        }
        //转成map的形式 实例加表名为key值 主键为 value值
        return tablePkMessageList.stream().collect(Collectors.toMap(it->it.getTableSchema() + it.getTableName(),TablePkMessage :: getPkColumn));
    }


    @Override
    public Boolean importExcelData(MultipartFile file,TemplateEntity templateEntity) {
        // 判断主表是否有数据 如果只导入子表则抛异常 todo 根据tableId和tableName isFk=1 查询列表
//        boolean chekMainTableAndFkDateNotExist = this.checkMainTableOrChidFkNotExistsData(1L,new HashMap<>());
//        if(chekMainTableAndFkDateNotExist){
//            throw new BadRequestAlertException("添加数据错误：子表添加主表没有数据或外键数据没有传入","CscpListModelDesignServiceImpl.importExcelData","");
//        }
        List<List<Object>> valuesList = new ArrayList<>();
        try {
            // 通过文件输入流读取到对应的 workbook 工作簿
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            // 只解析第一张 sheet 工作表
            XSSFSheet sheet = workbook.getSheetAt(0);
            // 遍历第一个工作表的所有行
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                if (i == 0) {
                    // 跳过标题行
                    continue;
                }
                XSSFRow row = sheet.getRow(i); // 获取工作表中的某一行，通过下标获取
                if (row == null) {
                    // 跳过空行
                    continue;
                }
                // 构造要批量插入的Student对象
                List<Object> values = new ArrayList<>();

                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    // 获取一行中的某个单元格，通过下标获取
                    XSSFCell cell = row.getCell(j);
                    // 跳过空单元格
                    if (cell == null) {
                        continue;
                    }
                    // 判断单元格是第几个，从零开始
                    String cellValue = getCellValue(cell, workbook);
                    values.add(cellValue);
                }
                valuesList.add(values);
            }
            templateEntity.setValuesList(valuesList);
            // 批量新增
            customizeTemplateRepository.addTemplateBatch(templateEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("导入失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚代码
            return false;
        }
    }

    @Override
    public List<Long> selectChildTable(Long mainTableId) {
        return cscpListModelDesignRepository.selectChildTable(mainTableId);
    }


    private String getCellValue(XSSFCell cell,XSSFWorkbook workbook){
        String  data="";
        //判断类型
        CellType cellTypeEnum = cell.getCellTypeEnum();//类型
        switch (cellTypeEnum){
            case _NONE://无
                data = null;
                break;
            case BLANK://空值
                data = "";
                break;
            case ERROR://错误
                data = "错误";
                break;
            case STRING://字符串
                data = cell.getStringCellValue();
                break;
            case BOOLEAN://布尔
                data = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA://公式

                XSSFFormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator(workbook);//后面使用它来执行计算公式
                String cellFormula1 = cell.getCellFormula();//获得公式
                CellValue evaluate = formulaEvaluator.evaluate(cell);//执行公式.获取字符串值
                data=evaluate.getStringValue();
                if (data == null){//如果获得值为空，可能是数字公式
                    String cellFormula2 = cell.getCellFormula();//获得公式
                    double value = cell.getNumericCellValue();//获取公式的计算值
                    String s1 = new DecimalFormat("#.###########").format(value);//格式化数字
                    data = new BigDecimal(s1).stripTrailingZeros().toPlainString();//去后面的0
                }
                break;
            case NUMERIC://数字 （日期、普通数字）

                if (DateUtil.isCellDateFormatted(cell)){//如果是日期
                    Date date =cell.getDateCellValue();//获得日期
                    data = new SimpleDateFormat("yyyy/MM/dd").format(date);//格式化日期

                }else {//如果不是日期，防止数字过长,转换格式
                    String s2 = new DecimalFormat("#.###########").format(cell.getNumericCellValue());//格式化数字
                    data = new BigDecimal(s2).stripTrailingZeros().toPlainString();//去后面的0
                }
                break;
            default:
                data = "";
                break;
        }//获得数据
        return data;
    }

    /**
     * 校验是否为子表添加 如果为子表添加校验是否传入了外键数据
     * @author wbb
     * @param formId 子表formid
     * @param addMap 添加map
     * @return 是否没有主表数据或外键值是否传入 true 没有主表数据或外键值没有传入 false 有主表数据或外键值有传入
     */
    private boolean checkMainTableOrChidFkNotExistsData(Long formId, Map<String , Object> addMap) {
        //根据formId查询子表表单信息
        CscpCustomizeVpage childPage = cscpCustomizeVpageRepository.selectByPrimaryKey(formId);
        //mainFormId为空视为单表
        if(childPage.getMainPageId() == null){
            return false;
        }
        //外键字段
        String fk = childPage.getSubPageFk().replaceAll("_","");
        //判断是否传入外键值
        Set<Map.Entry<String, Object>> entrySet = addMap.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if(entry.getKey() != null &&
                    StringUtils.equalsIgnoreCase(entry.getKey().replaceAll("_",""),fk) &&
                    addMap.get(entry.getKey()) != null){
                return false;
            }
        }
        return true;
    }

}
