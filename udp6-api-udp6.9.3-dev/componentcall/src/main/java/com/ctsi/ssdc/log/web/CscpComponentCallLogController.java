package com.ctsi.ssdc.log.web;

import com.ctsi.ssdc.log.domain.CscpComponentCallLog;
import com.ctsi.ssdc.log.domain.CscpComponentCallLogExample;
import com.ctsi.ssdc.log.service.CscpComponentCallLogService;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CscpComponentCallLog.
 *
 * @author hx
 * @date 2022-03-15 21:35:20
 *
 */

@Api(value = "/api",tags = {"cscp-component-call-log-controller"})
@RestController
@RequestMapping("/api")
public class CscpComponentCallLogController {


    private final Logger log = LoggerFactory.getLogger(CscpComponentCallLogController.class);

    private static final String ENTITY_NAME = "cscpComponentCallLog";

    private final CscpComponentCallLogService cscpComponentCallLogService;

    public CscpComponentCallLogController(CscpComponentCallLogService cscpComponentCallLogService) {
        this.cscpComponentCallLogService = cscpComponentCallLogService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpComponentCallLogs : Create a new cscpComponentCallLog.
     *
     * @param cscpComponentCallLog the cscpComponentCallLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpComponentCallLog, or with status 400 (Bad Request) if the cscpComponentCallLog has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpComponentCallLog",name = "cscpComponentCallLog",value = "the cscpComponentCallLog to create")
    })
    @ApiOperation(value = "POST  /cscpComponentCallLogs : create a new cscpComponentCallLog.",notes = "POST  /cscpComponentCallLogs : create a new cscpComponentCallLog.",httpMethod = "POST")
    @PostMapping("/cscpComponentCallLogs")
    public ResponseEntity<CscpComponentCallLog> createCscpComponentCallLog(@RequestBody  CscpComponentCallLog cscpComponentCallLog) throws URISyntaxException {

        log.debug("REST request to save CscpComponentCallLog : {}", cscpComponentCallLog);

        CscpComponentCallLog result = cscpComponentCallLogService.insert(cscpComponentCallLog);
        return ResponseEntity.created(new URI("/api/cscpComponentCallLogs/" + result.getId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /cscpComponentCallLogs : Updates an existing cscpComponentCallLog.
     *
     * @param cscpComponentCallLog the cscpComponentCallLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpComponentCallLog,
     * or with status 400 (Bad Request) if the cscpComponentCallLog is not valid,
     * or with status 500 (Internal Server Error) if the cscpComponentCallLog couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpComponentCallLog",name = "cscpComponentCallLog",value = "the cscpComponentCallLog to update")
    })
    @ApiOperation(value = "PUT  /cscpComponentCallLogs : updates an existing cscpComponentCallLog.",notes = "PUT  /cscpComponentCallLogs : updates an existing cscpComponentCallLog.",httpMethod = "PUT")
    @PutMapping("/cscpComponentCallLogs")
    public ResponseEntity<CscpComponentCallLog> updateCscpComponentCallLog(@RequestBody  CscpComponentCallLog cscpComponentCallLog) throws URISyntaxException {

        log.debug("REST request to update CscpComponentCallLog : {}", cscpComponentCallLog);

        if (cscpComponentCallLog.getId() == null) {
            return createCscpComponentCallLog(cscpComponentCallLog);
        }
        CscpComponentCallLog result = cscpComponentCallLogService.update(cscpComponentCallLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpComponentCallLogs/:id : get the "id" cscpComponentCallLog.
     *
     * @param id the id of the cscpComponentCallLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpComponentCallLog, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id",value = "the id of the cscpComponentCallLog to retrieve")
    })
    @ApiOperation(value = "GET  /cscpComponentCallLogs/id : get the id cscpComponentCallLog.",notes = "GET  /cscpComponentCallLogs/id : get the id cscpComponentCallLog.",httpMethod = "GET")
    @GetMapping("/cscpComponentCallLogs/{id}")
    public ResponseEntity<CscpComponentCallLog> getCscpComponentCallLog(@PathVariable Long id) {

        log.debug("REST request to get CscpComponentCallLog : {}", id);

        CscpComponentCallLog cscpComponentCallLog = cscpComponentCallLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpComponentCallLog));
    }

    /**
     * GET  /cscpComponentCallLogs : get the cscpComponentCallLogs firstStringBaseColumn.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpComponentCallLogs in body
     */
    @ApiOperation(value = "GET  /cscpComponentCallLogs ")
    @GetMapping("/cscpComponentCallLogs")
    public PageResult<CscpComponentCallLog> getCscpComponentCallLogsList(CscpComponentCallLogExample cscpComponentCallLogExample,Pageable pageable) {

        log.debug("REST request to get CscpComponentCallLogsList");

        return cscpComponentCallLogService.findByExample(cscpComponentCallLogExample,pageable);
    }


    /**
     * DELETE  /cscpComponentCallLogs/:id : delete the "id" cscpComponentCallLog.
     *
     * @param id the id of the cscpComponentCallLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id",value = "the id of the cscpComponentCallLog to delete")
    })
    @ApiOperation(value = "DELETE  /cscpComponentCallLogs/id : delete the id cscpComponentCallLog.",notes = "DELETE  /cscpComponentCallLogs/id : delete the id cscpComponentCallLog.",httpMethod = "DELETE")
    @DeleteMapping("/cscpComponentCallLogs/{id}")
    public ResponseEntity<Void> deleteCscpComponentCallLog(@PathVariable Long id) {

        log.debug("REST request to delete CscpComponentCallLog : {}", id);

        cscpComponentCallLogService.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * GET  /cscpComponentCallLogs/:id : get the "id" cscpComponentCallLog.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cscpComponentCallLog, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /cscpComponentCallLogs/export : export the cscpComponentCallLog to excel",notes = "DELETE  /cscpComponentCallLogs/id : delete the id cscpComponentCallLog.",httpMethod = "DELETE")
    @PostMapping("/cscpComponentCallLogs/export")
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export CscpComponentCallLog");

        PageResult<CscpComponentCallLog> result = cscpComponentCallLogService.findAll();
        List<CscpComponentCallLog> list = result.getData();
        ExcelUtil<CscpComponentCallLog> util = new ExcelUtil<CscpComponentCallLog>(CscpComponentCallLog.class);
        return util.exportExcel(list, "cscpComponentCallLog");
    }


    /**
    * DELETE  /ids : delete the cscpComponentCallLog.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE"
    *
    * @return
    */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "ids", value = "the ids of the cscpComponentCallLog to delete")
    })
    @ApiOperation(value = "DELETE  /ids : delete the cscpComponentCallLog.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE")
    @DeleteMapping("/cscpComponentCallLogs/delAll")
    public ResponseEntity<Void> deleteCscpComponentCallLog(Long[] ids) {

        log.debug("REST request to delete ids");

        cscpComponentCallLogService.deleteByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(ids))).build();
    }

}
