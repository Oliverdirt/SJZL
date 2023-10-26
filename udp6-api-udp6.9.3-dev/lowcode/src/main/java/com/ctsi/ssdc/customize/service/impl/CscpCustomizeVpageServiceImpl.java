package com.ctsi.ssdc.customize.service.impl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctsi.ssdc.admin.domain.CscpMenus;
import com.ctsi.ssdc.admin.service.CscpMenusService;
import com.ctsi.ssdc.admin.service.CscpTenantService;
import com.ctsi.ssdc.customize.domain.*;
import com.ctsi.ssdc.gen.domain.NoModelWriteData;
import com.ctsi.ssdc.gen.domain.TablePkMessage;
import com.ctsi.ssdc.gen.domain.TemplateEntity;
import com.ctsi.ssdc.gen.repository.CscpCustomizeTemplateRepository;
import com.ctsi.ssdc.customize.repository.CscpCustomizeVpageRepository;
import com.ctsi.ssdc.customize.service.CscpCustomizeVpageService;
import com.ctsi.ssdc.gen.domain.CscpHxInfoSchemaMysql;
import com.ctsi.ssdc.gen.service.CscpHxInfoSchemaMysqlService;
import com.ctsi.ssdc.gen.util.EasyExcelUtil;
import com.ctsi.ssdc.security.jwt.TokenHxProvider;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Service Implementation for managing CscpCustomizeVpage.
 *
 * @author hx
 * @date 2022-09-01 14:40:59
 *
 */

@Service
public class CscpCustomizeVpageServiceImpl
        extends StrengthenBaseServiceImpl<CscpCustomizeVpageRepository, CscpCustomizeVpage, Long, CscpCustomizeVpageExample>
        implements CscpCustomizeVpageService {

    @Autowired
    private CscpCustomizeVpageRepository cscpCustomizeVpageRepository;
    @Autowired
    private CscpMenusService cscpMenusService;
    @Value("${spring.datasource.db-name}")
    private String dbName;
    @Resource
    private CscpCustomizeTemplateRepository customizeTemplateRepository;
    @Autowired
    CscpHxInfoSchemaMysqlService cscpHxInfoSchemaMysqlService;
    @Autowired
    private TokenHxProvider tokenHxProvider;
    @Autowired
    private CscpTenantService cscpTenantService;
    /**
     * 批量删除
     * @param pageIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] pageIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(pageIds));
        if(CollectionUtils.isEmpty(delList)){
            return;
        }
        //查询子表的formId
        List<Long> childFormIds = this.selectPageIdsByMainPageId(delList);
        if(CollectionUtils.isNotEmpty(childFormIds)){
            delList.addAll(childFormIds);
        }
        //批量删除页面表信息
        cscpCustomizeVpageRepository.deleteByIds(delList);
        //批量删除菜单及角色表信息
        this.deleteMenuBatch(delList);
    }


    @Override
    public List<Long> selectPageIdsByMainPageId(List<Long> mainPageIds) {

        List<Long> mainPageIdList = cscpCustomizeVpageRepository.selectPageIdsByMainPageIdList(mainPageIds);
        return mainPageIdList == null ? new ArrayList<>() : mainPageIdList;
    }

    @Override
    public List<CscpHxDformColumn> selectRelationColumnByPageId(Long pageId) {
        List<CscpHxDformColumn> dcList = cscpCustomizeVpageRepository.selectColumnByPageId(pageId);
        List<CscpHxDformColumn> relationColumnList = new ArrayList<>();
        if(CollectionUtils.isEmpty(dcList)){
            return relationColumnList;
        }
        for(CscpHxDformColumn cd : dcList){
            if(cd == null || StringUtils.isEmpty(cd.getColumnRelation())){
                continue;
            }
            relationColumnList.add(cd);
        }
        return relationColumnList;
    }

    @Override
    public List<CscpHxDformColumn> selectRelationColumnByTableNames(List<String> tableNames) {
        List<CscpHxDformColumn> dcList = cscpCustomizeVpageRepository.selectColumnByTableNames(tableNames);
        List<CscpHxDformColumn> relationColumnList = new ArrayList<>();
        if(CollectionUtils.isEmpty(dcList)){
            return relationColumnList;
        }
        for(CscpHxDformColumn cd : dcList){
            if(cd == null || StringUtils.isEmpty(cd.getColumnRelation())){
                continue;
            }
            relationColumnList.add(cd);
        }
        return relationColumnList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long pageId) {
        List<Long> urlList = new ArrayList<>();
        urlList.add(pageId);
        deleteMenuBatch(urlList);
        cscpCustomizeVpageRepository.deleteByPrimaryKey(pageId);
    }

    @Override
    public List<CscpCustomizeVpage> qryPageTableInfo(CscpCustomizeVpage cscpCustomizeVpage) {
        CscpCustomizeVpageExample cscpCustomizeVpageExample = new CscpCustomizeVpageExample();
        CscpCustomizeVpageExample.Criteria criteria = cscpCustomizeVpageExample.createCriteria().andDelFlagEqualTo("0");
        if (!ObjectUtils.isEmpty(cscpCustomizeVpage.getModuleId())) {
            criteria.andModuleIdEqualTo(cscpCustomizeVpage.getModuleId());
        }
        if (!ObjectUtils.isEmpty(cscpCustomizeVpage.getPageId())) {
            criteria.andPageIdEqualTo(cscpCustomizeVpage.getPageId());
        }
        if (!StringUtils.isEmpty(cscpCustomizeVpage.getPageType())) {
            criteria.andPageTypeEqualTo(cscpCustomizeVpage.getPageType());
        }
        return cscpCustomizeVpageRepository.selectByExample(cscpCustomizeVpageExample);
    }

    /**
     * 根据module_id批量删除
     * @param moduleIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByModuleIds(List<Long> moduleIds) {
        cscpCustomizeVpageRepository.deleteByModuleIds(moduleIds);
    }

    @Override
    public List<CscpCustomizeVpage> queryChildCscpCustomizeVpages(Long[] pageIds) {
        return cscpCustomizeVpageRepository.getCscpCustomizeVpagesByMainPageIds(Arrays.asList(pageIds));
    }

    @Override
    public List<CscpCustomizeVpage> getCscpCustomizeVpagesByModuleIds(Long[] moduleIds) {
        return cscpCustomizeVpageRepository.getCscpCustomizeVpagesByModuleIds(Arrays.asList(moduleIds));
    }

    /**
     * 批量删除菜单
     * @param pageIds pageId列表
     */
    @Override
    public void deleteMenuBatch(List<Long> pageIds){
        if(CollectionUtils.isEmpty(pageIds)){
            return;
        }
        List<String> urlList = new ArrayList<>();
        //设置角色查询参数
        for(Long fd : pageIds){
            urlList.add("/customize/page/" + fd);
        }
        //批量查询 菜单角色信息
        List<CscpMenus> cscpMenusList = cscpMenusService.getMenuByUrlBatch(urlList);
        if(CollectionUtils.isEmpty(cscpMenusList)){
            return;
        }
        //菜单Id 列表获取
        List<Long> idList = cscpMenusList.stream().map(CscpMenus :: getId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(idList)){
            return;
        }
        //根据菜单列表删除菜单及角色关系
        cscpMenusService.deleteByIdBatch(idList);
    }

    @Override
    public void export(Long pageId, Map<String, Object> mapTypes, HttpServletResponse response) throws IOException {
        TemplateEntity templateEntity = new TemplateEntity();
        List<String> headMap = new ArrayList<>();
        List<String> dataStrMap = new ArrayList<>();
        List<Map<String, Object>> listData = new ArrayList<>();
        Map<String,Object> queryMap = new HashMap();
        List<String> dateList = new ArrayList<>();
        CscpCustomizeVpage cscpCustomizeVpage = cscpCustomizeVpageRepository.selectByPrimaryKey(pageId);
        queryMap.put("tableName",cscpCustomizeVpage.getPageTable());
        for (Object obj : mapTypes.entrySet()){
            dataStrMap.add(((Map.Entry)obj).getKey().toString());
            headMap.add(((Map.Entry)obj).getValue().toString());
        }

        List<String> queryFields = templateEntity.getQueryFields();
        //查询条件为空
        List<Map> maps = null;
        //设置排序字段 默认根据主键排序
        List<String> tableNameList = new ArrayList<>();
        List<String> tableSchemaList = new ArrayList<>();
        tableNameList.add(cscpCustomizeVpage.getPageTable());
        tableSchemaList.add(dbName);
        queryMap = this.buildOrderParam(tableNameList, tableSchemaList, queryMap,templateEntity);
        if (queryFields == null || queryFields.size() == 0) {
            maps = customizeTemplateRepository.queryAllPage(queryMap);
        }

        List<CscpHxInfoSchemaMysql> tableFieldInfoByMap = cscpHxInfoSchemaMysqlService.getTableFieldInfoByMap(queryMap);
        if(CollectionUtils.isNotEmpty(tableFieldInfoByMap)){
            for (CscpHxInfoSchemaMysql oneCscpHxInfoSchemaMysql : tableFieldInfoByMap){
                if("datetime".equals(oneCscpHxInfoSchemaMysql.getDataType())){
                    dateList.add(oneCscpHxInfoSchemaMysql.getColumnName());
                }
            }
        }

        String formats = "yyyy-MM-dd HH:mm:ss";
        JSONArray objects = JSONArray.parseArray(JSONObject.toJSON(maps).toString());
        for (int i = 0; i < objects.size(); i++) {
            JSONObject jsonObject = JSONObject.parseObject(objects.get(i).toString());
            for(String oneDate : dateList){
                if(jsonObject.containsKey(oneDate)){
                    Long timestamp = Long.parseLong(jsonObject.getString(oneDate));
                    //日期格式字符串
                    String dateStr = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
                    jsonObject.put(oneDate,dateStr);
                }
            }

            Map<String, Object> map = new HashMap<>();
            for (String one : dataStrMap) {
//                if(jsonObject.containsKey(one)){
                    map.put(one,jsonObject.get(one)== null ? "" : jsonObject.get(one));
//                }
            }
            listData.add(map);
        }
        NoModelWriteData data = new NoModelWriteData();
        data.setFileName(cscpCustomizeVpage.getPageName());
        data.setHeadMap(headMap);
        data.setDataStrMap(dataStrMap);
        data.setDataList(listData);
        EasyExcelUtil easyExcelUtil = new EasyExcelUtil();
        easyExcelUtil.noModelWrite(data, response);
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
                org.apache.commons.lang.StringUtils.isNotEmpty(pkMesageMap.get(key))){
            orderColumn.append(",");
            //默认根据主键排序
            orderColumn.append(pkMesageMap.get(key));
        }
        //如果存在排序字段 且排序规则不为空 增加排序字段
        String orderByClause = templateEntity.getOrderByClause();
        if(org.apache.commons.lang.StringUtils.isNotEmpty(orderByClause) &&
                org.apache.commons.lang.StringUtils.isNotEmpty(orderColumn.toString())){
            orderColumn.append(" ");
            orderColumn.append(orderColumn);
        }
        if(org.apache.commons.lang.StringUtils.isNotEmpty(orderColumn.toString())){
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
        List<TablePkMessage> notNullTablePkMessageList = new ArrayList<>();
        for(TablePkMessage tm : tablePkMessageList){
            if(tm == null){
                continue;
            }
            notNullTablePkMessageList.add(tm);
        }
        //转成map的形式 实例加表名为key值 主键为 value值
        return notNullTablePkMessageList.stream().collect(Collectors.toMap(it->it.getTableSchema() + it.getTableName(),TablePkMessage :: getPkColumn));
    }
}
