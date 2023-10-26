package com.ctsi.ssdc.gen.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.gen.constant.CscpCustomizeConstant;
import com.ctsi.ssdc.gen.domain.*;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVform;
import com.ctsi.ssdc.gen.domain.TemplateEntity;
import com.ctsi.ssdc.gen.repository.CscpCustomizeTemplateRepository;
import com.ctsi.ssdc.gen.repository.CscpCustomizeVfieldRepository;
import com.ctsi.ssdc.gen.repository.CscpCustomizeVformRepository;
import com.ctsi.ssdc.gen.service.CscpCustomizeTemplateService;
import com.ctsi.ssdc.gen.service.CscpCustomizeVfieldService;
import com.ctsi.ssdc.gen.util.EasyExcelUtil;
import com.ctsi.ssdc.enums.QueryTypeEnum;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CscpCustomizeTemplateServiceImpl implements CscpCustomizeTemplateService {
    @Resource
    private CscpCustomizeTemplateRepository customizeTemplateRepository;

    @Resource
    private CscpCustomizeVformRepository cscpCustomizeVformRepository;

    @Resource
    private CscpCustomizeVfieldRepository customizeVfieldRepository;

    @Resource
    private CscpCustomizeVfieldService cscpCustomizeVfieldService;

    @Value("${spring.datasource.db-name}")
    private String dbName;

    @Override
    public PageResult<Map> queryTemplatePage(Long formId, TemplateEntity templateEntity, Pageable pageable) {
        CscpCustomizeVform customizeVform = cscpCustomizeVformRepository.selectByPrimaryKey(formId);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       String tableName = customizeVform.getFormTable();
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
        tableNameList.add(customizeVform.getFormTable());
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
    public int batchDelTemplate(Long formId,Map<String, Object> delMap) {
        //查询主表单信息
        CscpCustomizeVform customizeVform = cscpCustomizeVformRepository.selectByPrimaryKey(formId);
        //获取主表业务表名称
        String tableName = customizeVform.getFormTable();
        // 获取主键信息
        String formOption = customizeVform.getFormOption();
        JSONObject jsonObject = JSONObject.parseObject(formOption);
        String pkFeild = jsonObject.getString(CscpCustomizeConstant.PK);
        //这里兼容单条和批量 后面页面传参确定统一修改
        Object pkFieldVal = delMap.get(pkFeild+"s") == null ? delMap.get(pkFeild) : delMap.get(pkFeild+"s");
        //表字段拼接成页面传参形式
        if(StringUtils.isEmpty((String)pkFieldVal)){
            throw new BadRequestAlertException("没有选择要删除的数据","ids is null","");
        }else{
            //表字段拼接成页面传参形式
            String ids = String.valueOf(pkFieldVal);
            //子表数据删除
            this.delChild(formId,pkFeild,ids,customizeVform);
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
        //转成map的形式 实例加表名为key值 主键为 value值
        return tablePkMessageList.stream().collect(Collectors.toMap(it->it.getTableSchema() + it.getTableName(),TablePkMessage :: getPkColumn));
    }

    /**
     * 删除子表信息
     * @author wbb
     * @param formId 主表模块id
     * @param pkFeild 主表主键
     * @param ids 页面传入主表id
     * @param customizeVform 主表form表单信息
     */
    private void delChild(Long formId,String pkFeild,String ids,CscpCustomizeVform customizeVform){
        //子表查询参数
        List<Long> mainFormIdList = new ArrayList<>();
        mainFormIdList.add(formId);
        //根据主表fromId查询子表表单信息
        List<CscpCustomizeVform> childCustomizeVforms = cscpCustomizeVformRepository.getCscpCustomizeVformsByMainFormIds(mainFormIdList);
        //批量删除子表数据
        if(CollectionUtils.isEmpty(childCustomizeVforms)){
            return;
        }
        Map<String,Object> childDelMap = new HashMap<>();
        for(CscpCustomizeVform form : childCustomizeVforms){
            childDelMap.clear();
            //t1为主表信息 t2为子表信息 这里删除的是 t2表信息
            childDelMap.put("mainTableIds"," and t1." + pkFeild + " in (" +ids + ") ");
            childDelMap.put("tableNames",customizeVform.getFormTable() + " t1, " + form.getFormTable() + " t2 ");
            childDelMap.put("fkRelationShip","t1." + pkFeild + " = t2." + form.getSubFormFk() + " ");
            customizeTemplateRepository.batchDelChildTemplate(childDelMap);
        }
    }

    private Map<String, Object> bulidQueryPkWhere(Long formId, Map<String, Object> queryMap,String operation) {
        CscpCustomizeVform customizeVform = cscpCustomizeVformRepository.selectByPrimaryKey(formId);
        String tableName = customizeVform.getFormTable();
        // 获取主键信息
        String formOption = customizeVform.getFormOption();
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
    public int  addTemplate(Long formId, Map<String , Object> addMap) {
        //校验是否为子表添加并且外键数据是否传入
        boolean chekMainTableAndFkDateNotExist = this.checkMainTableOrChidFkNotExistsData(formId,addMap);
        if(chekMainTableAndFkDateNotExist){
            throw new BadRequestAlertException("添加数据错误：子表添加主表没有数据或外键数据没有传入","CscpCustomizeTemplateServiceImpl.addTemplate","");
        }
        //校验通过后数据添加
        TemplateEntity templateEntity = new TemplateEntity();
        // 根据formId获取业务表名
        CscpCustomizeVform customizeVform = cscpCustomizeVformRepository.selectByPrimaryKey(formId);
        String tableName = customizeVform.getFormTable();
        // 获取主键信息
        String formOption = customizeVform.getFormOption();
        JSONObject jsonObject = JSONObject.parseObject(formOption);
        String pkFeild = jsonObject.getString("pk");

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

        // 判断是否包含主键字段,添加主键
        if(!fields.contains(pkFeild)){
            fields.add(pkFeild);
            values.add(SnowIdUtils.uniqueLong());
        }

        templateEntity.setTableName(tableName);
        templateEntity.setList(fields);
        templateEntity.setValues(values);



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
     * 校验是否为子表添加 如果为子表添加校验是否传入了外键数据
     * @author wbb
     * @param formId 子表formid
     * @param addMap 添加map
     * @return 是否没有主表数据或外键值是否传入 true 没有主表数据或外键值没有传入 false 有主表数据或外键值有传入
     */
    private boolean checkMainTableOrChidFkNotExistsData(Long formId, Map<String , Object> addMap) {
        //根据formId查询子表表单信息
        CscpCustomizeVform childForm = cscpCustomizeVformRepository.selectByPrimaryKey(formId);
        //mainFormId为空视为单表
        if(childForm.getMainFormId() == null){
            return false;
        }
        //外键字段
        String fk = childForm.getSubFormFk().replaceAll("_","");
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

    @Override
    @Transactional
    public int updateTemplate(Long formId,  Map<String , Object> updateMap) {
        TemplateEntity templateEntity = new TemplateEntity();
        // 根据formId获取业务表名
        CscpCustomizeVform customizeVform = cscpCustomizeVformRepository.selectByPrimaryKey(formId);
        String tableName = customizeVform.getFormTable();
        // 获取主键信息
        String formOption = customizeVform.getFormOption();
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
//            if(entry.getValue() != null){
                values.add(entry.getValue());
                fields.add(entry.getKey());
//            }

        }
        // 判断是否包含主键字段,添加主键
        if(!fields.contains(pkFeild)){
            throw new BadRequestAlertException("更新数据错误：主键为空","CscpCustomizeTemplateServiceImpl.updateTemplate","");
        }else{
            templateEntity.setPkField(pkFeild);
            templateEntity.setTableName(tableName);
            templateEntity.setList(fields);
            templateEntity.setValues(values);
        }
        //先更新子表数据
//        this.synChildren(formId,fields,values,pkFeild,templateEntity.getPkValue(),tableName);
        return customizeTemplateRepository.updateTemplate(templateEntity);
    }

    /**
     *
     * 当主表外键修改时子表同步修改
     * @author wbb
     * @param formId 主表formid
     * @param fields 修改字段
     * @param values 字段修改值
     * @param mainPkFeild 主表主键
     * @param mainPkValue 主表主键值
     */
    public void synChildren(Long formId,
                            List<Object> fields,
                            List<Object> values,
                            String mainPkFeild,
                            Long   mainPkValue,
                            String mainTableName){
        if(formId == null || CollectionUtils.isEmpty(fields) || CollectionUtils.isEmpty(values)){
            return;
        }
        //查询是否有子表
        List<Long> mainFormId = new ArrayList<>();
        mainFormId.add(formId);
        //根据主表form表单信息查询子表form表单信息
        List<CscpCustomizeVform> childForms = cscpCustomizeVformRepository.getCscpCustomizeVformsByMainFormIds(mainFormId);
        if(CollectionUtils.isEmpty(childForms)){
            return;
        }
        for(CscpCustomizeVform cf : childForms){
            //更新字段不包含外键时跳过
            if(!fields.contains(cf.getSubFormFk())){
                continue;
            }
            Map<String,Object> paramMap = this.buildChildUpdateParam(cf,fields,values,cf.getSubFormFk());
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
     * @param cf 子表form表单信息
     * @param fields 主表更新字段列表
     * @param values 主表更新信息字段值
     * @param fk 子表和主表之间的外键
     * @return 子表更新参数
     */
    private Map<String,Object> buildChildUpdateParam(CscpCustomizeVform cf,
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
            resultMap.put("tableName",cf.getFormTable());
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
        CscpCustomizeVform customizeVform = cscpCustomizeVformRepository.selectByPrimaryKey(formId);
        if (org.apache.commons.lang3.StringUtils.equals(customizeVform.getFormType(), "1")) {
            String tableName = customizeVform.getFormTable();
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
        return null;
    }

    @Transactional
    @Override
    public AjaxResult taskUploadExcel(MultipartFile file, Long formId) throws Exception{
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
            //通过formId 获取vfield 信息 通过表头名称获取对应的表字段
            List<CscpCustomizeVfield> vfieldList = customizeVfieldRepository.getListByFormId(formId);
            Map<String,List<CscpCustomizeVfield>> fieldMap = vfieldList.stream().collect(Collectors.groupingBy(CscpCustomizeVfield :: getFieldComment));
            //表头对应字段列表
            List<Object> headList = new ArrayList<>();
            //根据formId查询form 表单信息获取表名
            // 根据formId获取业务表名
            CscpCustomizeVform customizeVform = cscpCustomizeVformRepository.selectByPrimaryKey(formId);
            // 获取主键信息
            String formOption = customizeVform.getFormOption();
            JSONObject jsonObject = JSONObject.parseObject(formOption);
            String pkFeild = jsonObject.getString("pk");
            headList.add(pkFeild);
            //获取表头列表信息
            Row headRow = sheet.getRow(0);
            int columnNum = headRow.getPhysicalNumberOfCells();
            for(int i = 0 ; i < columnNum ; i++){
                headList.add(fieldMap.get(headRow.getCell(i).getStringCellValue()).get(0).getFieldName());
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
                        values.add(row.getCell(i).getStringCellValue());
                    } catch (Exception e){
                        values.add(row.getCell(i).getNumericCellValue());
                    }
                }
                valuesList.add(values);
            }
            TemplateEntity templateEntity = new TemplateEntity();
            templateEntity.setList(headList);
            templateEntity.setTableName(customizeVform.getFormTable());
            templateEntity.setValuesList(valuesList);
            //修改实体类注解属性值
            updateAutoIdPk(pkFeild);
            //批量添加
            customizeTemplateRepository.addTemplateBatch(templateEntity);
            return  AjaxResult.success();
        }catch (Exception e){
            return  AjaxResult.error();
        }
    }

    @Transactional
    @Override
    public void export(Long formId, TemplateEntity templateEntity, HttpServletResponse response) throws IOException {
        List<String> headMap = new ArrayList<>();
        List<String> dataStrMap = new ArrayList<>();
        List<Map<String, Object>> listData = new ArrayList<>();
        List<CscpCustomizeVfield> fields = cscpCustomizeVfieldService.getListByFormId(formId);
        for (CscpCustomizeVfield one: fields) {
            if("1".equals(one.getFieldList())){
                headMap.add(one.getFieldComment());
                dataStrMap.add(one.getFieldName());
            }
        }
        CscpCustomizeVform customizeVform = cscpCustomizeVformRepository.selectByPrimaryKey(formId);
        String tableName = customizeVform.getFormTable();

//        String formJson = customizeVForm.getFormJson();
//        JSONObject jsonObject1 = JSONObject.parseObject(formJson.toString());


        List<String> queryFields = templateEntity.getQueryFields();
        //查询条件为空
        List<Map> maps = null;
        Map<String,Object> queryMap = new HashMap();
        queryMap.put("tableName",tableName);
        //设置排序字段 默认根据主键排序
        List<String> tableNameList = new ArrayList<>();
        List<String> tableSchemaList = new ArrayList<>();
        tableNameList.add(customizeVform.getFormTable());
        tableSchemaList.add(dbName);
        queryMap = this.buildOrderParam(tableNameList, tableSchemaList, queryMap,templateEntity);
        if (queryFields == null || queryFields.size() == 0) {
            maps = customizeTemplateRepository.queryAllPage(queryMap);
        }
        JSONArray objects = JSONArray.parseArray(JSONObject.toJSON(maps).toString());
        for (int i = 0; i < objects.size(); i++) {
            JSONObject jsonObject = JSONObject.parseObject(objects.get(i).toString());
            Map<String, Object> map = new HashMap<>();
            for (String one : dataStrMap) {
                if(jsonObject.containsKey(one)){
                    map.put(one,jsonObject.get(one));
                }
            }
            listData.add(map);
        }
        NoModelWriteData data = new NoModelWriteData();
        data.setFileName(customizeVform.getFormName());
        data.setHeadMap(headMap);
        data.setDataStrMap(dataStrMap);
        data.setDataList(listData);
        EasyExcelUtil easyExcelUtil = new EasyExcelUtil();
        easyExcelUtil.noModelWrite(data, response);
    }
}
