package com.ctsi.ssdc.gen.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVfield;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVfieldExample;
import com.ctsi.ssdc.gen.service.CscpCustomizeVfieldService;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
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
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CscpCustomizeVfield.
 *
 * @author hx
 * @date 2022-05-23 09:59:35
 *
 */

@Api(value = "/api/lowcode/customize",tags = {"cscp-customize-vfield-controller"})
@RestController
@RequestMapping("/api/lowcode/customize")
public class CscpCustomizeVfieldController {


    private final Logger log = LoggerFactory.getLogger(CscpCustomizeVfieldController.class);

    private static final String ENTITY_NAME = "cscpCustomizeVfield";

    private final CscpCustomizeVfieldService cscpCustomizeVfieldService;

    public CscpCustomizeVfieldController(CscpCustomizeVfieldService cscpCustomizeVfieldService) {
        this.cscpCustomizeVfieldService = cscpCustomizeVfieldService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpCustomizeVfields : Create a new cscpCustomizeVfield.
     *
     * @param cscpCustomizeVfield the cscpCustomizeVfield to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpCustomizeVfield, or with status 400 (Bad Request) if the cscpCustomizeVfield has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpCustomizeVfield",name = "cscpCustomizeVfield",value = "the cscpCustomizeVfield to create")
    })
    @ApiOperation(value = "POST  /cscpCustomizeVfields : create a new cscpCustomizeVfield.",notes = "POST  /cscpCustomizeVfields : create a new cscpCustomizeVfield.",httpMethod = "POST")
    @PostMapping("/cscpCustomizeVfields")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "createCscpCustomizeVfield")
    @Log
    public ResponseEntity<CscpCustomizeVfield> createCscpCustomizeVfield(@RequestBody  CscpCustomizeVfield cscpCustomizeVfield) throws URISyntaxException {

        log.debug("REST request to save CscpCustomizeVfield : {}", cscpCustomizeVfield);
        cscpCustomizeVfield.setCreatedBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeVfield.setCreatedTime(ZonedDateTime.now());
        cscpCustomizeVfield.setTenantId(SecurityHxUtils.getCurrentTenantId());
        cscpCustomizeVfield.setDelFlag("0");
        cscpCustomizeVfield.setStatus("1");
        CscpCustomizeVfield result = cscpCustomizeVfieldService.insert(cscpCustomizeVfield);
        return ResponseEntity.created(new URI("/api/cscpCustomizeVfields/" + result.getFieldId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getFieldId().toString()))
            .body(result);
    }
    /**
     * PUT  /cscpCustomizeVfields : Updates an existing cscpCustomizeVfield.
     *
     * @param cscpCustomizeVfield the cscpCustomizeVfield to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpCustomizeVfield,
     * or with status 400 (Bad Request) if the cscpCustomizeVfield is not valid,
     * or with status 500 (Internal Server Error) if the cscpCustomizeVfield couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpCustomizeVfield",name = "cscpCustomizeVfield",value = "the cscpCustomizeVfield to update")
    })
    @ApiOperation(value = "PUT  /cscpCustomizeVfields : updates an existing cscpCustomizeVfield.",notes = "PUT  /cscpCustomizeVfields : updates an existing cscpCustomizeVfield.",httpMethod = "PUT")
    @PutMapping("/cscpCustomizeVfields")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "updateCscpCustomizeVfield")
    @Log
    public ResponseEntity<CscpCustomizeVfield> updateCscpCustomizeVfield(@RequestBody  CscpCustomizeVfield cscpCustomizeVfield) throws URISyntaxException {

        log.debug("REST request to update CscpCustomizeVfield : {}", cscpCustomizeVfield);

        if (cscpCustomizeVfield.getFieldId() == null) {
            return createCscpCustomizeVfield(cscpCustomizeVfield);
        }
        cscpCustomizeVfield.setUpdateBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeVfield.setUpdateTime(ZonedDateTime.now());
        CscpCustomizeVfield result = cscpCustomizeVfieldService.update(cscpCustomizeVfield);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getFieldId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpCustomizeVfields/:fieldId : get the "fieldId" cscpCustomizeVfield.
     *
     * @param fieldId the id of the cscpCustomizeVfield to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpCustomizeVfield, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "fieldId",value = "the fieldId of the cscpCustomizeVfield to retrieve")
    })
    @ApiOperation(value = "GET  /cscpCustomizeVfields/fieldId : get the fieldId cscpCustomizeVfield.",notes = "GET  /cscpCustomizeVfields/fieldId : get the fieldId cscpCustomizeVfield.",httpMethod = "GET")
    @GetMapping("/cscpCustomizeVfields/{fieldId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getCscpCustomizeVfield")
    @Log
    public ResponseEntity<CscpCustomizeVfield> getCscpCustomizeVfield(@PathVariable Long fieldId) {

        log.debug("REST request to get CscpCustomizeVfield : {}", fieldId);

        CscpCustomizeVfield cscpCustomizeVfield = cscpCustomizeVfieldService.findOne(fieldId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeVfield));
    }

    /**
     * GET  /cscpCustomizeVfields : get the cscpCustomizeVfields .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpCustomizeVfields in body
     */
    @ApiOperation(value = "GET  /cscpCustomizeVfields ")
    @GetMapping("/cscpCustomizeVfields")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getCscpCustomizeVfieldsList")
    @Log
    public PageResult<CscpCustomizeVfield> getCscpCustomizeVfieldsList(CscpCustomizeVfieldExample cscpCustomizeVfieldExample,Pageable pageable) {

        log.debug("REST request to get CscpCustomizeVfieldsList");

        return cscpCustomizeVfieldService.findByExample(cscpCustomizeVfieldExample,pageable);
    }

    /**
     * DELETE  /cscpCustomizeVfields/:fieldId : delete the "fieldId" cscpCustomizeVfield.
     *
     * @param fieldId the id of the cscpCustomizeVfield to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "fieldId",value = "the fieldId of the cscpCustomizeVfield to delete")
    })
    @ApiOperation(value = "DELETE  /cscpCustomizeVfields/fieldId : delete the fieldId cscpCustomizeVfield.",notes = "DELETE  /cscpCustomizeVfields/fieldId : delete the fieldId cscpCustomizeVfield.",httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeVfields/{fieldId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "deleteCscpCustomizeVfield")
    @Log
    public ResponseEntity<Void> deleteCscpCustomizeVfield(@PathVariable Long fieldId) {
        log.debug("REST request to delete CscpCustomizeVfield : {}", fieldId);
        cscpCustomizeVfieldService.deleteById(fieldId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, fieldId.toString())).build();
    }


    /**
     * GET  /cscpCustomizeVfields/:fieldId : get the "fieldId" cscpCustomizeVfield.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cscpCustomizeVfield, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /cscpCustomizeVfields/export : export the cscpCustomizeVfield to excel",notes = "DELETE  /cscpCustomizeVfields/fieldId : delete the fieldId cscpCustomizeVfield.",httpMethod = "DELETE")
    @PostMapping("/cscpCustomizeVfields/export")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "export")
    @Log
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export CscpCustomizeVfield");

        PageResult<CscpCustomizeVfield> result = cscpCustomizeVfieldService.findAll();
        List<CscpCustomizeVfield> list = result.getData();
        ExcelUtil<CscpCustomizeVfield> util = new ExcelUtil<CscpCustomizeVfield>(CscpCustomizeVfield.class);
        return util.exportExcel(list, "cscpCustomizeVfield");
    }


    /**
    * DELETE  /fieldIds : delete the cscpCustomizeVfield.", notes = "DELETE  /fieldIds : delete the fieldIds.", httpMethod = "DELETE"
    *
    * @return
    */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "fieldIds", value = "the fieldIds of the cscpCustomizeVfield to delete")
    })
    @ApiOperation(value = "DELETE  /fieldIds : delete the cscpCustomizeVfield.", notes = "DELETE  /fieldIds : delete the fieldIds.", httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeVfields/delAll")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "deleteCscpCustomizeVfield")
    @Log
    public ResponseEntity<Void> deleteCscpCustomizeVfield(Long[] fieldIds) {

        log.debug("REST request to delete fieldIds");

        cscpCustomizeVfieldService.deleteByIds(fieldIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(fieldIds))).build();
    }

    /**
     *  批量新增
     */
    @ApiOperation(value = "批量新增",httpMethod = "POST")
    @PostMapping("/cscpCustomizeVfields/save")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "saveCscpCustomizeVfields")
    @Log
    public AjaxResult saveCscpCustomizeVfields(@RequestBody  List<CscpCustomizeVfield> cscpCustomizeVfields) throws URISyntaxException {
        log.debug("REST request to save CscpCustomizeVfield : {}", cscpCustomizeVfields);
        cscpCustomizeVfieldService.saveCscpCustomizeVfields(cscpCustomizeVfields);
        return AjaxResult.success();
    }

    /**
     *  批量修改
     */
    @ApiOperation(value = "批量修改",httpMethod = "PUT")
    @PostMapping("/cscpCustomizeVfields/update")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "updateCscpCustomizeVfields")
    @Log
    public AjaxResult updateCscpCustomizeVfields(@RequestBody  List<CscpCustomizeVfield> cscpCustomizeVfields) throws URISyntaxException {
        log.debug("REST request to update CscpCustomizeVfield : {}", cscpCustomizeVfields);
        cscpCustomizeVfieldService.updateCscpCustomizeVfields(cscpCustomizeVfields);
        return AjaxResult.success();
    }

    /**
     * 根据formId查询字段列表
     */
    @ApiOperation(value = "根据formId查询字段列表",httpMethod = "GET")
    @GetMapping("/cscpCustomizeVfields/getListByFormId/{formId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getListByFormId")
    @Log
    public AjaxResult getListByFormId(@PathVariable Long formId){
        List<CscpCustomizeVfield> vfields = cscpCustomizeVfieldService.getListByFormId(formId);
        return AjaxResult.success(vfields);

    }

    /**
     * 根据多个formId查询字段列表(目前用于子表查询场景)
     */
    @ApiOperation(value = "根据formId查询字段列表",httpMethod = "GET")
    @GetMapping("/cscpCustomizeVfields/getListByFormIds/{formIds}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getListByFormIds")
    @Log
    public AjaxResult getListByFormIds(@PathVariable Long[] formIds){
        log.info("REST request to update getListByFormIds : {}", formIds);
        List<CscpCustomizeVfield> vfields = cscpCustomizeVfieldService.getListByFormIds(formIds);
        return AjaxResult.success(vfields);
    }
}
