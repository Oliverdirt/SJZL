package com.ctsi.ssdc.customize.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.customize.constant.CscpCustomizeConstant;
import com.ctsi.ssdc.customize.domain.*;
import com.ctsi.ssdc.customize.service.CscpCustomizeVpageService;
import com.ctsi.ssdc.gen.domain.CscpHxInfoSchemaMysql;
import com.ctsi.ssdc.gen.domain.TablePkMessage;
import com.ctsi.ssdc.gen.domain.TemplateEntity;
import com.ctsi.ssdc.gen.repository.CscpCustomizeTemplateRepository;
import com.ctsi.ssdc.customize.repository.CscpCustomizeVpageRepository;
import com.ctsi.ssdc.customize.service.CscpCustomizePageTemplateService;
import com.ctsi.ssdc.enums.QueryTypeEnum;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.gen.service.CscpHxInfoSchemaMysqlService;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.SnowIdUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CscpCustomizePageTemplateServiceImpl implements CscpCustomizePageTemplateService {
    @Resource
    private CscpCustomizeTemplateRepository customizeTemplateRepository;

    @Resource
    private CscpCustomizeVpageRepository cscpCustomizeVpageRepository;

    @Value("${spring.datasource.db-name}")
    private String dbName;

    @Resource
    private CscpHxInfoSchemaMysqlService informationSchemaMysqlService;
    @Resource
    private CscpCustomizeVpageService cscpCustomizeVpageService;

    @Override
    public PageResult<Map> queryTemplatePage(Long pageId, TemplateEntity templateEntity, Pageable pageable) {
        CscpCustomizeVpage customizeVpage = cscpCustomizeVpageRepository.selectByPrimaryKey(pageId);
        String tableName = customizeVpage.getPageTable();
        List<String> queryFields = templateEntity.getQueryFields();
        List<String> queryTypes = templateEntity.getQueryTypes();
        List<Object> queryValues = templateEntity.getQueryValues();
        //查询条件为空
        List<Map> maps = null;
        Map<String,Object> queryMap = new HashMap();
        queryMap.put("tableName",tableName);
        //设置排序字段 默认根据主键排序
        List<String> tableNameList = new ArrayList<>();
        List<String> tableSchemaList = new ArrayList<>();
        tableNameList.add(customizeVpage.getPageTable());
        tableSchemaList.add(dbName);
        queryMap = this.buildOrderParam(tableNameList, tableSchemaList, queryMap,templateEntity);
        if (queryFields == null || queryFields.size() == 0){
            PageHelper.startPage(pageable.getPageNumber(),pageable.getPageSize());
            maps = customizeTemplateRepository.queryAllPage(queryMap);

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
                    stringBuilder.append("'").append(arrVal[0]).append("'");
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.AND);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(queryFields.get(i));
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.LESSOREQUALTHAN);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append("'").append(arrVal[1]).append("'");
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
                    stringBuilder.append("'").append(queryValues.get(i)).append("'");
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.AND);
                }
            }
            queryMap.put("queryParams",stringBuilder.substring(0,stringBuilder.length()-4));
            PageHelper.startPage(pageable.getPageNumber(),pageable.getPageSize());
            maps = customizeTemplateRepository.queryTemplatePage(queryMap);
        }
        PageInfo<Map> pageInfo = new PageInfo<>(maps);
        PageResult<Map> pageResult = new PageResult<>();
        pageResult.setData(pageInfo.getList());
        pageResult.setRecordsTotal(pageInfo.getTotal());
        return pageResult;
    }


    @Override
    public PageResult<Map> queryTemplatePageBatch(Long pageId, TemplateEntity templateEntity, Pageable pageable) {
        CscpCustomizeVpage customizeVpage = cscpCustomizeVpageRepository.selectByPrimaryKey(pageId);
        //表名获取
        String[] tableNames = customizeVpage.getPageTable().split(",");
        List<String> queryFields = templateEntity.getQueryFields();
        List<String> queryTypes = templateEntity.getQueryTypes();
        List<Object> queryValues = templateEntity.getQueryValues();
        //查询条件为空
        List<Map> maps = null;
        Map<String,Object> queryMap = new HashMap();
        //查询表属性
        List<CscpHxInfoSchemaMysql> cscpHxInfoSchemaMysqlList = informationSchemaMysqlService.getTableFieldInfoByMapBatch(Arrays.asList(tableNames.clone()), dbName);
        Map<String,List<CscpHxInfoSchemaMysql>> cscpHxInfoSchemaMysqlListMap = cscpHxInfoSchemaMysqlList.stream().collect(Collectors.groupingBy(CscpHxInfoSchemaMysql::getTableName));
        StringBuilder sqlHeader = new StringBuilder("");
        // 设置表头
        int mark = 0;
        for(String s : tableNames){
            //设置查询表头
            List<CscpHxInfoSchemaMysql> tempColumnList = cscpHxInfoSchemaMysqlListMap.get(s);
            for(CscpHxInfoSchemaMysql cs :  tempColumnList){
                if(mark == 0){
                    sqlHeader.append(" "+s + "." + cs.getColumnName() + " as " + s + "_table" + cs.getColumnName() + " ");
                }else{
                    sqlHeader.append(" ,"+s + "." + cs.getColumnName() + " as " + s + "_table" + cs.getColumnName() + " ");
                }
                mark ++;
            }
        }
        StringBuilder conditions = new StringBuilder(" where 1 = 1 ");
        StringBuilder tableNamesStr = new StringBuilder(" ");
        //多表时设置关联字段设置关联关系
        if(tableNames.length > 1){
            List<CscpHxDformColumn>  dcList = cscpCustomizeVpageService.selectRelationColumnByTableNames(Arrays.asList(tableNames));
            if(CollectionUtils.isEmpty(dcList)){
                return null;
            }
            Map<String,List<CscpHxDformColumn>> cdListMap = dcList.stream().collect(Collectors.groupingBy(CscpHxDformColumn :: getTableName));
            //只能有一张主表
            if(cdListMap.size() < tableNames.length - 1 ){
                return null;
            }
            //获取主表
            String mainTableName = null;
            for(String s : tableNames){
                if(CollectionUtils.isEmpty(cdListMap.get(s))){
                    mainTableName = s;
                    tableNamesStr.append(s+" ");
                    break;
                }
            }
            StringBuilder orderByCase = new StringBuilder("");
            //拼接主子表关联条件
            Set<Map.Entry<String ,List<CscpHxDformColumn>>> etrs = cdListMap.entrySet();
            //现根据主键id排序倒序
            orderByCase.append(" " + mainTableName + ".id DESC ");
            for(Map.Entry<String, List<CscpHxDformColumn>> entry : etrs){
                List<CscpHxDformColumn> tempCdList = entry.getValue();
                if(CollectionUtils.isEmpty(tempCdList)){
                    continue;
                }
                //设置关联表
                tableNamesStr.append(" left join " +  entry.getKey() );
                int fg = 0;
                //设置关联条件
                for(CscpHxDformColumn cd : tempCdList){
                    orderByCase.append(" ," + mainTableName + "." + cd.getColumnRelation() + " DESC ");
                    if(fg == 0){
                        tableNamesStr.append(" on " +   mainTableName + "." + cd.getColumnRelation() + " = " + entry.getKey() + "." + cd.getColumnName() + " ");
                    }else{
                        tableNamesStr.append(" and " +   mainTableName + "." + cd.getColumnRelation() + " = " + entry.getKey() + "." + cd.getColumnName() + " ");
                    }
                    fg ++ ;
                }
            }
            //如果没有给排序规则默认根据关联字段 主键 最后更新时间
            if(StringUtils.isEmpty(templateEntity.getOrderByClause())){
                templateEntity.setOrderByClause(orderByCase + " ");
            }
        }
        //单表场景
        if(StringUtils.isEmpty((tableNamesStr+"").trim())){
            tableNamesStr.append(tableNames[0] + " ");
            //设置排序字段
            if(StringUtils.isEmpty(templateEntity.getOrderByClause())){
                templateEntity.setOrderByClause(" " + tableNames[0] + ".id desc ");
            }
        }
        //设置查询条件
        if(CollectionUtils.isNotEmpty(queryFields)) {
            // 构建查询条件
            StringBuilder stringBuilder = new StringBuilder();
            //查询条件非空
            for (int i = 0; i <= queryFields.size() - 1; i++) {
                int queryType = Integer.parseInt(String.valueOf(queryTypes.get(i)));
                if (QueryTypeEnum.getByNum(queryType).getSign().equals(CscpCustomizeConstant.BETWEEN)) {
                    String vals = (String) queryValues.get(i);
                    String[] arrVal = vals.split(CscpCustomizeConstant.QUERY_FIELD_SEPARATOR);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.AND);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(queryFields.get(i));
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.GREATEROREQUALTHAN);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append("'").append(arrVal[0]).append("'");
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.AND);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(queryFields.get(i));
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.LESSOREQUALTHAN);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append("'").append(arrVal[1]).append("'");
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                } else if (QueryTypeEnum.getByNum(queryType).getSign().equals(CscpCustomizeConstant.LIKE)) {
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.AND);
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
                } else {
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(CscpCustomizeConstant.AND);
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(queryFields.get(i));
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append(QueryTypeEnum.getByNum(queryType).getSign());
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                    stringBuilder.append("'").append(queryValues.get(i)).append("'");
                    stringBuilder.append(CscpCustomizeConstant.SPACE);
                }
                conditions.append(stringBuilder);
            }
        }
        //设置查询表
        queryMap.put("tableName",tableNamesStr);
        //设置查询条件
        queryMap.put("conditions",conditions);
        //设置查询表头
        queryMap.put("sqHeader",sqlHeader);
        //设置排序
        queryMap.put("orderByClause",templateEntity.getOrderByClause());
        //设置开始下标
        queryMap.put("startIndex",(pageable.getPageNumber() - 1) * pageable.getPageSize());
        //设置结束下标
        queryMap.put("endIndex",(pageable.getPageNumber()) * pageable.getPageSize());
        //查询当前页数据
        maps = customizeTemplateRepository.queryAllPageSqls(queryMap);
        // 查询总条数
        int count = customizeTemplateRepository.queryAllCount(queryMap);
        List<Map> tempMaps = new ArrayList<>();
        //加工结果集 生成表名点字段名形式
        for(Map mp : maps){
            Set<Map.Entry<String, Object>> entries = mp.entrySet();
            Map<String,Object> tempMp = new HashMap<>();
            for (Map.Entry<String, Object> entry : entries) {
                String tempKey = entry.getKey();
                for(String ts : tableNames){
                    if(tempKey.contains(ts+"_table")){
                        tempKey = tempKey.replaceAll(ts+"_table",ts+".");
                        break;
                    }
                }
                tempMp.put(tempKey,entry.getValue());
            }
            tempMaps.add(tempMp);
        }
        maps = tempMaps;
        PageInfo<Map> pageInfo = new PageInfo<>(maps);
        PageResult<Map> pageResult = new PageResult<>();
        pageResult.setData(pageInfo.getList());
        pageResult.setRecordsTotal(count);
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
    public Map<String,Object> buildOrderParam(List<String> tableNameList,
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

    @Override
    public Map queryTemplate(Long formId, Map<String , Object> queryMap) {
        queryMap = bulidQueryPkWhere(formId, queryMap,"idQuery");
        return customizeTemplateRepository.queryTemplate(queryMap);
    }

    @Override
    public int delTemplate(Long formId, Map<String, Object> delMap) {
        // 根据formId获取业务表名
//        delMap = bulidQueryPkWhere(formId, delMap,"idDel");
//        return customizeTemplateRepository.delTemplate(delMap);
        return this.batchDelTemplate(formId,delMap);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelTemplate(Long pageId,Map<String, Object> delMap) {
        //查询主表单信息
        CscpCustomizeVpage customizeVpage = cscpCustomizeVpageRepository.selectByPrimaryKey(pageId);
        //获取主表业务表名称
        String[] tables =  customizeVpage.getPageTable().split(",");
        //获取表名
        String tableName = customizeVpage.getPageTable();
        //如果是多张表 获取主表名
        if(tables.length > 1){
            List<CscpHxDformColumn>  dcList = cscpCustomizeVpageService.selectRelationColumnByTableNames(Arrays.asList(tables));
            Map<String,List<CscpHxDformColumn>> cdListMap = dcList.stream().collect(Collectors.groupingBy(CscpHxDformColumn :: getTableName));
            //获取主表
            for(String s : tables){
                if(CollectionUtils.isEmpty(cdListMap.get(s))){
                    tableName = s;
                    break;
                }
            }
        }
        // 获取主键信息
        String pkFeild = this.getPkColumn(tableName,dbName);
        //这里兼容单条和批量 后面页面传参确定统一修改
        Object pkFieldVal = delMap.get(pkFeild+"s") == null ? delMap.get(pkFeild) : delMap.get(pkFeild+"s");
        //表字段拼接成页面传参形式
        if(StringUtils.isEmpty((String)pkFieldVal)){
            throw new BadRequestAlertException("没有选择要删除的数据","ids is null","");
        }else{
            //表字段拼接成页面传参形式
            String ids = String.valueOf(pkFieldVal);
            //子表数据删除
            this.delChild(pageId,pkFeild,ids,customizeVpage);
            //主表数据删除
            delMap.put("idsDel",pkFeild + " in (" +ids + ")");
            delMap.put("tableName",tableName);
            return customizeTemplateRepository.batchDelTemplate(delMap);
        }
    }

    /**
     * 获取业务表主键字段
     * @author wbb
     * @param tableNameList 表名称
     * @param tableSchemaList 实例名称
     * @return 表名称和实例主键名称map值 联合主键用逗号隔开
     */
    @Override
    public Map<String,String> getPkColumn(List<String> tableNameList, List<String> tableSchemaList) {
        if(CollectionUtils.isEmpty(tableNameList) ||
                CollectionUtils.isEmpty(tableSchemaList)){
            return new HashMap<>();
        }
        //插询表的主键信息
        List<TablePkMessage> tablePkMessageList = customizeTemplateRepository.queryTablePkList(tableNameList,tableSchemaList);
        if(CollectionUtils.isEmpty(tablePkMessageList)){
            return new HashMap<>();
        }
        List<TablePkMessage> resultList = new ArrayList<>();
        for(TablePkMessage tm : tablePkMessageList){
            if(tm != null){
                resultList.add(tm);
            }
        }
        if(CollectionUtils.isEmpty(resultList)){
            return new HashMap<>();
        }
        //转成map的形式 实例加表名为key值 主键为 value值
        return resultList.stream().collect(Collectors.toMap(it->it.getTableSchema() + it.getTableName(),TablePkMessage :: getPkColumn));
    }

    /**
     * 删除子页面信息
     * @author wbb
     * @param pageId 主表模块id
     * @param pkFeild 主表主键
     * @param ids 页面传入主表id
     * @param customizeVpage 主表form表单信息
     */
    private void delChild(Long pageId,String pkFeild,String ids,CscpCustomizeVpage customizeVpage){
        //子表查询参数
        List<Long> mainPageIdList = new ArrayList<>();
        mainPageIdList.add(pageId);
        //根据主表fromId查询子表表单信息
        List<CscpCustomizeVpage> childCustomizeVpages = cscpCustomizeVpageRepository.getCscpCustomizeVpagesByMainPageIds(mainPageIdList);
        //批量删除子表数据
        if(CollectionUtils.isEmpty(childCustomizeVpages)){
            return;
        }
        Map<String,Object> childDelMap = new HashMap<>();
        for(CscpCustomizeVpage page : childCustomizeVpages){
            List<CscpHxDformColumn> dcList = cscpCustomizeVpageService.selectRelationColumnByPageId(page.getPageId());
            if(CollectionUtils.isEmpty(dcList)){
                return;
            }
            childDelMap.clear();
            //t1为主表信息 t2为子表信息 这里删除的是 t2表信息
            childDelMap.put("mainTableIds"," and t1." + pkFeild + " in (" +ids + ") ");
            childDelMap.put("tableNames",customizeVpage.getPageTable() + " t1, " + page.getPageTable() + " t2 ");
            StringBuilder fkRelationShip = new StringBuilder("");
            int mark = 0;
            for(CscpHxDformColumn cd : dcList){
                if(mark == 0){
                    fkRelationShip.append(" t1." + cd.getColumnRelation() + " = t2." + cd.getColumnName() + " ");
                }else{
                    fkRelationShip.append(" and  t1." +  cd.getColumnRelation()  + " = t2." +cd.getColumnName() + " ");
                }
                mark ++;
            }
            childDelMap.put("fkRelationShip",fkRelationShip);
            customizeTemplateRepository.batchDelChildTemplate(childDelMap);
        }
    }

    private Map<String, Object> bulidQueryPkWhere(Long formId, Map<String, Object> queryMap,String operation) {
        CscpCustomizeVpage customizeVpage = cscpCustomizeVpageRepository.selectByPrimaryKey(formId);
        String tableName = customizeVpage.getPageTable();
        // 获取主键信息
        String formOption = customizeVpage.getPageOption();
        JSONObject jsonObject = JSONObject.parseObject(formOption);
        String pkFeild = jsonObject.getString(CscpCustomizeConstant.PK);
        Long pkVal = Long.parseLong(String.valueOf(queryMap.get(pkFeild)));

        // 构建条件
        StringBuilder stringBuilder = new StringBuilder();
        if(null != pkVal){
            stringBuilder.append(pkFeild);
            stringBuilder.append(CscpCustomizeConstant.SPACE);
            stringBuilder.append(CscpCustomizeConstant.EQUALS_SIGN);
            stringBuilder.append(CscpCustomizeConstant.SPACE);
            stringBuilder.append(pkVal);
        }
        queryMap = new HashMap();
        queryMap.put(operation,stringBuilder.toString());
        queryMap.put("tableName",tableName);
        return queryMap;
    }

    @Override
    @Transactional
    public int  addTemplate(Long pageId, Map<String , Object> addMap) {
        //校验是否为子表添加并且外键数据是否传入
        boolean chekMainTableAndFkDateNotExist = this.checkMainTableOrChidFkNotExistsData(pageId,addMap);
        if(chekMainTableAndFkDateNotExist){
            throw new BadRequestAlertException("添加数据错误：子表添加主表没有数据或外键数据没有传入","CscpCustomizeTemplateServiceImpl.addTemplate","");
        }
        //校验通过后数据添加
        TemplateEntity templateEntity = new TemplateEntity();
        // 根据formId获取业务表名
        CscpCustomizeVpage customizeVpage = cscpCustomizeVpageRepository.selectByPrimaryKey(pageId);
        //获取数据表字段信息
        String tableName = customizeVpage.getPageTable();
        List<Object> fields = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        // 获取添加属性及属性值列表
        Set<Map.Entry<String, Object>> entries = addMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            if(entry.getValue() != null){
                fields.add(entry.getKey());
                values.add(entry.getValue());
            }
        }
        String pkFeild = this.getPkColumn(tableName,dbName);
        // 判断是否包含主键字段,添加主键
        if(!fields.contains(pkFeild)){
            fields.add(pkFeild);
            values.add(SnowIdUtils.uniqueLong());
        }
        templateEntity.setTableName(tableName);
        templateEntity.setList(fields);
        templateEntity.setValues(values);
        //设置默认字段值 租户id 创建时间 创建人 最后更新时间 最后更新人 columnName
        templateEntity = this.setDefaultValue(templateEntity,true);
        // 更新主键注解
        if(null != pkFeild){
            //修改实体类注解属性值
            updateAutoIdPk(pkFeild);
            return customizeTemplateRepository.addTemplate(templateEntity);
        }else{
            throw new BadRequestAlertException("添加数据错误：主键为空","CscpCustomizeTemplateServiceImpl.addTemplate","");
        }

    }

    /**
     * 获取主键字段值
     * @param tableName 表名
     * @param dbName 实例名
     * @return
     */
    public String getPkColumn(String tableName,String dbName){
        Map<String,Object> map = new HashMap<>();
        map.put("tableName",tableName);
        map.put("dbName",dbName);
        List<CscpHxInfoSchemaMysql> list = informationSchemaMysqlService.getTableFieldInfoByMap(map);
        String pkFeild = null;
        //获取主键信息
        for(CscpHxInfoSchemaMysql cs : list){
            if(StringUtils.equalsIgnoreCase("PRI",cs.getColumnKey())){
                pkFeild = cs.getColumnName();
                break;
            }
        }
        return pkFeild;
    }

    /**
     * 设置默认字段  默认字段：tenant_id created_time created_userid updated_time updated_userid
     * @author wbb
     * @param templateEntity 入表实体
     * @return
     */
    public TemplateEntity setDefaultValue(TemplateEntity templateEntity,boolean addFlag){
        if(templateEntity == null
                || StringUtils.isEmpty(templateEntity.getTableName())
                || CollectionUtils.isEmpty(templateEntity.getList())
                || CollectionUtils.isEmpty(templateEntity.getValues())){
            return templateEntity;
        }
        //设置默认字段值 租户id 创建时间 创建人 最后更新时间 最后更新人 columnName
        List<String> tableNameList = new ArrayList<>();
        tableNameList.add(templateEntity.getTableName());
        List<Object> fields = templateEntity.getList();
        List<Object> values = templateEntity.getValues();
        List<CscpHxInfoSchemaMysql> list = informationSchemaMysqlService.getTableFieldInfoByMapBatch(tableNameList,dbName);
        //默认字段：tenant_id created_time created_userid updated_time updated_userid
        String[] defaultClumnArr = {"tenant_id","created_time","created_userid","updated_time","updated_userid"};
        List<String> defaultClumn = Arrays.asList(defaultClumnArr);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd :HH:mm:dd");
        if(CollectionUtils.isNotEmpty(tableNameList)){
            for(CscpHxInfoSchemaMysql cs : list){
                //如果添加的字段包含默认字段跳过
                if(fields.contains(cs.getColumnName())){
                    continue;
                }
                //如果不是默认字段跳过
                if(!defaultClumn.contains(cs.getColumnName())){
                    continue;
                }
                if(StringUtils.equalsIgnoreCase("updated_userid",cs.getColumnName())){
                    fields.add(cs.getColumnName());
                    values.add(SecurityHxUtils.getCurrentUserId());
                }
                if(StringUtils.equalsIgnoreCase("updated_time",cs.getColumnName())){
                    fields.add(cs.getColumnName());
                    values.add(sf.format(calendar.getTime()));
                }
                if(addFlag){
                    if(StringUtils.equalsIgnoreCase("tenant_id",cs.getColumnName())){
                        fields.add(cs.getColumnName());
                        values.add(SecurityHxUtils.getCurrentTenantId());
                    }
                    if(StringUtils.equalsIgnoreCase("created_userid",cs.getColumnName())){
                        fields.add(cs.getColumnName());
                        values.add(SecurityHxUtils.getCurrentUserId());
                    }
                    if(StringUtils.equalsIgnoreCase("created_time",cs.getColumnName())){
                        fields.add(cs.getColumnName());
                        values.add(sf.format(calendar.getTime()));
                    }
                }
            }
        }
        return templateEntity;
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
        List<String> fkList = new ArrayList<>();
        //关联字段
        if(StringUtils.isEmpty(childPage.getSubPageFk())){
            List<CscpHxDformColumn> columnList = cscpCustomizeVpageRepository.selectColumnByPageId(childPage.getPageId());
            if(CollectionUtils.isEmpty(columnList)){
                return true;
            }
            for(CscpHxDformColumn dc : columnList){
                if(StringUtils.isEmpty(dc.getColumnRelation())){
                    continue;
                }
                fkList.add(dc.getColumnName());
            }
        }else{
            fkList.add(childPage.getSubPageFk());
        }
        if(CollectionUtils.isEmpty(fkList)){
            return true;
        }
        for(String s : fkList){
            if(addMap.get(s) == null){
                return true;
            }
        }
        return false;
    }



    @Override
    @Transactional
    public int updateTemplate(Long pageId,  Map<String , Object> updateMap) {
        TemplateEntity templateEntity = new TemplateEntity();
        // 根据pageId获取业务表名
        CscpCustomizeVpage customizeVpage = cscpCustomizeVpageRepository.selectByPrimaryKey(pageId);
        String tableName = customizeVpage.getPageTable();
        // 获取主键信息
        String formOption = customizeVpage.getPageOption();
        JSONObject jsonObject = JSONObject.parseObject(formOption);
        String pkFeild = jsonObject.getString("pk");

        List<Object> fields = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        // 获取添加属性及属性值列表
        Set<Map.Entry<String, Object>> entries = updateMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            if(entry.getKey().equals(pkFeild)){
                templateEntity.setPkValue(Long.valueOf(String.valueOf(entry.getValue())));
            }
            if(entry.getValue() == null){
                continue;
            }
            values.add(entry.getValue());
            fields.add(entry.getKey());
        }
        //如果为空不给修改
        if(fields.size() == 0){
            return 0;
        }
        // 判断是否包含主键字段,添加主键
        if(!fields.contains(pkFeild)){
            throw new BadRequestAlertException("更新数据错误：主键为空","CscpCustomizeTemplateServiceImpl.updateTemplate","");
        }else{
            templateEntity.setPkField(pkFeild);
            templateEntity.setTableName(tableName);
            templateEntity.setList(fields);
            templateEntity.setValues(values);
            templateEntity = this.setDefaultValue(templateEntity,false);
        }
        //先更新子表数据
        return customizeTemplateRepository.updateTemplate(templateEntity);
    }

    /**
     *
     * 当主表外键修改时子表同步修改
     * @author wbb
     * @param pageId 主表formid
     * @param fields 修改字段
     * @param values 字段修改值
     * @param mainPkFeild 主表主键
     * @param mainPkValue 主表主键值
     */
    public void synChildren(Long pageId,
                            List<Object> fields,
                            List<Object> values,
                            String mainPkFeild,
                            Long   mainPkValue,
                            String mainTableName){
        if(pageId == null || CollectionUtils.isEmpty(fields) || CollectionUtils.isEmpty(values)){
            return;
        }
        //查询是否有子表
        List<Long> mainPageId = new ArrayList<>();
        mainPageId.add(pageId);
        //根据主表form表单信息查询子表form表单信息
        List<CscpCustomizeVpage> childPages = cscpCustomizeVpageRepository.getCscpCustomizeVpagesByMainPageIds(mainPageId);
        if(CollectionUtils.isEmpty(childPages)){
            return;
        }
        for(CscpCustomizeVpage cp : childPages){
            //更新字段不包含外键时跳过
            if(!fields.contains(cp.getSubPageFk())){
                continue;
            }
            Map<String,Object> paramMap = this.buildChildUpdateParam(cp,fields,values,cp.getSubPageFk());
            if(paramMap == null){
                continue;
            }
            //主表表名称
            paramMap.put("mainTableName",mainTableName);
            //主表主键字段名称
            paramMap.put("mainPkFeild",mainPkFeild);
            //主表主键值
            paramMap.put("mainPkValue",mainPkValue);
            //更新子表信息
            customizeTemplateRepository.updateChildTemplateBatch(paramMap);
        }
    }

    /**
     * 主表更新时如果更新了外键 设置子表更新参数
     * @author wbb
     * @param cp 子页信息
     * @param fields 主表更新字段列表
     * @param values 主表更新信息字段值
     * @param fk 子表和主表之间的外键
     * @return 子表更新参数
     */
    private Map<String,Object> buildChildUpdateParam(CscpCustomizeVpage cp,
                                                     List<Object> fields,
                                                     List<Object> values,
                                                     String fk){
        Map<String,Object> resultMap = null;
        for(int i = 0 ; i < fields.size() ; i++){
            if(!StringUtils.equals(fk, (String) fields.get(i)) || values.get(i) == null){
                continue;
            }
            resultMap = new HashMap<>();
            resultMap.put("fkName",fk);
            resultMap.put("fkValue",values.get(i));
            resultMap.put("tableName",cp.getPageTable());
        }
        return resultMap;
    }

    private void updateAutoIdPk(String pkField){
        Field idField = null;
        try {
            idField = TemplateEntity.class.getDeclaredField("pkValue");
            AutoId autoId = idField.getAnnotation(AutoId.class);
            //获取 autoId 这个代理实例所持有的 InvocationHandler
            InvocationHandler h = Proxy.getInvocationHandler(autoId);
            // 获取 AnnotationInvocationHandler 的 memberValues 字段
            Field hField = h.getClass().getDeclaredField("memberValues");
            // 因为这个字段事 private final 修饰，所以要打开权限
            hField.setAccessible(true);
            // 获取 memberValues
            Map memberValues = (Map) hField.get(h);
            // 修改 value 属性值
            memberValues.put("primaryKey", pkField);
            // 获取 autoId 的 value 属性值
//            String id = autoId.primaryKey();
//            System.out.println(id); //

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map queryByInstanceId(Long formId, Map<String, Object> queryMap) {
        CscpCustomizeVpage customizeVpage = cscpCustomizeVpageRepository.selectByPrimaryKey(formId);
        String tableName = customizeVpage.getPageTable();
        StringBuilder stringBuilder = new StringBuilder();
        if(null != queryMap.get("instance_id")){
            stringBuilder.append("instance_id");
            stringBuilder.append(CscpCustomizeConstant.SPACE);
            stringBuilder.append(CscpCustomizeConstant.EQUALS_SIGN);
            stringBuilder.append(CscpCustomizeConstant.SPACE);
            stringBuilder.append("'");
            stringBuilder.append(queryMap.get("instance_id"));
            stringBuilder.append("'");
        }
        HashMap query = new HashMap();
        query.put("idQuery",stringBuilder.toString());
        query.put("tableName",tableName);

        return customizeTemplateRepository.queryTemplate(query);
    }

    @Transactional
    @Override
    public AjaxResult taskUploadExcel(MultipartFile file,Long pageId,String queryStr) throws Exception{
        String fileName = file.getOriginalFilename();
        //校验文件格式
        assert fileName != null;
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            return AjaxResult.error("上传文件格式不正确");
        }
        try{
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            //获取第一个sheet页的内容
            Sheet sheet = workbook.getSheetAt(0);
            //获取行数
            int rows = sheet.getPhysicalNumberOfRows();
            if(rows <= 1){
                return AjaxResult.error("导入表格为空");
            }
            //表头对应字段列表
            List<Object> headList = new ArrayList<>();
            //根据formId查询form 表单信息获取表名
            // 根据formId获取业务表名
            CscpCustomizeVpage customizeVform = cscpCustomizeVpageRepository.selectByPrimaryKey(pageId);
            // 获取主键信息
            String formOption = customizeVform.getPageOption();
            JSONObject jsonObject = JSONObject.parseObject(formOption);
            String pkFeild = jsonObject.getString("pk");
            headList.add(pkFeild);
            Map headMap = (Map)JSONObject.parseObject(queryStr);
            //获取表头列表信息
            Row headRow = sheet.getRow(0);
            int columnNum = headRow.getPhysicalNumberOfCells();
            int idMark = -100;
            for(int i = 0 ; i < columnNum ; i++){
                //根据模板中文名称获取字段名称
                String headCluStr = (String) headMap.get(headRow.getCell(i).getStringCellValue());
                //过滤掉id
                if(StringUtils.equalsIgnoreCase("id",headCluStr)){
                    idMark = i;
                    continue;
                }
                headList.add(headCluStr);
            }
            List<List<Object>> valuesList = new ArrayList<>();
            for (int j = 1; j < rows; j++) {
                //获得第 j 行
                Row row = sheet.getRow(j);
                List<Object> values = new ArrayList<>();
                //设置主键
                values.add(SnowIdUtils.uniqueLong());
                for(int i = 0 ; i < columnNum ; i++){
                    try{
                        //过滤掉为id的数据
                        if(i == idMark){
                            continue;
                        }
                        values.add(StringUtils.equals(row.getCell(i).getStringCellValue(),"") ? null : row.getCell(i).getStringCellValue());
                    } catch (Exception e){
                        values.add(row.getCell(i).getNumericCellValue());
                    }
                }
                valuesList.add(values);
            }
            TemplateEntity templateEntity = new TemplateEntity();
            templateEntity.setList(headList);
            templateEntity.setTableName(customizeVform.getPageTable());
            templateEntity.setValuesList(valuesList);
            //修改实体类注解属性值
            updateAutoIdPk(pkFeild);
            //设置默认的添加字段
            templateEntity = this.setDefaultValueBatch(templateEntity);
            //批量添加
            customizeTemplateRepository.addTemplateBatch(templateEntity);
            return  AjaxResult.success("导入成功");
        }catch (Exception e){
            e.printStackTrace();
            return  AjaxResult.error("导入失败，请确认模板");
        }
    }

    /**
     * 设置默认字段  默认字段：tenant_id created_time created_userid updated_time updated_userid
     * @author wbb
     * @param templateEntity 入表实体
     * @return
     */
    public TemplateEntity setDefaultValueBatch(TemplateEntity templateEntity){
        if(templateEntity == null
                || StringUtils.isEmpty(templateEntity.getTableName())
                || CollectionUtils.isEmpty(templateEntity.getList())
                || CollectionUtils.isEmpty(templateEntity.getValuesList())){
            return templateEntity;
        }
        //设置默认字段值 租户id 创建时间 创建人 最后更新时间 最后更新人 columnName
        List<String> tableNameList = new ArrayList<>();
        tableNameList.add(templateEntity.getTableName());
        List<Object> fields = templateEntity.getList();
        List<List<Object>> valuesList = templateEntity.getValuesList();
        List<CscpHxInfoSchemaMysql> list = informationSchemaMysqlService.getTableFieldInfoByMapBatch(tableNameList,dbName);
        //默认字段：tenant_id created_time created_userid updated_time updated_userid
        String[] defaultClumnArr = {"tenant_id","created_time","created_userid","updated_time","updated_userid"};
        List<String> defaultClumn = Arrays.asList(defaultClumnArr);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd :HH:mm:dd");
        if(CollectionUtils.isNotEmpty(tableNameList)){
            for(CscpHxInfoSchemaMysql cs : list){
                //如果添加的字段包含默认字段跳过
                if(fields.contains(cs.getColumnName())){
                    continue;
                }
                //如果不是默认字段跳过
                if(!defaultClumn.contains(cs.getColumnName())){
                    continue;
                }
                fields.add(cs.getColumnName());
                for(List<Object> values : valuesList){
                    if(StringUtils.equalsIgnoreCase("tenant_id",cs.getColumnName())){
                        values.add(SecurityHxUtils.getCurrentTenantId());
                    }
                    if(StringUtils.equalsIgnoreCase("created_userid",cs.getColumnName()) ||
                            StringUtils.equalsIgnoreCase("updated_userid",cs.getColumnName())){
                        values.add(SecurityHxUtils.getCurrentUserId());
                    }
                    if(StringUtils.equalsIgnoreCase("created_time",cs.getColumnName()) ||
                            StringUtils.equalsIgnoreCase("updated_time",cs.getColumnName())){
                        values.add(sf.format(calendar.getTime()));
                    }
                }

            }
        }
        return templateEntity;
    }
}
