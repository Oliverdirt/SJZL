package com.ctsi.ssdc.dic.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.dic.domain.CscpHxDicCustom;
import com.ctsi.ssdc.dic.domain.CscpHxDicCustomExample;
import com.ctsi.ssdc.dic.service.CscpHxDicCustomService;
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
 * REST controller for managing CscpHxDicCustom.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(value = "/api",tags = {"cscp-hx-dic-custom-controller"})
@RestController
@RequestMapping("/api")
public class CscpHxDicCustomController {

    private final Logger log = LoggerFactory.getLogger(CscpHxDicCustomController.class);

    private static final String ENTITY_NAME = "cscpHxDicCustom";

    private final CscpHxDicCustomService cscpHxDicCustomService;

    public CscpHxDicCustomController(CscpHxDicCustomService cscpHxDicCustomService) {
        this.cscpHxDicCustomService = cscpHxDicCustomService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
   
    /**
     * POST  /cscpHxDicCustoms : Create a new cscpHxDicCustom.
     *
     * @param cscpHxDicCustom the cscpHxDicCustom to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpHxDicCustom, or with status 400 (Bad Request) if the cscpHxDicCustom has already an dicId
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxDicCustom",name = "cscpHxDicCustom",value = "the cscpHxDicCustom to create")
    })
    @ApiOperation(value = "POST  /cscpHxDicCustoms : create a new cscpHxDicCustom.",notes = "POST  /cscpHxDicCustoms : create a new cscpHxDicCustom.",httpMethod = "POST")
    @PostMapping("/cscpHxDicCustoms")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "createCscpHxDicCustom")
    @Log
    public ResponseEntity<Long> createCscpHxDicCustom(@RequestBody CscpHxDicCustom cscpHxDicCustom) throws URISyntaxException {
        log.debug("REST request to save CscpHxDicCustom : {}", cscpHxDicCustom);
        CscpHxDicCustom result = cscpHxDicCustomService.insert(cscpHxDicCustom);
        return ResponseEntity.created(new URI("/api/cscpHxDicCustoms" + "/" +result.getDicId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getDicId().toString()))
            .body(result.getDicId());
    }
	
    /**
     * PUT  /cscpHxDicCustoms : Updates an existing cscpHxDicCustom.
     *
     * @param cscpHxDicCustom the cscpHxDicCustom to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpHxDicCustom,
     * or with status 400 (Bad Request) if the cscpHxDicCustom is not valid,
     * or with status 500 (Internal Server Error) if the cscpHxDicCustom couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxDicCustom",name = "cscpHxDicCustom",value = "the cscpHxDicCustom to update")
    })
    @ApiOperation(value = "PUT  /cscpHxDicCustoms : updates an existing cscpHxDicCustom.",notes = "PUT  /cscpHxDicCustoms : updates an existing cscpHxDicCustom.",httpMethod = "PUT")
    @PutMapping("/cscpHxDicCustoms")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "updateCscpHxDicCustom")
    @Log
    public ResponseEntity<Long> updateCscpHxDicCustom(@RequestBody CscpHxDicCustom cscpHxDicCustom)  {
        log.debug("REST request to update CscpHxDicCustom : {}", cscpHxDicCustom);
        CscpHxDicCustom result = cscpHxDicCustomService.update(cscpHxDicCustom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getDicId().toString()))
            .body(result.getDicId());
    }

    /**
     * GET  /cscpHxDicCustoms/:dicId : get the "dicId" cscpHxDicCustom.
     *
     * @param dicId the id of the cscpHxDicCustom to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpHxDicCustom, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "dicId",value = "the dicId of the cscpHxDicCustom to retrieve")
    })
    @ApiOperation(value = "GET  /cscpHxDicCustoms/dicId : get the dicId cscpHxDicCustom.",notes = "GET  /cscpHxDicCustoms/dicId : get the dicId cscpHxDicCustom.",httpMethod = "GET")
    @GetMapping("/cscpHxDicCustoms/{dicId}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getCscpHxDicCustom")
    @Log
    public ResponseEntity<CscpHxDicCustom> getCscpHxDicCustom(@PathVariable Long dicId) {
        log.debug("REST request to get CscpHxDicCustom : {}", dicId);
        CscpHxDicCustom cscpHxDicCustom = cscpHxDicCustomService.findOne(dicId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpHxDicCustom));
    }

    /**
    * GET  /cscpHxDicCustoms : get the cscpHxDicCustoms firstStringBaseColumn.
    *
    * @return the ResponseEntity with status 200 (OK) and the list of cscpHxDicCustoms in body
    */
    @ApiOperation(value = "GET  /cscpHxDicCustoms : get the cscpHxDicCustoms firstStringBaseColumn.",notes = "GET  /cscpHxDicCustoms : get the cscpHxDicCustoms firstStringBaseColumn.",httpMethod = "GET")
    @GetMapping("/cscpHxDicCustoms")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getCscpHxDicCustomsList")
    @Log
    public PageResult<CscpHxDicCustom> getCscpHxDicCustomsList(CscpHxDicCustomExample cscpHxDicCustomExample, Pageable pageable) {
        log.debug("REST request to get CscpHxDicCustomsList");
        return cscpHxDicCustomService.findByExample(cscpHxDicCustomExample,pageable);
    }
	
    /**
     * DELETE  /cscpHxDicCustoms/:dicId : delete the "dicId" cscpHxDicCustom.
     *
     * @param dicId the id of the cscpHxDicCustom to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "dicId",value = "the dicId of the cscpHxDicCustom to delete")
    })
    @ApiOperation(value = "DELETE  /cscpHxDicCustoms/dicId : delete the dicId cscpHxDicCustom.",notes = "DELETE  /cscpHxDicCustoms/dicId : delete the dicId cscpHxDicCustom.",httpMethod = "DELETE")
    @DeleteMapping("/cscpHxDicCustoms/{dicId}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "deleteCscpHxDicCustom")
    @Log
    public ResponseEntity<Void> deleteCscpHxDicCustom(@PathVariable Long dicId) {
        log.debug("REST request to delete CscpHxDicCustom : {}", dicId);
        cscpHxDicCustomService.delete(dicId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, dicId.toString())).build();
    }

    /**
    * GET  /cscpHxDicCustoms/:dicId : get the "dicId" cscpHxDicCustom.
    *
    * @return the ResponseEntity with status 200 (OK) and with body the cscpHxDicCustom, or with status 404 (Not Found)
    */
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "dicId",value = "the dicId of the cscpHxDicCustom to retrieve")
    })
    @ApiOperation(value = "POST  /cscpHxDicCustoms/export : export the cscpHxDicCustom to excel",notes = "POST  /cscpHxDicCustoms/export : export the cscpHxDicCustom to excel",httpMethod = "POST")
    @PostMapping("/cscpHxDicCustoms/export")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "exportDicCustoms")
    @Log
    public ResponseEntity<byte[]> export() {
        log.debug("REST request to export CscpHxDicCustom");
        PageResult<CscpHxDicCustom> result = cscpHxDicCustomService.findAll();
        List<CscpHxDicCustom> list = result.getData();
        ExcelUtil<CscpHxDicCustom> util = new ExcelUtil<CscpHxDicCustom>(CscpHxDicCustom.class);
        return util.exportExcel(list, "cscpHxDicCustom");
    }



    
}
