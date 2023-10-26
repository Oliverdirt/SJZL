package com.ctsi.ssdc.gen.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.gen.domain.CscpHxFormSuite;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.domain.CscpHxFormTableExample;
import com.ctsi.ssdc.gen.service.CscpHxFormSuiteService;
import com.ctsi.ssdc.gen.service.CscpHxFormTableService;
import com.ctsi.ssdc.model.AjaxBackData;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import com.ctsi.ssdc.utils.HxStringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * REST controller for managing CscpHxFormTable.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(value = "/api",tags = {"cscp-hx-form-table-controller"})
@RestController
@RequestMapping("/api")
public class CscpHxFormTableController {

    private final Logger log = LoggerFactory.getLogger(CscpHxFormTableController.class);

    private static final String ENTITY_NAME = "cscpHxFormTable";

    private final CscpHxFormTableService cscpHxFormTableService;

    private final CscpHxFormSuiteService cscpHxFormSuiteService;

    public CscpHxFormTableController(CscpHxFormTableService cscpHxFormTableService, CscpHxFormSuiteService cscpHxFormSuiteService) {
        this.cscpHxFormTableService = cscpHxFormTableService;
        this.cscpHxFormSuiteService = cscpHxFormSuiteService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
   
    /**
     * POST  /cscpHxFormTables : Create a new cscpHxFormTable.
     *
     * @param cscpHxFormTable the cscpHxFormTable to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpHxFormTable, or with status 400 (Bad Request) if the cscpHxFormTable has already an tableId
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxFormTable",name = "cscpHxFormTable",value = "the cscpHxFormTable to create")
    })
    @ApiOperation(value = "POST  /cscpHxFormTables : create a new cscpHxFormTable.",notes = "POST  /cscpHxFormTables : create a new cscpHxFormTable.",httpMethod = "POST")
    @PostMapping("/cscpHxFormTables")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "createCscpHxFormTable")
    @Log
    public ResponseEntity<Long> createCscpHxFormTable(@RequestBody CscpHxFormTable cscpHxFormTable) throws URISyntaxException {
        log.debug("REST request to save CscpHxFormTable : {}", cscpHxFormTable);
        if (cscpHxFormTable.getTableId() != null) {
            throw new BadRequestAlertException("A new cscpHxFormTable cannot already have an id", ENTITY_NAME, "idexists");
        }
        CscpHxFormTable result = cscpHxFormTableService.insert(cscpHxFormTable);
        return ResponseEntity.created(new URI("/api/cscpHxFormTables" + "/" +result.getTableId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getTableId().toString()))
            .body(result.getTableId());
    }


	
    /**
     * PUT  /cscpHxFormTables : Updates an existing cscpHxFormTable.
     *
     * @param cscpHxFormTable the cscpHxFormTable to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpHxFormTable,
     * or with status 400 (Bad Request) if the cscpHxFormTable is not valid,
     * or with status 500 (Internal Server Error) if the cscpHxFormTable couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxFormTable",name = "cscpHxFormTable",value = "the cscpHxFormTable to update")
    })
    @ApiOperation(value = "PUT  /cscpHxFormTables : updates an existing cscpHxFormTable.",notes = "PUT  /cscpHxFormTables : updates an existing cscpHxFormTable.",httpMethod = "PUT")
    @PutMapping("/cscpHxFormTables")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "updateCscpHxFormTable")
    @Log
    public ResponseEntity<Long> updateCscpHxFormTable(@RequestBody CscpHxFormTable cscpHxFormTable) throws URISyntaxException  {
        log.debug("REST request to update CscpHxFormTable : {}", cscpHxFormTable);
        if (cscpHxFormTable.getTableId() == null) {
            return createCscpHxFormTable(cscpHxFormTable);
        }
        cscpHxFormTable.setIsDbSynch(0);
        CscpHxFormTable result = cscpHxFormTableService.update(cscpHxFormTable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getTableId().toString()))
            .body(result.getTableId());
    }

    /**
     * GET  /cscpHxFormTables/:tableId : get the "tableId" cscpHxFormTable.
     *
     * @param tableId the id of the cscpHxFormTable to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpHxFormTable, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "tableId",value = "the tableId of the cscpHxFormTable to retrieve")
    })
    @ApiOperation(value = "获取表单信息（不含字段）")
    @GetMapping("/cscpHxFormTables/{tableId}")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getCscpHxFormTable")
    @Log
    public ResponseEntity<CscpHxFormTable> getCscpHxFormTable(@PathVariable Long tableId) {
        log.debug("REST request to get CscpHxFormTable : {}", tableId);
        CscpHxFormTable cscpHxFormTable = cscpHxFormTableService.findOne(tableId);
        CscpHxFormSuite formSuite = cscpHxFormSuiteService.findOne(cscpHxFormTable.getFormSuite());
        if ( null != formSuite) {
            cscpHxFormTable.setCscpHxFormSuite(formSuite);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpHxFormTable));
    }

    /**
     * 查询出表单所有信息
     * @param tableId
     * @return
     */
    @GetMapping("/cscpHxFormTablesAll/{tableId}")
    @ApiOperation(value = "获取表单信息（含字段）")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getFormTableAndFields")
    @Log
    public ResponseEntity<CscpHxFormTable> getFormTableAndFields(@PathVariable Long tableId) {
        log.debug("REST request to get CscpHxFormTable : {}", tableId);
        CscpHxFormTable cscpHxFormTable = cscpHxFormTableService.findOneAll(tableId);
        cscpHxFormTable.setCscpHxFormSuite(cscpHxFormSuiteService.findOne(cscpHxFormTable.getFormSuite()));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpHxFormTable));
    }


    /**
    * GET  /cscpHxFormTables : get the cscpHxFormTables.
    *
    * @return the ResponseEntity with status 200 (OK) and the list of cscpHxFormTables in body
    */
    @ApiOperation(value = "GET  /cscpHxFormTables : get the cscpHxFormTables .",notes = "GET  /cscpHxFormTables : get the cscpHxFormTables.",httpMethod = "GET")
    @GetMapping("/cscpHxFormTables")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getCscpHxFormTablesList")
    @Log
    public PageResult<CscpHxFormTable> getCscpHxFormTablesList(CscpHxFormTableExample cscpHxFormTableExample, Pageable pageable) {
        log.debug("REST request to get CscpHxFormTablesList");
        PageResult<CscpHxFormTable> result = cscpHxFormTableService.findByExample(cscpHxFormTableExample, pageable);
        for (CscpHxFormTable cscpHxFormTable : result.getData()){
            CscpHxFormSuite formSuite = cscpHxFormSuiteService.findOne(cscpHxFormTable.getFormSuite());
            if ( null != formSuite) {
                cscpHxFormTable.setCscpHxFormSuite(formSuite);
            }
        }
        return  result;
    }

    /**
     * GET  /cscpHxAllSubFormTables : get the cscpHxAllSubFormTables.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpHxAllSubFormTables in body
     */
    @ApiOperation(value = "GET  /cscpHxAllSubFormTables : get the cscpHxAllSubFormTables.",
            notes = "GET  /cscpHxAllSubFormTables : get the cscpHxAllSubFormTables .",httpMethod = "GET")
    @GetMapping("/cscpHxAllSubFormTables")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getcscpHxAllSubFormTables")
    @Log
    public PageResult<CscpHxFormTable> getCscpHxAllSubFormTables(String tableName) {

        log.debug("REST request to get cscpHxAllSubFormTables");
        PageResult<CscpHxFormTable> result = cscpHxFormTableService.findAll();

        List<CscpHxFormTable> allFormTables = result.getData();
        if (CollectionUtils.isEmpty(allFormTables)) {
            log.debug("have not any formTable");
        }
        // 查询所有表，将主表本身排除
        if (StringUtils.isNotBlank(tableName)) {
            List<CscpHxFormTable> allFilterFormTables = allFormTables.stream().filter(cscpHxFormTable 
                    -> !Objects.equals(tableName, cscpHxFormTable.getTableName())).collect(Collectors.toList());
            result.setData(allFilterFormTables);
            result.setRecordsFiltered(result.getRecordsFiltered() - 1);
        }

        return  result;
    }


    /**
     * DELETE  /cscpHxFormTables/:tableId : delete the "tableId" cscpHxFormTable.
     *
     * @param tableId the id of the cscpHxFormTable to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "tableId",value = "the tableId of the cscpHxFormTable to delete")
    })
    @ApiOperation(value = "DELETE  /cscpHxFormTables/tableId : delete the tableId cscpHxFormTable.",notes = "DELETE  /cscpHxFormTables/tableId : delete the tableId cscpHxFormTable.",httpMethod = "DELETE")
    @DeleteMapping("/cscpHxFormTables/{tableId}")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "deleteCscpHxFormTable")
    @Log
    public ResponseEntity<Void> deleteCscpHxFormTable(@PathVariable Long tableId) {
        log.debug("REST request to delete CscpHxFormTable : {}", tableId);
        cscpHxFormTableService.delete(tableId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, tableId.toString())).build();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "BIGINT", name = "ids", value = "the id of the cscpHxFormTables to delete")
    })
    @ApiOperation(value = "DELETE  /cscpHxFormTables : delete the cscpHxFormTables.", notes = "DELETE  /ids : delete the ids .", httpMethod = "DELETE")
    @DeleteMapping("/cscpHxFormTables/delAll")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "deleteCscpHxFormTables")
    @Log
    public ResponseEntity<Void> deleteCscpHxFormTables(Long[] ids) {
        log.debug("REST request to delete CscpHxFormTables : {}", (Object) ids);
        cscpHxFormTableService.deleteByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, HxStringUtils.join(ids))).build();
    }




    /**
    * GET  /cscpHxFormTables/:tableId : get the "tableId" cscpHxFormTable.
    *
    * @return the ResponseEntity with status 200 (OK) and with body the cscpHxFormTable, or with status 404 (Not Found)
    */
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "tableId",value = "the tableId of the cscpHxFormTable to retrieve")
    })
    @ApiOperation(value = "导出列表数据",notes = "POST  /cscpHxFormTables/export : export the cscpHxFormTable to excel",httpMethod = "POST")
    @PostMapping("/cscpHxFormTables/export")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "exportHxFormTables")
    @Log
    public ResponseEntity<byte[]> export() {
        log.debug("REST request to export CscpHxFormTable");
        PageResult<CscpHxFormTable> result = cscpHxFormTableService.findAll();
        List<CscpHxFormTable> list = result.getData();
        ExcelUtil<CscpHxFormTable> util = new ExcelUtil<CscpHxFormTable>(CscpHxFormTable.class);
        return util.exportExcel(list, "cscpHxFormTable");
    }


    /**
     * 同步数据库
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/cscpHxsync/{id}")
    @ApiOperation(value = "同步表单信息至数据库")
//    @BeanExposeMethodAble(component = "biyi-hx-form",method = "")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "syncDatabase")
//    @ApiImplicitParam(paramType="path",name = "id",value = "表单id",required = true,dataType = "Integer")
    @Log
    public ResponseEntity<CscpHxFormTable> syncDatabase(@PathVariable Long id) {
        log.debug("REST request to sync cscpHxFormTable : {}", id);
        CscpHxFormTable cscpHxFormTable = cscpHxFormTableService.syncDatabase(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cscpHxFormTable.getTableId().toString()))
                .body(cscpHxFormTable);
    }


    /**
     * 表名校验，不能重复
     * @param name
     * @return
     */

    @GetMapping("/validation/cscpHxFormTable")
//    @BeanExposeMethodAble(component = "biyi-hx-form",method = "")
    @ApiOperation("表名校验")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "checkFormTable")
    @Log
    public AjaxBackData checkFormTable(String name )  {
        CscpHxFormTableExample cscpHxFormTableExample = new CscpHxFormTableExample();
        CscpHxFormTableExample.Criteria critieria = cscpHxFormTableExample.createCriteria();
        if (StringUtils.isNotBlank(name)) {
            critieria.andTableNameEqualTo(name);
        }
        long l = cscpHxFormTableService.countByExample(cscpHxFormTableExample);
        AjaxBackData ajaxBackData = new AjaxBackData();
        if(l > 0 ){
            ajaxBackData.setCode(-1);
            ajaxBackData.setMsg("表名不能重复!");
        }else{
            ajaxBackData.setCode(200);
            ajaxBackData.setMsg("校验成功");
        }
        return ajaxBackData;
    }


}
