package com.ctsi.flow.approve.web;

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
    import com.ctsi.flow.approve.service.ApproveService;
import com.ctsi.flow.approve.domain.ApproveExample;
import com.ctsi.flow.approve.domain.Approve;

import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;

/**
 * REST controller for managing Approve.
 *
 * @author hx
 * @date 2022-11-03 19:50:16
 *
 */

@Api(value = "/api",tags = {"approve-controller"})
@RestController
@RequestMapping("/api")
public class ApproveController {


    private final Logger log = LoggerFactory.getLogger(ApproveController.class);

    private static final String ENTITY_NAME = "approve";

    private final ApproveService approveService;

    public ApproveController(ApproveService approveService) {
        this.approveService = approveService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /approves : Create a new approve.
     *
     * @param approve the approve to create
     * @return the ResponseEntity with status 201 (Created) and with body the new approve, or with status 400 (Bad Request) if the approve has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "Approve",name = "approve",value = "the approve to create")
    })
    @ApiOperation(value = "POST  /approves : create a new approve.",notes = "POST  /approves : create a new approve.",httpMethod = "POST")
    @PostMapping("/approves")
    public ResponseEntity<Approve> createApprove(@RequestBody  Approve approve) throws URISyntaxException {

        log.debug("REST request to save Approve : {}", approve);

        Approve result = approveService.insert(approve);
        return ResponseEntity.created(new URI("/api/approves/" + result.getId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /approves : Updates an existing approve.
     *
     * @param approve the approve to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated approve,
     * or with status 400 (Bad Request) if the approve is not valid,
     * or with status 500 (Internal Server Error) if the approve couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "Approve",name = "approve",value = "the approve to update")
    })
    @ApiOperation(value = "PUT  /approves : updates an existing approve.",notes = "PUT  /approves : updates an existing approve.",httpMethod = "PUT")
    @PutMapping("/approves")
    public ResponseEntity<Approve> updateApprove(@RequestBody  Approve approve) throws URISyntaxException {

        log.debug("REST request to update Approve : {}", approve);

        if (approve.getId() == null) {
            return createApprove(approve);
        }
        Approve result = approveService.update(approve);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /approves/:id : get the "id" approve.
     *
     * @param id the id of the approve to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the approve, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id",value = "the id of the approve to retrieve")
    })
    @ApiOperation(value = "GET  /approves/id : get the id approve.",notes = "GET  /approves/id : get the id approve.",httpMethod = "GET")
    @GetMapping("/approves/{id}")
    public ResponseEntity<Approve> getApprove(@PathVariable Long id) {

        log.debug("REST request to get Approve : {}", id);

        Approve approve = approveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(approve));
    }

    /**
     * GET  /approves : get the approves .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of approves in body
     */
    @ApiOperation(value = "GET  /approves ")
    @GetMapping("/approves")
    public PageResult<Approve> getApprovesList(ApproveExample approveExample,Pageable pageable) {

        log.debug("REST request to get ApprovesList");

        return approveService.findByExample(approveExample,pageable);
    }

    /**
     * DELETE  /approves/:id : delete the "id" approve.
     *
     * @param id the id of the approve to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id",value = "the id of the approve to delete")
    })
    @ApiOperation(value = "DELETE  /approves/id : delete the id approve.",notes = "DELETE  /approves/id : delete the id approve.",httpMethod = "DELETE")
    @DeleteMapping("/approves/{id}")
    public ResponseEntity<Void> deleteApprove(@PathVariable Long id) {
        log.debug("REST request to delete Approve : {}", id);
        approveService.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * GET  /approves/:id : get the "id" approve.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the approve, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /approves/export : export the approve to excel",notes = "DELETE  /approves/id : delete the id approve.",httpMethod = "DELETE")
    @PostMapping("/approves/export")
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export Approve");

        PageResult<Approve> result = approveService.findAll();
        List<Approve> list = result.getData();
        ExcelUtil<Approve> util = new ExcelUtil<Approve>(Approve.class);
        return util.exportExcel(list, "approve");
    }


    /**
    * DELETE  /ids : delete the approve.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE"
    *
    * @return
    */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "ids", value = "the ids of the approve to delete")
    })
    @ApiOperation(value = "DELETE  /ids : delete the approve.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE")
    @DeleteMapping("/approves/delAll")
    public ResponseEntity<Void> deleteApprove(Long[] ids) {

        log.debug("REST request to delete ids");

        approveService.deleteByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(ids))).build();
    }

}
