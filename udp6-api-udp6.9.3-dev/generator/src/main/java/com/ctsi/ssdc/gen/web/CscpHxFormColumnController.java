package com.ctsi.ssdc.gen.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.gen.domain.CscpHxFormColumn;
import com.ctsi.ssdc.gen.domain.CscpHxFormColumnExample;
import com.ctsi.ssdc.gen.service.CscpHxFormColumnService;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing CscpHxFormColumn.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(value = "/api",tags = {"cscp-hx-form-column-controller"})
@RestController
@RequestMapping("/api")
public class CscpHxFormColumnController {

    private final Logger log = LoggerFactory.getLogger(CscpHxFormColumnController.class);

    private static final String ENTITY_NAME = "cscpHxFormColumn";

    private final CscpHxFormColumnService cscpHxFormColumnService;

    public CscpHxFormColumnController(CscpHxFormColumnService cscpHxFormColumnService) {
        this.cscpHxFormColumnService = cscpHxFormColumnService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
   
    /**
     * POST  /cscpHxFormColumns : Create a new cscpHxFormColumn.
     *
     * @param cscpHxFormColumn the cscpHxFormColumn to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpHxFormColumn, or with status 400 (Bad Request) if the cscpHxFormColumn has already an columnId
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxFormColumn",name = "cscpHxFormColumn",value = "the cscpHxFormColumn to create")
    })
    @ApiOperation(value = "POST  /cscpHxFormColumns : create a new cscpHxFormColumn.",notes = "POST  /cscpHxFormColumns : create a new cscpHxFormColumn.",httpMethod = "POST")
    @PostMapping("/cscpHxFormColumns")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "createCscpHxFormColumn")
    @Log
    public ResponseEntity<CscpHxFormColumn> createCscpHxFormColumn(@RequestBody CscpHxFormColumn cscpHxFormColumn) throws URISyntaxException {
        log.debug("REST request to save CscpHxFormColumn : {}", cscpHxFormColumn);
        if (cscpHxFormColumn.getColumnId() != null) {
            throw new BadRequestAlertException("A new cscpHxFormColumn cannot already have an id", ENTITY_NAME, "idexists");
        }
        CscpHxFormColumn result = cscpHxFormColumnService.insert(cscpHxFormColumn);
        return ResponseEntity.created(new URI("/api/cscpHxFormColumns" + "/" +result.getColumnId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getColumnId().toString()))
            .body(result);
    }
	
    /**
     * PUT  /cscpHxFormColumns : Updates an existing cscpHxFormColumn.
     *
     * @param cscpHxFormColumn the cscpHxFormColumn to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpHxFormColumn,
     * or with status 400 (Bad Request) if the cscpHxFormColumn is not valid,
     * or with status 500 (Internal Server Error) if the cscpHxFormColumn couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxFormColumn",name = "cscpHxFormColumn",value = "the cscpHxFormColumn to update")
    })
    @ApiOperation(value = "PUT  /cscpHxFormColumns : updates an existing cscpHxFormColumn.",notes = "PUT  /cscpHxFormColumns : updates an existing cscpHxFormColumn.",httpMethod = "PUT")
    @PutMapping("/cscpHxFormColumns")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "updateCscpHxFormColumn")
    @Log
    public ResponseEntity<CscpHxFormColumn> updateCscpHxFormColumn(@RequestBody CscpHxFormColumn cscpHxFormColumn) throws URISyntaxException  {
        log.debug("REST request to update CscpHxFormColumn : {}", cscpHxFormColumn);
        if (cscpHxFormColumn.getColumnId() == null) {
            return createCscpHxFormColumn(cscpHxFormColumn);
        }
        CscpHxFormColumn result = cscpHxFormColumnService.update(cscpHxFormColumn);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getColumnId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpHxFormColumns/:columnId : get the "columnId" cscpHxFormColumn.
     *
     * @param columnId the id of the cscpHxFormColumn to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpHxFormColumn, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "columnId",value = "the columnId of the cscpHxFormColumn to retrieve")
    })
    @ApiOperation(value = "GET  /cscpHxFormColumns/columnId : get the columnId cscpHxFormColumn.",notes = "GET  /cscpHxFormColumns/columnId : get the columnId cscpHxFormColumn.",httpMethod = "GET")
    @GetMapping("/cscpHxFormColumns/{columnId}")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getCscpHxFormColumn")
    @Log
    public ResponseEntity<CscpHxFormColumn> getCscpHxFormColumn(@PathVariable Long columnId) {
        log.debug("REST request to get CscpHxFormColumn : {}", columnId);
        CscpHxFormColumn cscpHxFormColumn = cscpHxFormColumnService.findOne(columnId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpHxFormColumn));
    }

    /**
    * GET  /cscpHxFormColumns : get the cscpHxFormColumns firstStringBaseColumn.
    *
    * @return the ResponseEntity with status 200 (OK) and the list of cscpHxFormColumns in body
    */
    @ApiOperation(value = "GET  /cscpHxFormColumns : get the cscpHxFormColumns firstStringBaseColumn.",notes = "GET  /cscpHxFormColumns : get the cscpHxFormColumns firstStringBaseColumn.",httpMethod = "GET")
    @GetMapping("/cscpHxFormColumns")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "getCscpHxFormColumnsList")
    @Log
    public PageResult<CscpHxFormColumn> getCscpHxFormColumnsList(CscpHxFormColumnExample cscpHxFormColumnExample, Pageable pageable) {
        log.debug("REST request to get CscpHxFormColumnsList");
        return cscpHxFormColumnService.findByExample(cscpHxFormColumnExample,pageable);
    }
	
    /**
     * DELETE  /cscpHxFormColumns/:columnId : delete the "columnId" cscpHxFormColumn.
     *
     * @param columnId the id of the cscpHxFormColumn to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "columnId",value = "the columnId of the cscpHxFormColumn to delete")
    })
    @ApiOperation(value = "DELETE  /cscpHxFormColumns/columnId : delete the columnId cscpHxFormColumn.",notes = "DELETE  /cscpHxFormColumns/columnId : delete the columnId cscpHxFormColumn.",httpMethod = "DELETE")
    @DeleteMapping("/cscpHxFormColumns/{columnId}")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "deleteCscpHxFormColumn")
    @Log
    public ResponseEntity<Void> deleteCscpHxFormColumn(@PathVariable Long columnId) {
        log.debug("REST request to delete CscpHxFormColumn : {}", columnId);
        cscpHxFormColumnService.delete(columnId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, columnId.toString())).build();
    }

    /**
    * GET  /cscpHxFormColumns/:columnId : get the "columnId" cscpHxFormColumn.
    *
    * @return the ResponseEntity with status 200 (OK) and with body the cscpHxFormColumn, or with status 404 (Not Found)
    */
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "columnId",value = "the columnId of the cscpHxFormColumn to retrieve")
    })
    @ApiOperation(value = "POST  /cscpHxFormColumns/export : export the cscpHxFormColumn to excel",notes = "POST  /cscpHxFormColumns/export : export the cscpHxFormColumn to excel",httpMethod = "POST")
    @PostMapping("/cscpHxFormColumns/export")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "exportHxFormColumns")
    @Log
    public ResponseEntity<byte[]> export() {
        log.debug("REST request to export CscpHxFormColumn");
        PageResult<CscpHxFormColumn> result = cscpHxFormColumnService.findAll();
        List<CscpHxFormColumn> list = result.getData();
        ExcelUtil<CscpHxFormColumn> util = new ExcelUtil<CscpHxFormColumn>(CscpHxFormColumn.class);
        return util.exportExcel(list, "cscpHxFormColumn");
    }



    
}
