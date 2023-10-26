package com.ctsi.ssdc.customize.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.customize.domain.CscpCustomizeVfieldPage;
import com.ctsi.ssdc.customize.domain.CscpCustomizeVfieldPageExample;
import com.ctsi.ssdc.gen.service.CscpCustomizeTemplateService;
import com.ctsi.ssdc.customize.service.CscpCustomizeVfieldPageService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

@Api(value = "/api/lowcode/customize",tags = {"cscp-customize-vfield-page-controller"})
@RestController
@RequestMapping("/api/lowcode/customize")

public class CscpCustomizeVfieldPageController {


    private final Logger log = LoggerFactory.getLogger(CscpCustomizeVfieldPageController.class);

    private static final String ENTITY_NAME = "cscpCustomizeVfieldPage";

    private final CscpCustomizeVfieldPageService cscpCustomizeVfieldPageService;

    @Autowired
    private CscpCustomizeTemplateService cscpCustomizeTemplateService;

    public CscpCustomizeVfieldPageController(CscpCustomizeVfieldPageService cscpCustomizeVfieldPageService) {
        this.cscpCustomizeVfieldPageService = cscpCustomizeVfieldPageService;
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
        @ApiImplicitParam(paramType = "body",dataType = "CscpCustomizeVfieldPage",name = "cscpCustomizeVfield",value = "the cscpCustomizeVfield to create")
    })
    @ApiOperation(value = "POST  /cscpCustomizeVfieldPages : create a new cscpCustomizeVfield.",notes = "POST  /cscpCustomizeVfields : create a new cscpCustomizeVfield.",httpMethod = "POST")
    @PostMapping("/cscpCustomizeVfieldPages")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "createCscpCustomizeVfield")
    @Log
    public ResponseEntity<CscpCustomizeVfieldPage> createCscpCustomizeVfieldPage(@RequestBody  CscpCustomizeVfieldPage cscpCustomizeVfield) throws URISyntaxException {

        log.debug("REST request to save CscpCustomizeVfield : {}", cscpCustomizeVfield);
        cscpCustomizeVfield.setCreatedBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeVfield.setCreatedTime(ZonedDateTime.now());
        cscpCustomizeVfield.setTenantId(SecurityHxUtils.getCurrentTenantId());
        cscpCustomizeVfield.setDelFlag("0");
        cscpCustomizeVfield.setStatus("1");
        CscpCustomizeVfieldPage result = cscpCustomizeVfieldPageService.insert(cscpCustomizeVfield);
        return ResponseEntity.created(new URI("/api/cscpCustomizeVfieldPages/" + result.getFieldId() ))
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
            @ApiImplicitParam(paramType = "body",dataType = "CscpCustomizeVfieldPage",name = "cscpCustomizeVfield",value = "the cscpCustomizeVfield to update")
    })
    @ApiOperation(value = "PUT  /cscpCustomizeVfieldPages : updates an existing cscpCustomizeVfield.",notes = "PUT  /cscpCustomizeVfields : updates an existing cscpCustomizeVfield.",httpMethod = "PUT")
    @PutMapping("/cscpCustomizeVfieldPages")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "updateCscpCustomizeVfield")
    @Log
    public ResponseEntity<CscpCustomizeVfieldPage> updateCscpCustomizeVfieldPage(@RequestBody  CscpCustomizeVfieldPage cscpCustomizeVfield) throws URISyntaxException {

        log.debug("REST request to update CscpCustomizeVfieldPage : {}", cscpCustomizeVfield);

        if (cscpCustomizeVfield.getFieldId() == null) {
            return createCscpCustomizeVfieldPage(cscpCustomizeVfield);
        }
        cscpCustomizeVfield.setUpdateBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeVfield.setUpdateTime(ZonedDateTime.now());
        CscpCustomizeVfieldPage result = cscpCustomizeVfieldPageService.update(cscpCustomizeVfield);
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
    @ApiOperation(value = "GET  /cscpCustomizeVfieldPages/fieldId : get the fieldId cscpCustomizeVfield.",notes = "GET  /cscpCustomizeVfields/fieldId : get the fieldId cscpCustomizeVfield.",httpMethod = "GET")
    @GetMapping("/cscpCustomizeVfieldPages/{fieldId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getCscpCustomizeVfield")
    @Log
    public ResponseEntity<CscpCustomizeVfieldPage> getCscpCustomizeVfield(@PathVariable Long fieldId) {

        log.debug("REST request to get CscpCustomizeVfieldPage : {}", fieldId);

        CscpCustomizeVfieldPage cscpCustomizeVfield = cscpCustomizeVfieldPageService.findOne(fieldId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeVfield));
    }

    /**
     * GET  /cscpCustomizeVfields : get the cscpCustomizeVfields .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpCustomizeVfields in body
     */
    @ApiOperation(value = "GET  /cscpCustomizeVfields ")
    @GetMapping("/cscpCustomizeVfieldPages")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getCscpCustomizeVfieldsList")
    @Log
    public PageResult<CscpCustomizeVfieldPage> getCscpCustomizeVfieldsList(CscpCustomizeVfieldPageExample cscpCustomizeVfieldExample,Pageable pageable) {

        log.debug("REST request to get CscpCustomizeVfieldsList");

        return cscpCustomizeVfieldPageService.findByExample(cscpCustomizeVfieldExample,pageable);
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
    @ApiOperation(value = "DELETE  /cscpCustomizeVfieldPages/fieldId : delete the fieldId cscpCustomizeVfield.",notes = "DELETE  /cscpCustomizeVfields/fieldId : delete the fieldId cscpCustomizeVfield.",httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeVfieldPages/{fieldId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "deleteCscpCustomizeVfield")
    @Log
    public ResponseEntity<Void> deleteCscpCustomizeVfield(@PathVariable Long fieldId) {
        log.debug("REST request to delete CscpCustomizeVfieldPage : {}", fieldId);
        cscpCustomizeVfieldPageService.deleteById(fieldId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, fieldId.toString())).build();
    }


    /**
     * GET  /cscpCustomizeVfields/:fieldId : get the "fieldId" cscpCustomizeVfield.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cscpCustomizeVfield, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /cscpCustomizeVfields/export : export the cscpCustomizeVfield to excel",notes = "DELETE  /cscpCustomizeVfields/fieldId : delete the fieldId cscpCustomizeVfield.",httpMethod = "DELETE")
    @PostMapping("/cscpCustomizeVfieldPages/export")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "export")
    @Log
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export CscpCustomizeVfield");

        PageResult<CscpCustomizeVfieldPage> result = cscpCustomizeVfieldPageService.findAll();
        List<CscpCustomizeVfieldPage> list = result.getData();
        ExcelUtil<CscpCustomizeVfieldPage> util = new ExcelUtil<CscpCustomizeVfieldPage>(CscpCustomizeVfieldPage.class);
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
    @DeleteMapping("/cscpCustomizeVfieldPages/delAll")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "deleteCscpCustomizeVfield")
    @Log
    public ResponseEntity<Void> deleteCscpCustomizeVfieldPage(Long[] fieldIds) {

        log.debug("REST request to delete fieldIds");

        cscpCustomizeVfieldPageService.deleteByIds(fieldIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(fieldIds))).build();
    }

    /**
     *  批量新增
     */
    @ApiOperation(value = "批量新增",httpMethod = "POST")
    @PostMapping("/cscpCustomizeVfieldPages/save")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "saveCscpCustomizeVfields")
    @Log
    public AjaxResult saveCscpCustomizeVfields(@RequestBody  List<CscpCustomizeVfieldPage> cscpCustomizeVfields) {
        log.debug("REST request to save CscpCustomizeVfieldPage : {}", cscpCustomizeVfields);
        cscpCustomizeVfieldPageService.saveCscpCustomizeVfieldPages(cscpCustomizeVfields);
        return AjaxResult.success();
    }

    /**
     *  批量修改
     */
    @ApiOperation(value = "批量修改",httpMethod = "PUT")
    @PostMapping("/cscpCustomizeVfieldPages/update")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "updateCscpCustomizeVfields")
    @Log
    public AjaxResult updateCscpCustomizeVfields(@RequestBody  List<CscpCustomizeVfieldPage> cscpCustomizeVfields) {
        log.debug("REST request to update CscpCustomizeVfield : {}", cscpCustomizeVfields);
        cscpCustomizeVfieldPageService.updateCscpCustomizeVfieldPages(cscpCustomizeVfields);
        return AjaxResult.success();
    }

    /**
     * 根据formId查询字段列表
     */
    @ApiOperation(value = "根据pageId查询字段列表",httpMethod = "GET")
    @GetMapping("/cscpCustomizeVfields/getListByPageId/{pageId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getListByPageId")
    @Log
    public ResponseEntity<List> getListByFormId(@PathVariable Long pageId){
        List<CscpCustomizeVfieldPage> vFields = cscpCustomizeVfieldPageService.getListByPageId(pageId);
        return ResponseEntity.ok().body(vFields);

    }

    /**
     * 根据多个pageIds查询字段列表(目前用于子表查询场景)
     */
    @ApiOperation(value = "根据pageIds查询字段列表",httpMethod = "GET")
    @GetMapping("/cscpCustomizeVfields/getListByPageIds/{pageIds}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getListByFormIds")
    @Log
    public AjaxResult getListByFormIds(@PathVariable Long[] pageIds){
        log.info("REST request to update getListByPageIds : {}", pageIds);
        List<CscpCustomizeVfieldPage> vfields = cscpCustomizeVfieldPageService.getListByPageIds(pageIds);
        return AjaxResult.success(vfields);
    }

    @PostMapping("/import")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "importTemplate")
    @ApiOperation("导入excel")
    public AjaxResult importTemplte(@RequestParam(value = "file") MultipartFile file, Long formId) throws Exception {
        return cscpCustomizeTemplateService.taskUploadExcel(file, formId);
    }
}
