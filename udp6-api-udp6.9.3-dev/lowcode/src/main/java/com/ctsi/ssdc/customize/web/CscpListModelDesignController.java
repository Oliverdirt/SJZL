package com.ctsi.ssdc.customize.web;

import com.ctsi.ssdc.admin.domain.CscpMenus;
import com.ctsi.ssdc.admin.service.CscpMenusService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.customize.domain.CscpCustomizeVpage;
import com.ctsi.ssdc.customize.domain.CscpHxDformTable;
import com.ctsi.ssdc.customize.service.CscpCustomizeModuleService;
import com.ctsi.ssdc.customize.service.CscpCustomizeVpageService;
import com.ctsi.ssdc.customize.service.CscpHxDformTableService;
import com.ctsi.ssdc.customize.service.CscpListModelDesignService;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.gen.domain.TemplateEntity;
import com.ctsi.ssdc.model.AjaxBackData;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import com.ctsi.ssdc.utils.HxStringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/8/25 10:35  by xx
 */
@Api(value = "/api/lowcode/modelDesign",tags = {"低代码-列表设计"})
@RestController
@RequestMapping("/api/lowcode/modelDesign")
@Slf4j
public class CscpListModelDesignController {

    private static final String ENTITY_NAME = "CscpHxDformTable";

    @Autowired
    private CscpListModelDesignService cscpListModelDesignService;

    @Autowired
    private CscpHxDformTableService cscpHxDformTableService;

    @Autowired
    private CscpCustomizeVpageService cscpCustomizeVpageService;

    @Autowired
    private CscpMenusService cscpMenusService;

    @Autowired
    CscpCustomizeModuleService cscpCustomizeModuleService;

    /**
     * Description: 查询数据源信息 (数据库连接标识，地址，端口，数据库名，用户名)
     * Copyright (c) CSII.
     * All Rights Reserved.
     * 2022/8/25 10:40  by xx
     */
    @GetMapping("/qryDataBaseInfo")
    @ApiOperation(value = "查询默认数据源信息")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "qryDataBaseInfo")
    @Log
    public Map qryDataBaseInfo(){
        return cscpListModelDesignService.qryDataBaseInfo();
    }

    /**
     * 同步数据库 （主子表创建入库）
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/cscpHxsync/{id}")
    @ApiOperation(value = "同步表单信息至数据库")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "syncDatabase")
    @Log
    public ResponseEntity<CscpHxDformTable> syncDatabase(@PathVariable("id") Long id) {
        CscpHxDformTable cscpHxDformTable = cscpHxDformTableService.syncDatabaseWithModelDesign(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cscpHxDformTable.getTableId().toString()))
                .body(cscpHxDformTable);
    }

    /**
     * 数据模型新增（单表或多表）
     * @param cscpHxDformTable
     * @return
     * @throws URISyntaxException
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpHxDformTable",name = "CscpHxDformTable",value = "the CscpHxDformTable to create")
    })
    @ApiOperation(value = "POST  /cscpHxFormTables : create a new CscpHxDformTable.",notes = "POST  /CscpHxDformTables : create a new CscpHxDformTable.",httpMethod = "POST")
    @PostMapping("/cscpHxFormTables")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "createCscpHxDformTable")
    @Log
    public ResponseEntity<Long> createCscpHxDformTable(@RequestBody CscpHxDformTable cscpHxDformTable) throws URISyntaxException {
        log.debug("REST request to save CscpHxDformTable : {}", cscpHxDformTable);
        if (cscpHxDformTable.getTableId() != null) {
            throw new BadRequestAlertException("A new CscpHxDformTable cannot already have an id", ENTITY_NAME, "idexists");
        }
        if (StringUtils.isEmpty(cscpHxDformTable.getMainTableId())) {
            cscpHxDformTable.setMainTableId(0L);
        }
        CscpHxDformTable result = new CscpHxDformTable();
        try {
            result = cscpHxDformTableService.insert(cscpHxDformTable);
            // 创建数据库表
            CscpHxDformTable table = cscpHxDformTableService.createTable(cscpHxDformTable, cscpHxDformTable.getColumns());
            log.debug("createTableInfo: {}",table);
            // 更新模型信息表table_id
            int i = cscpListModelDesignService.updateModelInfo(result.getTableId(),cscpHxDformTable.getTableName());
            log.debug("updateModelInfo result {}",i);
            // 插入子表
            for (CscpHxDformTable formTable:cscpHxDformTable.getCscpHxFormTables()) {
                formTable.setMainTableId(result.getTableId());
                cscpHxDformTableService.insert(formTable);
                // 创建子表
                cscpHxDformTableService.createTable(formTable,formTable.getColumns());
            }
        }catch (Exception e){
            // 删除主表
            CscpHxDformTable mainTable = new CscpHxDformTable();
            mainTable.setTableId(result.getTableId());
            deleteTable(mainTable);
            deleteCscpHxDformTable(result.getTableId());
            // 删除子表
            List<Long> childTableIds = cscpListModelDesignService.selectChildTable(result.getTableId());
            for (Long childTableId:childTableIds) {
                CscpHxDformTable childTable = new CscpHxDformTable();
                childTable.setTableId(childTableId);
                deleteTable(childTable);
                deleteCscpHxDformTable(childTableId);
            }
            throw new BadRequestAlertException("createCscpHxDformTable post fail", ENTITY_NAME, "error");
        }

        return ResponseEntity.created(new URI("/api/CscpHxDformTables" + "/" +result.getTableId() ))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getTableId().toString()))
                .body(result.getTableId());
    }


    /**
     * 数据模型新增（单表或多表）
     * @param cscpHxDformTable
     * @return
     * @throws URISyntaxException
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpHxDformTable",name = "CscpHxDformTable",value = "the CscpHxDformTable to create")
    })
    @ApiOperation(value = "POST  /cscpHxFormTablesMesage : create a new CscpHxDformTable.",notes = "POST  /CscpHxDformTables : create a new CscpHxDformTable.",httpMethod = "POST")
    @PostMapping("/cscpHxFormTablesMesage")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "CscpHxDformTablesMesage")
    @Log
    public ResponseEntity<Long> createCscpHxDformTableMessage(@RequestBody CscpHxDformTable cscpHxDformTable) throws URISyntaxException {
        log.debug("REST request to save CscpHxDformTablesMesage : {}", cscpHxDformTable);
        if (cscpHxDformTable.getTableId() != null) {
            throw new BadRequestAlertException("A new CscpHxDformTable cannot already have an id", ENTITY_NAME, "idexists");
        }
        cscpHxDformTable.setMainTableId(0L);
        CscpHxDformTable result = cscpHxDformTableService.insert(cscpHxDformTable);
        // 更新模型信息表table_id
        int i = cscpListModelDesignService.updateModelInfo(result.getTableId(),cscpHxDformTable.getTableName());
        log.debug("updateModelInfo result {}",i);
        return ResponseEntity.created(new URI("/api/CscpHxDformTablesMesage" + "/" +result.getTableId() ))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getTableId().toString()))
                .body(result.getTableId());
    }


    /**
     * 更新PUT
     * @param cscpHxDformTable
     * @return
     * @throws URISyntaxException
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpHxDformTable",name = "CscpHxDformTable",value = "the CscpHxDformTable to update")
    })
    @ApiOperation(value = "PUT  /cscpHxFormTables : updates an existing CscpHxDformTable.",notes = "PUT  /CscpHxDformTables : updates an existing CscpHxDformTable.",httpMethod = "PUT")
    @PutMapping("/cscpHxFormTables")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "updateCscpHxDformTable")
    @Log
    public ResponseEntity<String> updateCscpHxDformTable(@RequestBody CscpHxDformTable cscpHxDformTable) throws URISyntaxException  {
        log.debug("REST request to update CscpHxDformTable : {}", cscpHxDformTable);
        List<CscpHxDformTable> cscpHxDformTables = cscpHxDformTable.getCscpHxFormTables();
        // 主表修改
        if (cscpHxDformTable.getTableId() == null) {
            createCscpHxDformTable(cscpHxDformTable);
            cscpHxDformTableService.createTable(cscpHxDformTable,cscpHxDformTable.getColumns());
        }else {
            // 更新
            cscpHxDformTableService.update(cscpHxDformTable);
            this.syncDatabase(cscpHxDformTable.getTableId());
        }
        // 子表修改
        for (CscpHxDformTable formTable:cscpHxDformTables) {
            //子表设置主表tableId
            formTable.setMainTableId(cscpHxDformTable.getTableId());
            if (formTable.getTableId() == null) {
                if (StringUtils.isEmpty(cscpHxDformTable.getTableId())) {
                    throw new RuntimeException("TableId is null");
                }
                createCscpHxDformTable(formTable);
                cscpHxDformTableService.createTable(formTable,formTable.getColumns());
            }else {
                // 更新
                cscpHxDformTableService.update(formTable);
                this.syncDatabase(formTable.getTableId());
            }
        }
        return ResponseEntity.ok().body("修改成功");
    }


    /**
     * 查询出表单所有信息
     * @param tableId
     * @return
     */
    @GetMapping("/cscpHxFormTablesAll/{tableId}")
    @ApiOperation(value = "获取表单信息（含字段）")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getFormTableAndFields")
    @Log
    public ResponseEntity<CscpHxDformTable> getFormTableAndFields(@PathVariable Long tableId) {
        log.debug("REST request to get CscpHxDformTable : {}", tableId);
        CscpHxDformTable cscpHxDformTable = cscpHxDformTableService.findOneAll(tableId);
        List<CscpHxDformTable> subCscpHxDformTables = new ArrayList<>();
        if (!ObjectUtils.isEmpty(cscpHxDformTable)) {
            subCscpHxDformTables = cscpHxDformTableService.findsubAll(cscpHxDformTable.getTableId());
        }
        if (!ObjectUtils.isEmpty(cscpHxDformTable)) {
            cscpHxDformTable.setCscpHxFormTables(subCscpHxDformTables);
        }else {
            cscpHxDformTable = new CscpHxDformTable();
        }
        return ResponseUtil.wrapOrNotFound(Optional.of(cscpHxDformTable));
    }




    /**
     *
     * 删除单表
     *
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "tableId",value = "the tableId of the CscpHxDformTable to delete")
    })
    @ApiOperation(value = "DELETE  /cscpHxFormTables/tableId : delete the tableId CscpHxDformTable.",notes = "DELETE  /CscpHxDformTables/tableId : delete the tableId CscpHxDformTable.",httpMethod = "DELETE")
    @DeleteMapping("/cscpHxFormTables/{tableId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "deleteCscpHxDformTable")
    @Log
    public ResponseEntity<Void> deleteCscpHxDformTable(@PathVariable Long tableId) {
        log.debug("REST request to delete CscpHxDformTable : {}", tableId);
        cscpHxDformTableService.delete(tableId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, tableId.toString())).build();
    }


    /**
     * 批量删除
     * @param ids
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "BIGINT", name = "ids", value = "the id of the CscpHxDformTables to delete")
    })
    @ApiOperation(value = "DELETE  /cscpHxFormTables : delete the CscpHxDformTables.", notes = "DELETE  /ids : delete the ids .", httpMethod = "DELETE")
    @DeleteMapping("/cscpHxFormTables/delAll")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "deleteCscpHxDformTables")
    @Log
    public ResponseEntity<Void> deleteCscpHxDformTables(Long[] ids) {
        log.debug("REST request to delete CscpHxDformTables : {}", (Object) ids);
        cscpHxDformTableService.deleteByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, HxStringUtils.join(ids))).build();
    }



    /**
     * 校验表名是否存在
     */
    @GetMapping("/validation/cscpHxFormTable")
    @ApiOperation("表名校验")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "checkFormTable")
    @Log
    public AjaxBackData checkFormTable(String name ) {
        long l = cscpListModelDesignService.checkFormTable(name);
        AjaxBackData ajaxBackData = new AjaxBackData();
        if(l > 0 ){
            ajaxBackData.setCode(-1);
            ajaxBackData.setMsg("表名不能重复!");
            throw new BadRequestAlertException("tableName is existed",ENTITY_NAME,"error");
        }else{
            ajaxBackData.setCode(200);
            ajaxBackData.setMsg("校验成功");
        }
        return ajaxBackData;
    }


    /**
     * 生成业务模块表单信息查询
     * cscp_customize_vpage
     */
    @GetMapping("/qryPageTableInfo")
    @ApiOperation("业务模块表单信息查询")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "qryPageTableInfo")
    @Log
    public List<CscpCustomizeVpage> qryPageTableInfo(Long moduleId, Long pageId, String pageType){
        CscpCustomizeVpage cscpCustomizeVpage = new CscpCustomizeVpage();
        cscpCustomizeVpage.setModuleId(moduleId);
        cscpCustomizeVpage.setPageId(pageId);
        cscpCustomizeVpage.setPageType(pageType);
        return cscpCustomizeVpageService.qryPageTableInfo(cscpCustomizeVpage);
    }

    /**
     * 生成业务表单数据查询接口
     */
    @GetMapping("/qryTemplateTable")
    @ApiOperation("生成业务表单数据查询")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "qryTemplateTable")
    @Log
    public PageResult<Map> qryTemplateTable(Long pageId, TemplateEntity templateEntity, Pageable pageable){
        // 根据表名查询数据
        return cscpListModelDesignService.queryTemplatePage(pageId,templateEntity,pageable);
    }


    /**
     * 删除数据库表单
     */
    @GetMapping("/deleteTable")
    @ApiOperation("删除单表")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "deleteTable")
    @Log
    public Map deleteTable(CscpHxDformTable cscpHxDformTable){
        return cscpListModelDesignService.deleteTable(cscpHxDformTable);
    }


    /**
     * Description: 根据模块id查询cscp_customize_module联查cscp_menus 根据menu_id 和id 连接查询
     * Copyright (c) CSII.
     * All Rights Reserved.
     * 2022/10/11 10:43  by xx
     */
    @GetMapping("/qryMenuByModuleId")
    @ApiOperation("根据模块id查询菜单")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "qryMenuByModuleId")
    @Log
    public Map qryMenuByModuleId(String moduleId){
        Map<String,Object> respMap = new HashMap<>(3);
        List<CscpMenus> cscpMenus = cscpMenusService.qryMenuByModuleId(moduleId);
        if (!CollectionUtils.isEmpty(cscpMenus)) {
            respMap.put("code","1");
            respMap.put("message","模块菜单已存在");
            respMap.put("cscpMenus",cscpMenus);
        }else {
            respMap.put("code","0");
            respMap.put("message","模块菜单不存在");
            respMap.put("cscpMenus",cscpMenus);
        }
        return respMap;
    }

    /**
     * Description: 生成所属模块菜单 修改cscp_customize_module表的menu_id为生成菜单的id
     * Copyright (c) CSII.
     * All Rights Reserved.
     * 2022/10/11 10:43  by xx
     */
    @PutMapping("/updateModuleByModuleId")
    @ApiOperation("更新模块表信息")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "updateModuleByModuleId")
    @Log
    public void updateModuleByModuleId(@RequestBody Map<String,Object> map){
        cscpCustomizeModuleService.updateByModuleId(map);
    }


    /**
     *页面跳转，自定义接口，列表查询，列表删除，表单保存，关闭页面，关闭弹框
     *
     */
    @GetMapping("/getButtonEventEnums")
    @ApiOperation("下拉查询所有的按钮绑定事件")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getButtonEventEnums")
    @Log
    public AjaxResult getButtonEventEnums() throws Exception {
        Class<Enum> clazz = null;
        try {
            // 通过完整的类名获取
            clazz = (Class<Enum>)Class.forName("com.ctsi.ssdc.customize.constant.EventEnum");
        } catch (Exception e) {
            return AjaxResult.success();
        }

        //获取所有枚举实例
        Enum[] enumConstants = clazz.getEnumConstants();
        Map<String, String> hashMap = new HashMap<>(10);
        for (Enum enumItem : enumConstants) {
            String code = clazz.getMethod("getCode").invoke(enumItem).toString();
            String message = clazz.getMethod("getMessage").invoke(enumItem).toString();
            hashMap.put(code,message);
        }
        return AjaxResult.success(hashMap);
    }

    /**
     * Description: 导入excel
     * Copyright (c) CSII.
     * All Rights Reserved.
     * 2022/10/25 16:44  by xx
     */
    @PostMapping("/importExcel")
    @ApiOperation("导入excel组件方法")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "importExcel")
    @Log
    public void importExcel(@RequestParam("file") MultipartFile file, TemplateEntity templateEntity){
        cscpListModelDesignService.importExcelData(file,templateEntity);
    }

}
