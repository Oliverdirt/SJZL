package com.ctsi.ssdc.gen.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVform;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVformExample;
import com.ctsi.ssdc.gen.service.CscpCustomizeVformService;
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
 * REST controller for managing CscpCustomizeVform.
 *
 * @author hx
 * @date 2022-05-23 09:59:33
 *
 */

@Api(value = "/api/lowcode/customize",tags = {"cscp-customize-vform-controller"})
@RestController
@RequestMapping("/api/lowcode/customize")
public class CscpCustomizeVformController {


    private final Logger log = LoggerFactory.getLogger(CscpCustomizeVformController.class);

    private static final String ENTITY_NAME = "cscpCustomizeVform";

    private final CscpCustomizeVformService cscpCustomizeVformService;

    public CscpCustomizeVformController(CscpCustomizeVformService cscpCustomizeVformService) {
        this.cscpCustomizeVformService = cscpCustomizeVformService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpCustomizeVforms : Create a new cscpCustomizeVform.
     *
     * @param cscpCustomizeVform the cscpCustomizeVform to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpCustomizeVform, or with status 400 (Bad Request) if the cscpCustomizeVform has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpCustomizeVform",name = "cscpCustomizeVform",value = "the cscpCustomizeVform to create")
    })
    @ApiOperation(value = "POST  /cscpCustomizeVforms : create a new cscpCustomizeVform.",notes = "POST  /cscpCustomizeVforms : create a new cscpCustomizeVform.",httpMethod = "POST")
    @PostMapping("/cscpCustomizeVforms")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "createCscpCustomizeVform")
    @Log
    public ResponseEntity<CscpCustomizeVform> createCscpCustomizeVform(@RequestBody  CscpCustomizeVform cscpCustomizeVform) throws URISyntaxException {

        log.debug("REST request to save CscpCustomizeVform : {}", cscpCustomizeVform);
        cscpCustomizeVform.setCreatedBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeVform.setCreatedTime(ZonedDateTime.now());
        cscpCustomizeVform.setTenantId(SecurityHxUtils.getCurrentTenantId());
        cscpCustomizeVform.setDelFlag("0");
        cscpCustomizeVform.setStatus("1");
        CscpCustomizeVform result = cscpCustomizeVformService.insert(cscpCustomizeVform);
        return ResponseEntity.created(new URI("/api/cscpCustomizeVforms/" + result.getFormId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getFormId().toString()))
            .body(result);
    }
    /**
     * PUT  /cscpCustomizeVforms : Updates an existing cscpCustomizeVform.
     *
     * @param cscpCustomizeVform the cscpCustomizeVform to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpCustomizeVform,
     * or with status 400 (Bad Request) if the cscpCustomizeVform is not valid,
     * or with status 500 (Internal Server Error) if the cscpCustomizeVform couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpCustomizeVform",name = "cscpCustomizeVform",value = "the cscpCustomizeVform to update")
    })
    @ApiOperation(value = "PUT  /cscpCustomizeVforms : updates an existing cscpCustomizeVform.",notes = "PUT  /cscpCustomizeVforms : updates an existing cscpCustomizeVform.",httpMethod = "PUT")
    @PutMapping("/cscpCustomizeVforms")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "updateCscpCustomizeVform")
    @Log
    public ResponseEntity<CscpCustomizeVform> updateCscpCustomizeVform(@RequestBody  CscpCustomizeVform cscpCustomizeVform) throws URISyntaxException {

        log.debug("REST request to update CscpCustomizeVform : {}", cscpCustomizeVform);

        if (cscpCustomizeVform.getFormId() == null) {
            return createCscpCustomizeVform(cscpCustomizeVform);
        }
        cscpCustomizeVform.setUpdateBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeVform.setUpdateTime(ZonedDateTime.now());
        CscpCustomizeVform result = cscpCustomizeVformService.update(cscpCustomizeVform);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getFormId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpCustomizeVforms/:formId : get the "formId" cscpCustomizeVform.
     *
     * @param formId the id of the cscpCustomizeVform to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpCustomizeVform, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "formId",value = "the formId of the cscpCustomizeVform to retrieve")
    })
    @ApiOperation(value = "GET  /cscpCustomizeVforms/formId : get the formId cscpCustomizeVform.",notes = "GET  /cscpCustomizeVforms/formId : get the formId cscpCustomizeVform.",httpMethod = "GET")
    @GetMapping("/cscpCustomizeVforms/{formId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getCscpCustomizeVform")
    @Log
    public ResponseEntity<CscpCustomizeVform> getCscpCustomizeVform(@PathVariable Long formId) {

        log.debug("REST request to get CscpCustomizeVform : {}", formId);

        CscpCustomizeVform cscpCustomizeVform = cscpCustomizeVformService.findOne(formId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeVform));
    }


    /**
     * 根据主表formId查询子表form表信息
     * @param formId 主表formId
     * @return  子表form表信息
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "formId",value = "the formId of the cscpCustomizeVform to retrieve")
    })
    @ApiOperation(value = "GET  /childCscpCustomizeVforms/formId : get the formId chicscpCustomizeVform.",notes = "GET  /cscpCustomizeVforms/formId : get the formId cscpCustomizeVform.",httpMethod = "GET")
    @GetMapping("/childCscpCustomizeVforms/{formId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getChildCscpCustomizeVforms")
    @Log
    public ResponseEntity<List<CscpCustomizeVform>> getChildCscpCustomizeVforms(@PathVariable Long[] formId) {
        log.debug("REST request to get childCscpCustomizeVforms : {}", formId);
        List<CscpCustomizeVform> cscpCustomizeVform = cscpCustomizeVformService.queryChildCscpCustomizeVforms(formId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeVform));
    }

    /**
     * 根据子表formId查询主表信息
     * @param formId 主表formId
     * @return  子表form表信息
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "formId",value = "the formId of the cscpCustomizeVform to retrieve")
    })
    @ApiOperation(value = "GET  /getMainCscpCustomizeVform/formId : get the  MaincscpCustomizeVform.",notes = "GET  /getMainCscpCustomizeVform/formId : get the  MainCscpCustomizeVform.",httpMethod = "GET")
    @GetMapping("/getMainCscpCustomizeVform/{formId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getMainCscpCustomizeVform")
    @Log
    public ResponseEntity<CscpCustomizeVform> getMainCscpCustomizeVform(@PathVariable Long formId) {
        log.debug("REST request to get getMainCscpCustomizeVform : {}", formId);
        CscpCustomizeVform cscpCustomizeVform= cscpCustomizeVformService.getMainCscpCustomizeVform(formId);
//        List<CscpCustomizeVform> cscpCustomizeVform = cscpCustomizeVformService.queryChildCscpCustomizeVforms(formId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeVform));
    }


    /**
     * GET  /getCscpCustomizeVformByName
     *
     * @param formName the formName of the cscpCustomizeVform to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpCustomizeVform, or with status 404 (Not Found)
     */
    @ApiOperation(value = "GET  /getCscpCustomizeVformByName : get the cscpCustomizeVform by formName.",notes = "GET  /getCscpCustomizeVformByName",httpMethod = "GET")
    @GetMapping("/getCscpCustomizeVformByName")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getCscpCustomizeVformByName")
    @Log
    public ResponseEntity<List<CscpCustomizeVform>> getCscpCustomizeVformByName(String formName) {

        log.debug("REST request to get CscpCustomizeVform : {}", formName);
        CscpCustomizeVformExample cscpCustomizeVformExample = new CscpCustomizeVformExample();
        cscpCustomizeVformExample.createCriteria().andFormNameEqualTo(formName);
        List<CscpCustomizeVform> cscpCustomizeVform = cscpCustomizeVformService.findByExample(cscpCustomizeVformExample).getData();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeVform));
    }



    /**
     * GET  /cscpCustomizeVforms : get the cscpCustomizeVforms .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpCustomizeVforms in body
     */
    @ApiOperation(value = "GET  /cscpCustomizeVforms ")
    @GetMapping("/cscpCustomizeVforms")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getCscpCustomizeVformsList")
    @Log
    public PageResult<CscpCustomizeVform> getCscpCustomizeVformsList(CscpCustomizeVformExample cscpCustomizeVformExample,Pageable pageable) {

        log.debug("REST request to get CscpCustomizeVformsList");

        return cscpCustomizeVformService.findByExample(cscpCustomizeVformExample,pageable);
    }

    /**
     * DELETE  /cscpCustomizeVforms/:formId : delete the "formId" cscpCustomizeVform.
     *
     * @param formId the id of the cscpCustomizeVform to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "formId",value = "the formId of the cscpCustomizeVform to delete")
    })
    @ApiOperation(value = "DELETE  /cscpCustomizeVforms/formId : delete the formId cscpCustomizeVform.",notes = "DELETE  /cscpCustomizeVforms/formId : delete the formId cscpCustomizeVform.",httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeVforms/{formId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "deleteCscpCustomizeVform")
    @Log
    public AjaxResult deleteCscpCustomizeVform(@PathVariable Long formId) {
        log.debug("REST request to delete CscpCustomizeVform : {}", formId);
        return cscpCustomizeVformService.deleteById(formId);
    }


    /**
     * GET  /cscpCustomizeVforms/:formId : get the "formId" cscpCustomizeVform.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cscpCustomizeVform, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /cscpCustomizeVforms/export : export the cscpCustomizeVform to excel",notes = "DELETE  /cscpCustomizeVforms/formId : delete the formId cscpCustomizeVform.",httpMethod = "DELETE")
    @PostMapping("/cscpCustomizeVforms/export")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "export")
    @Log
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export CscpCustomizeVform");

        PageResult<CscpCustomizeVform> result = cscpCustomizeVformService.findAll();
        List<CscpCustomizeVform> list = result.getData();
        ExcelUtil<CscpCustomizeVform> util = new ExcelUtil<CscpCustomizeVform>(CscpCustomizeVform.class);
        return util.exportExcel(list, "cscpCustomizeVform");
    }


    /**
    * DELETE  /formIds : delete the cscpCustomizeVform.", notes = "DELETE  /formIds : delete the formIds.", httpMethod = "DELETE"
    *
    * @return
    */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "formIds", value = "the formIds of the cscpCustomizeVform to delete")
    })
    @ApiOperation(value = "DELETE  /formIds : delete the cscpCustomizeVform.", notes = "DELETE  /formIds : delete the formIds.", httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeVforms/delAll")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "deleteCscpCustomizeVform")
    @Log
    public ResponseEntity<Void> deleteCscpCustomizeVform(Long[] formIds) {

        log.debug("REST request to delete formIds");

        cscpCustomizeVformService.deleteByIds(formIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(formIds))).build();
    }


    /**
     * 根据formId查询,赋值feilds
     */
    @GetMapping("/queryCscpCustomizeVform/{formId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "createTemplate")
    @Log
    public AjaxResult createTemplate(@PathVariable("formId") Long formId){
        CscpCustomizeVform cscpCustomizeVform = cscpCustomizeVformService.queryCscpCustomizeVform(formId);
        return AjaxResult.success(cscpCustomizeVform);
    }


    @ApiOperation(value = "获取单表、主表列表")
    @GetMapping("/cscpCustomizeVforms/getList")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getCscpCustomizeVformsPageList")
    @Log
    public PageResult<CscpCustomizeVform> getCscpCustomizeVformsPageList(CscpCustomizeVform form,Pageable pageable) {
        log.debug("REST request to get CscpCustomizeVformsList");
        return cscpCustomizeVformService.getCscpCustomizeVformsPageList(form,pageable);
    }

    @ApiOperation(value = "获取流程表选择列表")
    @GetMapping("/cscpCustomizeVforms/listAll")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getCscpCustomizeVformsListAll")
    @Log
    public List<CscpCustomizeVform> getCscpCustomizeVformsListAll() {
        log.debug("REST request to get CscpCustomizeVformsListAll");
        return cscpCustomizeVformService.getCscpCustomizeVformsListAll();
    }


    @ApiOperation(value = "获取流程表一个选择列表")
    @GetMapping("/cscpCustomizeVforms/listOne")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getCscpCustomizeVformsByFormName")
    @Log
    public CscpCustomizeVform getCscpCustomizeVformsByFormName(@RequestParam("formName") String formName) {
        log.debug("REST request to get getCscpCustomizeVformByFormName");
        return cscpCustomizeVformService.getCscpCustomizeVformsByFormName(formName);
    }

    @ApiOperation(value = "根据类型获取流程表")
    @GetMapping("/cscpCustomizeVforms/getByFormType")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getByFormType")
    @Log
    public List<CscpCustomizeVform> getByFormType(String formType) {
        log.debug("REST request to get getByFormType");
        return cscpCustomizeVformService.selectByFormType(formType);
    }

}
