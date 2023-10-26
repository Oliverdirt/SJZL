package com.ctsi.ssdc.quartz.web;


import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.model.AjaxBackData;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.quartz.domain.CscpJob;
import com.ctsi.ssdc.quartz.service.CscpJobService;
import com.ctsi.ssdc.quartz.util.CronUtils;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
 * REST controller for managing Job.
 *
 * @author ctsi-biyi-generator
 */
@Api(value = "/api", tags = {"job-controller"})
@RestController
@RequestMapping("/api")
public class CscpJobController {

    private final Logger log = LoggerFactory.getLogger(CscpJobController.class);

    private static final String ENTITY_NAME = "job";

    private final CscpJobService jobService;

    public CscpJobController(CscpJobService jobService) {
        this.jobService = jobService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /jobs : Create a new job.
     *
     * @param job the job to create
     * @return the ResponseEntity with status 201 (Created) and with body the new job, or with status 400 (Bad Request) if the job has already an id
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CscpJob", name = "job", value = "the job to create")
    })
    @ApiOperation(value = "POST  /jobs : create a new CscpJob.", notes = "POST  /jobs : create a new CscpJob.", httpMethod = "POST")
    @PostMapping("/jobs")
    @ComponentCallAnno(component = HxComponentConstant.QUARTZ,method = "createJob")
    @Log
    public ResponseEntity<CscpJob> createJob(@RequestBody CscpJob job) throws URISyntaxException {
        log.debug("REST request to save Job : {}", job);
        if (!CronUtils.isValid(job.getCronExpression())) {
            throw new BadRequestAlertException("Cron表达式不正确", ENTITY_NAME, "badCronExpression");
        }
        CscpJob result = jobService.insert(job);
        return ResponseEntity.created(new URI("/api/jobs" + "/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /jobs : Updates an existing job.
     *
     * @param job the job to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated job,
     * or with status 400 (Bad Request) if the job is not valid,
     * or with status 500 (Internal Server Error) if the job couldn't be updated
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CscpJob", name = "job", value = "the job to update")
    })
    @ApiOperation(value = "PUT  /jobs : updates an existing job.", notes = "PUT  /jobs : updates an existing job.", httpMethod = "PUT")
    @PutMapping("/jobs")
    @ComponentCallAnno(component = HxComponentConstant.QUARTZ,method = "updateJob")
    @Log
    public ResponseEntity<CscpJob> updateJob(@RequestBody CscpJob job) {
        log.debug("REST request to update Job : {}", job);
        if (!CronUtils.isValid(job.getCronExpression())) {
            throw new BadRequestAlertException("Cron表达式不正确", ENTITY_NAME, "badCronExpression");
        }
        CscpJob result = jobService.update(job);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * GET  /jobs/:id : get the "id" job.
     *
     * @param id the id of the job to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the job, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "BIGINT", name = "id", value = "the id of the job to retrieve")
    })
    @ApiOperation(value = "GET  /jobs/id : get the id job.", notes = "GET  /jobs/id : get the id job.", httpMethod = "GET")
    @GetMapping("/jobs/{id}")
    @ComponentCallAnno(component = HxComponentConstant.QUARTZ,method = "getJob")
    @Log
    public ResponseEntity<CscpJob> getJob(@PathVariable Long id) {
        log.debug("REST request to get Job : {}", id);
        CscpJob job = jobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(job));
    }

    /**
     * GET  /jobs : get the jobs firstStringBaseColumn.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
     */
    @ApiOperation(value = "GET  /jobs : get the jobs firstStringBaseColumn.", notes = "GET  /jobs : get the jobs firstStringBaseColumn.", httpMethod = "GET")
    @GetMapping("/jobs")
    @ComponentCallAnno(component = HxComponentConstant.QUARTZ,method = "getJobsList")
    @Log
    public PageResult<CscpJob> getJobsList(CscpJob job, Pageable pageable) {
        log.debug("REST request to get JobsList");
        return jobService.findFirstStringColumn(job,pageable);
    }

    /**
     * DELETE  /jobs/:id : delete the "id" job.
     *
     * @param id the id of the job to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "BIGINT", name = "id", value = "the id of the job to delete")
    })
    @ApiOperation(value = "DELETE  /jobs/id : delete the id job.", notes = "DELETE  /jobs/id : delete the id job.", httpMethod = "DELETE")
    @DeleteMapping("/jobs/{id}")
    @ComponentCallAnno(component = HxComponentConstant.QUARTZ,method = "deleteJob")
    @Log
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        log.debug("REST request to delete Job : {}", id);
        jobService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /jobs/:id : get the "id" job.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the job, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "id",value = "the id of the job to retrieve")
    })
    @ApiOperation(value = "POST  /jobs/export : export the job to excel",notes = "POST  /jobs/export : export the job to excel",httpMethod = "POST")
    @PostMapping("/jobs/export")
    @ComponentCallAnno(component = HxComponentConstant.QUARTZ,method = "exportJobs")
    @Log
    public ResponseEntity<byte[]> export() {
        log.debug("REST request to export Job");
        PageResult<CscpJob> result = jobService.findAll();
        List<CscpJob> list = result.getData();
        ExcelUtil<CscpJob> util = new ExcelUtil<>(CscpJob.class);
        return util.exportExcel(list, "job");
    }


    /**
     * 定时任务状态修改
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CscpJob", name = "job", value = "the job to update")
    })
    @ApiOperation(value = "PUT  /jobs/changeStatus : updates an existing job.", notes = "PUT  /jobs : updates an existing job.", httpMethod = "PUT")
    @PutMapping("/jobs/changeStatus")
    @ComponentCallAnno(component = HxComponentConstant.QUARTZ,method = "changeStatus")
    @Log
    public ResponseEntity<Long> changeStatus(@RequestBody CscpJob job){
        CscpJob newJob = jobService.findOne(job.getId());
        newJob.setStatus(job.getStatus());
        CscpJob result = jobService.changeStatus(newJob);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result.getId());
    }

    /**
     * 定时任务状态修改
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CscpJob", name = "job", value = "the job to update")
    })
    @ApiOperation(value = "PUT  /jobs/changeStatus : updates an existing job.", notes = "PUT  /jobs : updates an existing job.", httpMethod = "PUT")
    @PutMapping("/jobs/run")
    @ComponentCallAnno(component = HxComponentConstant.QUARTZ,method = "run")
    @Log
    public AjaxBackData run(@RequestBody CscpJob job) {
        AjaxBackData ajaxBackData = new AjaxBackData();
        jobService.run(job);
        ajaxBackData.setCode(HttpStatus.OK.value());
        return ajaxBackData;
    }

    /**
     * 批量删除定时任务
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "BIGINT", name = "jobIds", value = "the id of the job to delete")
    })
    @ApiOperation(value = "DELETE  /jobs : delete the id job.", notes = "DELETE  /jobs : delete the ids job.", httpMethod = "DELETE")
    @DeleteMapping("/jobs")
    @ComponentCallAnno(component = HxComponentConstant.QUARTZ,method = "deleteJob")
    @Log
    public ResponseEntity<Void> deleteJob(Long[] jobIds) {
        jobService.deleteByIds(jobIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(jobIds))).build();
    }
}
