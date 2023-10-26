package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.admin.domain.CscpNotice;
import com.ctsi.ssdc.admin.domain.CscpNoticeExample;
import com.ctsi.ssdc.admin.service.CscpNoticeService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import com.ctsi.ssdc.utils.HxStringUtils;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
 * REST controller for managing CscpNotice.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(value = "/api",tags = {"cscp-notice-controller"})
@RestController
@RequestMapping("/api")
public class CscpNoticeController {

    private final Logger log = LoggerFactory.getLogger(CscpNoticeController.class);

    private static final String ENTITY_NAME = "cscpNotice";

    private final CscpNoticeService cscpNoticeService;

    public CscpNoticeController(CscpNoticeService cscpNoticeService) {
        this.cscpNoticeService = cscpNoticeService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
   
    /**
     * POST  /cscpNotices : Create a new cscpNotice.
     *
     * @param cscpNotice the cscpNotice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpNotice, or with status 400 (Bad Request) if the cscpNotice has already an noticeId
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpNotice",name = "cscpNotice",value = "the cscpNotice to create")
    })
    @ApiOperation(value = "POST  /cscpNotices : create a new cscpNotice.",notes = "POST  /cscpNotices : create a new cscpNotice.",httpMethod = "POST")
    @PostMapping("/cscpNotices")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "createCscpMenus")
    public ResponseEntity<CscpNotice> createCscpNotice(@RequestBody CscpNotice cscpNotice) throws URISyntaxException {
        log.debug("REST request to save CscpNotice : {}", cscpNotice);
        cscpNotice.setCreateBy(SecurityHxUtils.getCurrentUserName());
        cscpNotice.setTenantId(SecurityHxUtils.getCurrentTenantId());
        cscpNotice.setCreateTime(ZonedDateTime.now());
        if (cscpNotice.getNoticeId() != null) {
            throw new BadRequestAlertException("A new cscpNotice cannot already have an noticeId", ENTITY_NAME, "idexists");
        }
        CscpNotice result = cscpNoticeService.insert(cscpNotice);
        return ResponseEntity.created(new URI("/api/cscpNotices" + "/" +result.getNoticeId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getNoticeId().toString()))
            .body(result);
    }
	
    /**
     * PUT  /cscpNotices : Updates an existing cscpNotice.
     *
     * @param cscpNotice the cscpNotice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpNotice,
     * or with status 400 (Bad Request) if the cscpNotice is not valid,
     * or with status 500 (Internal Server Error) if the cscpNotice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpNotice",name = "cscpNotice",value = "the cscpNotice to update")
    })
    @ApiOperation(value = "PUT  /cscpNotices : updates an existing cscpNotice.",notes = "PUT  /cscpNotices : updates an existing cscpNotice.",httpMethod = "PUT")
    @PutMapping("/cscpNotices")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "updateCscpNotice")
    public ResponseEntity<CscpNotice> updateCscpNotice(@RequestBody CscpNotice cscpNotice) throws URISyntaxException {
        log.debug("REST request to update CscpNotice : {}", cscpNotice);
	    if (cscpNotice.getNoticeId() == null) {
	    	return createCscpNotice(cscpNotice);
	    }
        cscpNotice.setUpdateBy(SecurityHxUtils.getCurrentUserName());
        cscpNotice.setUpdateTime(ZonedDateTime.now());
        CscpNotice result = cscpNoticeService.update(cscpNotice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getNoticeId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpNotices/:noticeId : get the "noticeId" cscpNotice.
     *
     * @param noticeId the id of the cscpNotice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpNotice, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "noticeId",value = "the noticeId of the cscpNotice to retrieve")
    })
    @ApiOperation(value = "GET  /cscpNotices/noticeId : get the noticeId cscpNotice.",notes = "GET  /cscpNotices/noticeId : get the noticeId cscpNotice.",httpMethod = "GET")
    @GetMapping("/cscpNotices/{noticeId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpNotice")
    public ResponseEntity<CscpNotice> getCscpNotice(@PathVariable Long noticeId) {
        log.debug("REST request to get CscpNotice : {}", noticeId);
        CscpNotice cscpNotice = cscpNoticeService.findOne(noticeId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpNotice));
    }

    /**
    * GET  /cscpNotices : get the cscpNotices firstStringBaseColumn.
    *
    * @return the ResponseEntity with status 200 (OK) and the list of cscpNotices in body
    */
    @ApiOperation(value = "GET  /cscpNotices : get the cscpNotices firstStringBaseColumn.",notes = "GET  /cscpNotices : get the cscpNotices firstStringBaseColumn.",httpMethod = "GET")
    @GetMapping("/cscpNotices")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpNoticesList")
    public PageResult<CscpNotice> getCscpNoticesList(CscpNoticeExample cscpNoticeExample, Pageable pageable) {
        log.debug("REST request to get CscpNoticesList");
        return cscpNoticeService.findByExample(cscpNoticeExample,pageable);
    }

    @GetMapping("/cscpNotice/getList")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "getCscpNoticeList")
    public PageResult<CscpNotice> getCscpNoticeList(CscpNotice cscpNotice, Pageable pageable) {
        log.debug("REST request to get JobsList");
        return cscpNoticeService.getCscpNotices(cscpNotice,pageable);
    }
	
    /**
     * DELETE  /cscpNotices/:noticeId : delete the "noticeId" cscpNotice.
     *
     * @param noticeId the id of the cscpNotice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "noticeId",value = "the noticeId of the cscpNotice to delete")
    })
    @ApiOperation(value = "DELETE  /cscpNotices/noticeId : delete the noticeId cscpNotice.",notes = "DELETE  /cscpNotices/noticeId : delete the noticeId cscpNotice.",httpMethod = "DELETE")
    @DeleteMapping("/cscpNotices/{noticeId}")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteCscpNotice")
    public ResponseEntity<Void> deleteCscpNotice(@PathVariable Long noticeId) {
        log.debug("REST request to delete CscpNotice : {}", noticeId);
        cscpNoticeService.delete(noticeId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, noticeId.toString())).build();
    }

    /**
    * GET  /cscpNotices/:noticeId : get the "noticeId" cscpNotice.
    *
    * @return the ResponseEntity with status 200 (OK) and with body the cscpNotice, or with status 404 (Not Found)
    */
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "noticeId",value = "the noticeId of the cscpNotice to retrieve")
    })
    @ApiOperation(value = "POST  /cscpNotices/export : export the cscpNotice to excel",notes = "POST  /cscpNotices/export : export the cscpNotice to excel",httpMethod = "POST")
    @PostMapping("/cscpNotices/export")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "export")
    public ResponseEntity<byte[]> export() {
        log.debug("REST request to export CscpNotice");
        PageResult<CscpNotice> result = cscpNoticeService.findAll();
        List<CscpNotice> list = result.getData();
        ExcelUtil<CscpNotice> util = new ExcelUtil<CscpNotice>(CscpNotice.class);
        return util.exportExcel(list, "cscpNotice");
    }

    /**
     * 删除通知公告
     */
    @DeleteMapping("/cscpNotices/batchDel")
    @ApiOperation("删除通知公告")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "deleteNoticeByIds")
    public ResponseEntity<Void> deleteNoticeByIds(Long[] ids)
    {
        log.debug("REST request to delete Job : {}", ids);
        cscpNoticeService.deleteNoticeByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, HxStringUtils.join(ids))).build();
    }

}
