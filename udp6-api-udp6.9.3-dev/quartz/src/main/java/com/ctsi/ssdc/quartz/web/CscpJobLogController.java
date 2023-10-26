package com.ctsi.ssdc.quartz.web;

import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import com.ctsi.ssdc.quartz.domain.CscpJobLog;
import com.ctsi.ssdc.quartz.domain.CscpJobLogExample;
import com.ctsi.ssdc.quartz.service.CscpJobLogService;
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
 * REST controller for managing CscpJobLog.
 *
 * @author hx
 * @date 2022-05-30 17:05:20
 *
 */

@Api(value = "/api",tags = {"sys-job-log-controller"})
@RestController
@RequestMapping("/api")
public class CscpJobLogController {


    private final Logger log = LoggerFactory.getLogger(CscpJobLogController.class);

    private static final String ENTITY_NAME = "sysJobLog";

    private final CscpJobLogService cscpJobLogService;

    public CscpJobLogController(CscpJobLogService cscpJobLogService) {
        this.cscpJobLogService = cscpJobLogService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /sysJobLogs : Create a new sysJobLog.
     *
     * @param sysJobLog the sysJobLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sysJobLog, or with status 400 (Bad Request) if the sysJobLog has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpJobLog",name = "sysJobLog",value = "the sysJobLog to create")
    })
    @ApiOperation(value = "POST  /sysJobLogs : create a new sysJobLog.",notes = "POST  /sysJobLogs : create a new sysJobLog.",httpMethod = "POST")
    @PostMapping("/sysJobLogs")
    @Log
    public ResponseEntity<CscpJobLog> createSysJobLog(@RequestBody  CscpJobLog sysJobLog) throws URISyntaxException {

        log.debug("REST request to save CscpJobLog : {}", sysJobLog);

        CscpJobLog result = cscpJobLogService.insert(sysJobLog);
        return ResponseEntity.created(new URI("/api/sysJobLogs/" + result.getJobLogId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getJobLogId().toString()))
            .body(result);
    }
    /**
     * PUT  /sysJobLogs : Updates an existing sysJobLog.
     *
     * @param cscpJobLog the sysJobLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sysJobLog,
     * or with status 400 (Bad Request) if the sysJobLog is not valid,
     * or with status 500 (Internal Server Error) if the sysJobLog couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpJobLog",name = "sysJobLog",value = "the sysJobLog to update")
    })
    @ApiOperation(value = "PUT  /sysJobLogs : updates an existing sysJobLog.",notes = "PUT  /sysJobLogs : updates an existing sysJobLog.",httpMethod = "PUT")
    @PutMapping("/sysJobLogs")
    @Log
    public ResponseEntity<CscpJobLog> updateSysJobLog(@RequestBody  CscpJobLog cscpJobLog) throws URISyntaxException {

        log.debug("REST request to update CscpJobLog : {}", cscpJobLog);

        if (cscpJobLog.getJobLogId() == null) {
            return createSysJobLog(cscpJobLog);
        }
        CscpJobLog result = cscpJobLogService.update(cscpJobLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getJobLogId().toString()))
            .body(result);
    }

    /**
     * GET  /sysJobLogs/:jobLogId : get the "jobLogId" sysJobLog.
     *
     * @param jobLogId the id of the sysJobLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sysJobLog, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "jobLogId",value = "the jobLogId of the sysJobLog to retrieve")
    })
    @ApiOperation(value = "GET  /sysJobLogs/jobLogId : get the jobLogId sysJobLog.",notes = "GET  /sysJobLogs/jobLogId : get the jobLogId sysJobLog.",httpMethod = "GET")
    @GetMapping("/sysJobLogs/{jobLogId}")
    @Log
    public ResponseEntity<CscpJobLog> getSysJobLog(@PathVariable Long jobLogId) {

        log.debug("REST request to get CscpJobLog : {}", jobLogId);

        CscpJobLog sysJobLog = cscpJobLogService.findOne(jobLogId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sysJobLog));
    }

    /**
     * GET  /sysJobLogs : get the sysJobLogs .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sysJobLogs in body
     */
    @ApiOperation(value = "GET  /sysJobLogs ")
    @GetMapping("/sysJobLogs")
    @Log
    public PageResult<CscpJobLog> getSysJobLogsList(CscpJobLogExample sysJobLogExample, Pageable pageable) {

        log.debug("REST request to get SysJobLogsList");

        return cscpJobLogService.findByExample(sysJobLogExample,pageable);
    }

    /**
     * DELETE  /sysJobLogs/:jobLogId : delete the "jobLogId" sysJobLog.
     *
     * @param jobLogId the id of the sysJobLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "jobLogId",value = "the jobLogId of the sysJobLog to delete")
    })
    @ApiOperation(value = "DELETE  /sysJobLogs/jobLogId : delete the jobLogId sysJobLog.",notes = "DELETE  /sysJobLogs/jobLogId : delete the jobLogId sysJobLog.",httpMethod = "DELETE")
    @DeleteMapping("/sysJobLogs/{jobLogId}")
    @Log
    public ResponseEntity<Void> deleteSysJobLog(@PathVariable Long jobLogId) {
        log.debug("REST request to delete CscpJobLog : {}", jobLogId);
        cscpJobLogService.deleteById(jobLogId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, jobLogId.toString())).build();
    }


    /**
     * GET  /sysJobLogs/:jobLogId : get the "jobLogId" sysJobLog.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the sysJobLog, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /sysJobLogs/export : export the sysJobLog to excel",notes = "DELETE  /sysJobLogs/jobLogId : delete the jobLogId sysJobLog.",httpMethod = "DELETE")
    @PostMapping("/sysJobLogs/export")
    @Log
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export CscpJobLog");

        PageResult<CscpJobLog> result = cscpJobLogService.findAll();
        List<CscpJobLog> list = result.getData();
        ExcelUtil<CscpJobLog> util = new ExcelUtil<CscpJobLog>(CscpJobLog.class);
        return util.exportExcel(list, "sysJobLog");
    }


    /**
    * DELETE  /jobLogIds : delete the sysJobLog.", notes = "DELETE  /jobLogIds : delete the jobLogIds.", httpMethod = "DELETE"
    *
    * @return
    */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "jobLogIds", value = "the jobLogIds of the sysJobLog to delete")
    })
    @ApiOperation(value = "DELETE  /jobLogIds : delete the sysJobLog.", notes = "DELETE  /jobLogIds : delete the jobLogIds.", httpMethod = "DELETE")
    @DeleteMapping("/sysJobLogs/delAll")
    @Log
    public ResponseEntity<Void> deleteSysJobLog(Long[] jobLogIds) {

        log.debug("REST request to delete jobLogIds");

        cscpJobLogService.deleteByIds(jobLogIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(jobLogIds))).build();
    }

}
