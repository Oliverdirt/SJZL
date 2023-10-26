package com.ctsi.ssdc.customize.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.customize.domain.CscpCustomizeVpage;
import com.ctsi.ssdc.customize.domain.CscpCustomizeVpageExample;
import com.ctsi.ssdc.customize.domain.CscpHxDformColumn;
import com.ctsi.ssdc.customize.service.CscpCustomizeVpageService;
import com.ctsi.ssdc.security.SecurityHxUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import java.lang.Long;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;

/**
 * REST controller for managing CscpCustomizeVpage.
 *
 * @author hx
 * @date 2022-09-01 14:40:59
 *
 */

@Api(value = "/api",tags = {"cscp-customize-vpage-controller"})
@RestController
@RequestMapping("/api/lowcode/customize")
public class CscpCustomizeVpageController {


    private final Logger log = LoggerFactory.getLogger(CscpCustomizeVpageController.class);

    private static final String ENTITY_NAME = "cscpCustomizeVpage";

    private final CscpCustomizeVpageService cscpCustomizeVpageService;

    public CscpCustomizeVpageController(CscpCustomizeVpageService cscpCustomizeVpageService) {
        this.cscpCustomizeVpageService = cscpCustomizeVpageService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpCustomizeVpages : Create a new cscpCustomizeVpage.
     *
     * @param cscpCustomizeVpage the cscpCustomizeVpage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpCustomizeVpage, or with status 400 (Bad Request) if the cscpCustomizeVpage has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpCustomizeVpage",name = "cscpCustomizeVpage",value = "the cscpCustomizeVpage to create")
    })
    @ApiOperation(value = "POST  /cscpCustomizeVpages : create a new cscpCustomizeVpage.",notes = "POST  /cscpCustomizeVpages : create a new cscpCustomizeVpage.",httpMethod = "POST")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "createCscpCustomizeVpage")
    @PostMapping("/cscpCustomizeVpages")
    public ResponseEntity<CscpCustomizeVpage> createCscpCustomizeVpage(@RequestBody  CscpCustomizeVpage cscpCustomizeVpage) throws URISyntaxException {

        log.debug("REST request to save CscpCustomizeVpage : {}", cscpCustomizeVpage);
        cscpCustomizeVpage.setCreatedBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeVpage.setCreatedTime(ZonedDateTime.now());
        cscpCustomizeVpage.setUpdateTime(ZonedDateTime.now());
        CscpCustomizeVpage result = cscpCustomizeVpageService.insert(cscpCustomizeVpage);
        return ResponseEntity.created(new URI("/api/cscpCustomizeVpages/" + result.getPageId() ))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getPageId().toString()))
                .body(result);
    }
    /**
     * PUT  /cscpCustomizeVpages : Updates an existing cscpCustomizeVpage.
     *
     * @param cscpCustomizeVpage the cscpCustomizeVpage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpCustomizeVpage,
     * or with status 400 (Bad Request) if the cscpCustomizeVpage is not valid,
     * or with status 500 (Internal Server Error) if the cscpCustomizeVpage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpCustomizeVpage",name = "cscpCustomizeVpage",value = "the cscpCustomizeVpage to update")
    })
    @ApiOperation(value = "PUT  /cscpCustomizeVpages : updates an existing cscpCustomizeVpage.",notes = "PUT  /cscpCustomizeVpages : updates an existing cscpCustomizeVpage.",httpMethod = "PUT")
    @PutMapping("/cscpCustomizeVpages")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "updateCscpCustomizeVpage")
    public ResponseEntity<CscpCustomizeVpage> updateCscpCustomizeVpage(@RequestBody  CscpCustomizeVpage cscpCustomizeVpage) throws URISyntaxException {

        log.debug("REST request to update CscpCustomizeVpage : {}", cscpCustomizeVpage);

        if (cscpCustomizeVpage.getPageId() == null) {
            return createCscpCustomizeVpage(cscpCustomizeVpage);
        }
        cscpCustomizeVpage.setUpdateBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeVpage.setUpdateTime(ZonedDateTime.now());
        CscpCustomizeVpage result = cscpCustomizeVpageService.update(cscpCustomizeVpage);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getPageId().toString()))
                .body(result);
    }

    /**
     * GET  /cscpCustomizeVpages/:pageId : get the "pageId" cscpCustomizeVpage.
     *
     * @param pageId the id of the cscpCustomizeVpage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpCustomizeVpage, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "pageId",value = "the pageId of the cscpCustomizeVpage to retrieve")
    })
    @ApiOperation(value = "GET  /cscpCustomizeVpages/pageId : get the pageId cscpCustomizeVpage.",notes = "GET  /cscpCustomizeVpages/pageId : get the pageId cscpCustomizeVpage.",httpMethod = "GET")
    @GetMapping("/cscpCustomizeVpages/{pageId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "getCscpCustomizeVpage")
    public ResponseEntity<CscpCustomizeVpage> getCscpCustomizeVpage(@PathVariable Long pageId) {

        log.debug("REST request to get CscpCustomizeVpage : {}", pageId);

        CscpCustomizeVpage cscpCustomizeVpage = cscpCustomizeVpageService.findOne(pageId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeVpage));
    }

    /**
     * GET  /cscpCustomizeVpages : get the cscpCustomizeVpages .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpCustomizeVpages in body
     */
    @ApiOperation(value = "GET  /cscpCustomizeVpages ")
    @GetMapping("/cscpCustomizeVpages")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "getCscpCustomizeVpagesList")
    public PageResult<CscpCustomizeVpage> getCscpCustomizeVpagesList(CscpCustomizeVpageExample cscpCustomizeVpageExample, Pageable pageable) {

        log.debug("REST request to get CscpCustomizeVpagesList");

        return cscpCustomizeVpageService.findByExample(cscpCustomizeVpageExample,pageable);
    }

    /**
     * DELETE  /cscpCustomizeVpages/:pageId : delete the "pageId" cscpCustomizeVpage.
     *
     * @param pageId the id of the cscpCustomizeVpage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "pageId",value = "the pageId of the cscpCustomizeVpage to delete")
    })
    @ApiOperation(value = "DELETE  /cscpCustomizeVpages/pageId : delete the pageId cscpCustomizeVpage.",notes = "DELETE  /cscpCustomizeVpages/pageId : delete the pageId cscpCustomizeVpage.",httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeVpages/{pageId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "deleteCscpCustomizeVpage")
    public ResponseEntity<Void> deleteCscpCustomizeVpage(@PathVariable Long pageId) {
        log.debug("REST request to delete CscpCustomizeVpage : {}", pageId);
        cscpCustomizeVpageService.deleteById(pageId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, pageId.toString())).build();
    }


    /**
     * GET  /cscpCustomizeVpages/:pageId : get the "pageId" cscpCustomizeVpage.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cscpCustomizeVpage, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /cscpCustomizeVpages/export : export the cscpCustomizeVpage to excel",notes = "DELETE  /cscpCustomizeVpages/pageId : delete the pageId cscpCustomizeVpage.",httpMethod = "DELETE")
    @PostMapping("/cscpCustomizeVpages/export")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "export")
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export CscpCustomizeVpage");

        PageResult<CscpCustomizeVpage> result = cscpCustomizeVpageService.findAll();
        List<CscpCustomizeVpage> list = result.getData();
        ExcelUtil<CscpCustomizeVpage> util = new ExcelUtil<CscpCustomizeVpage>(CscpCustomizeVpage.class);
        return util.exportExcel(list, "cscpCustomizeVpage");
    }


    /**
     * DELETE  /pageIds : delete the cscpCustomizeVpage.", notes = "DELETE  /pageIds : delete the pageIds.", httpMethod = "DELETE"
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "pageIds", value = "the pageIds of the cscpCustomizeVpage to delete")
    })
    @ApiOperation(value = "DELETE  /pageIds : delete the cscpCustomizeVpage.", notes = "DELETE  /pageIds : delete the pageIds.", httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeVpages/delAll")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "deleteCscpCustomizeVpage")
    public ResponseEntity<Void> deleteCscpCustomizeVpage(Long[] pageIds) {

        log.debug("REST request to delete pageIds");

        cscpCustomizeVpageService.deleteByIds(pageIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(pageIds))).build();
    }


    /**
     * 根据主表pageId查询子表form表信息
     * @param pageId 主页pageId
     * @return  主页form表信息
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "formId",value = "the formId of the cscpCustomizeVform to retrieve")
    })
    @ApiOperation(value = "GET  /childCscpCustomizeVpages/pageId : get the formId chicscpCustomizeVform.",notes = "GET  /cscpCustomizeVforms/formId : get the formId cscpCustomizeVform.",httpMethod = "GET")
    @GetMapping("/childCscpCustomizeVpage/{pageId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getChildCscpCustomizeVpages")
    @Log
    public ResponseEntity<List<CscpCustomizeVpage>> getChildCscpCustomizeVpages(@PathVariable Long[] pageId) {
        log.debug("REST request to get childCscpCustomizeVpates : {}", pageId);
        List<CscpCustomizeVpage> cscpCustomizeVpage = cscpCustomizeVpageService.queryChildCscpCustomizeVpages(pageId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeVpage));
    }

    /**
     * GET  /cscpCustomizeVpages : get the selectRelationColumnByPageId .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpCustomizeVpages in body
     */
    @ApiOperation(value = "GET  /selectRelationColumnByPageId ")
    @GetMapping("/selectRelationColumnByPageId")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "selectRelationColumnByPageId")
    public List<CscpHxDformColumn> selectRelationColumnByPageId(Long pageId) {
        log.debug("REST request to get selectRelationColumnByPageId");
        return cscpCustomizeVpageService.selectRelationColumnByPageId(pageId);
    }
}
