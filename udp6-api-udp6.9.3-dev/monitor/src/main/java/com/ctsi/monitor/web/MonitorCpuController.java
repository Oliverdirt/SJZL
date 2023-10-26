package com.ctsi.monitor.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
    import com.ctsi.monitor.service.MonitorCpuService;
import com.ctsi.monitor.domain.MonitorCpuExample;
import com.ctsi.monitor.domain.MonitorCpu;

import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;

/**
 * REST controller for managing MonitorCpu.
 *
 * @author hx
 * @date 2022-05-27 11:21:43
 *
 */

@Api(value = "/api",tags = {"monitor-cpu-controller"})
@RestController
@RequestMapping("/api")
public class MonitorCpuController {


    private final Logger log = LoggerFactory.getLogger(MonitorCpuController.class);

    private static final String ENTITY_NAME = "monitorCpu";

    private final MonitorCpuService monitorCpuService;

    public MonitorCpuController(MonitorCpuService monitorCpuService) {
        this.monitorCpuService = monitorCpuService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /monitorCpus : Create a new monitorCpu.
     *
     * @param monitorCpu the monitorCpu to create
     * @return the ResponseEntity with status 201 (Created) and with body the new monitorCpu, or with status 400 (Bad Request) if the monitorCpu has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "MonitorCpu",name = "monitorCpu",value = "the monitorCpu to create")
    })
    @ApiOperation(value = "POST  /monitorCpus : create a new monitorCpu.",notes = "POST  /monitorCpus : create a new monitorCpu.",httpMethod = "POST")
    @PostMapping("/monitorCpus")
    public ResponseEntity<MonitorCpu> createMonitorCpu(@RequestBody  MonitorCpu monitorCpu) throws URISyntaxException {

        log.debug("REST request to save MonitorCpu : {}", monitorCpu);

        MonitorCpu result = monitorCpuService.insert(monitorCpu);
        return ResponseEntity.created(new URI("/api/monitorCpus/" + result.getId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /monitorCpus : Updates an existing monitorCpu.
     *
     * @param monitorCpu the monitorCpu to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated monitorCpu,
     * or with status 400 (Bad Request) if the monitorCpu is not valid,
     * or with status 500 (Internal Server Error) if the monitorCpu couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "MonitorCpu",name = "monitorCpu",value = "the monitorCpu to update")
    })
    @ApiOperation(value = "PUT  /monitorCpus : updates an existing monitorCpu.",notes = "PUT  /monitorCpus : updates an existing monitorCpu.",httpMethod = "PUT")
    @PutMapping("/monitorCpus")
    public ResponseEntity<MonitorCpu> updateMonitorCpu(@RequestBody  MonitorCpu monitorCpu) throws URISyntaxException {

        log.debug("REST request to update MonitorCpu : {}", monitorCpu);

        if (monitorCpu.getId() == null) {
            return createMonitorCpu(monitorCpu);
        }
        MonitorCpu result = monitorCpuService.update(monitorCpu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /monitorCpus/:id : get the "id" monitorCpu.
     *
     * @param id the id of the monitorCpu to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the monitorCpu, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id",value = "the id of the monitorCpu to retrieve")
    })
    @ApiOperation(value = "GET  /monitorCpus/id : get the id monitorCpu.",notes = "GET  /monitorCpus/id : get the id monitorCpu.",httpMethod = "GET")
    @GetMapping("/monitorCpus/{id}")
    public ResponseEntity<MonitorCpu> getMonitorCpu(@PathVariable Long id) {

        log.debug("REST request to get MonitorCpu : {}", id);

        MonitorCpu monitorCpu = monitorCpuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(monitorCpu));
    }

    /**
     * GET  /monitorCpus : get the monitorCpus .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of monitorCpus in body
     */
    @ApiOperation(value = "GET  /monitorCpus ")
    @GetMapping("/monitorCpus")
    public PageResult<MonitorCpu> getMonitorCpusList(MonitorCpuExample monitorCpuExample,Pageable pageable) {

        log.debug("REST request to get MonitorCpusList");

        return monitorCpuService.findByExample(monitorCpuExample,pageable);
    }

    /**
     * DELETE  /monitorCpus/:id : delete the "id" monitorCpu.
     *
     * @param id the id of the monitorCpu to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id",value = "the id of the monitorCpu to delete")
    })
    @ApiOperation(value = "DELETE  /monitorCpus/id : delete the id monitorCpu.",notes = "DELETE  /monitorCpus/id : delete the id monitorCpu.",httpMethod = "DELETE")
    @DeleteMapping("/monitorCpus/{id}")
    public ResponseEntity<Void> deleteMonitorCpu(@PathVariable Long id) {
        log.debug("REST request to delete MonitorCpu : {}", id);
        monitorCpuService.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * GET  /monitorCpus/:id : get the "id" monitorCpu.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the monitorCpu, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /monitorCpus/export : export the monitorCpu to excel",notes = "DELETE  /monitorCpus/id : delete the id monitorCpu.",httpMethod = "DELETE")
    @PostMapping("/monitorCpus/export")
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export MonitorCpu");

        PageResult<MonitorCpu> result = monitorCpuService.findAll();
        List<MonitorCpu> list = result.getData();
        ExcelUtil<MonitorCpu> util = new ExcelUtil<MonitorCpu>(MonitorCpu.class);
        return util.exportExcel(list, "monitorCpu");
    }


    /**
    * DELETE  /ids : delete the monitorCpu.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE"
    *
    * @return
    */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "ids", value = "the ids of the monitorCpu to delete")
    })
    @ApiOperation(value = "DELETE  /ids : delete the monitorCpu.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE")
    @DeleteMapping("/monitorCpus/delAll")
    public ResponseEntity<Void> deleteMonitorCpu(Long[] ids) {

        log.debug("REST request to delete ids");

        monitorCpuService.deleteByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(ids))).build();
    }

}
