package com.ctsi.ssdc.dic.web;


import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.dic.consts.CscpDicAttrType;
import com.ctsi.ssdc.dic.domain.CscpHxDic;
import com.ctsi.ssdc.dic.domain.CscpHxDicExample;
import com.ctsi.ssdc.dic.service.CscpHxDicService;
import com.ctsi.ssdc.dic.validator.ValidatorDicEnums;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import com.ctsi.ssdc.utils.HxStringUtils;
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
import java.util.*;


/**
 * REST controller for managing CscpHxDic.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(value = "/api",tags = {"cscp-hx-dic-controller"})
@RestController
@RequestMapping("/api/dic")
public class CscpHxDicController {

    private final Logger log = LoggerFactory.getLogger(CscpHxDicController.class);

    private static final String ENTITY_NAME = "cscpHxDic";

    private final CscpHxDicService cscpHxDicService;

    public CscpHxDicController(CscpHxDicService cscpHxDicService) {
        this.cscpHxDicService = cscpHxDicService;
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpHxDics : Create a new cscpHxDic.
     *
     * @param cscpHxDic the cscpHxDic to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpHxDic, or with status 400 (Bad Request) if the cscpHxDic has already an dicId
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxDic",name = "cscpHxDic",value = "the cscpHxDic to create")
    })
    @ApiOperation(value = "POST  /cscpHxDics : create a new cscpHxDic.",notes = "POST  /cscpHxDics : create a new cscpHxDic.",httpMethod = "POST")
    @PostMapping("/cscpHxDics")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "createCscpHxDic")
    @Log
    public ResponseEntity<CscpHxDic> createCscpHxDic(@RequestBody CscpHxDic cscpHxDic) throws URISyntaxException {
        log.debug("REST request to save CscpHxDic : {}", cscpHxDic);
        CscpHxDic result = cscpHxDicService.insert(cscpHxDic);
        return ResponseEntity.created(new URI("/api/cscpHxDics" + "/" +result.getDicId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getDicId().toString()))
            .body(result);
    }


    @ApiOperation(value = "字典名称重复校验")
    @PostMapping("/cscpHxDics/checkCscpHxDicName")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "checkCscpHxDicName")
    @Log(message = "字典名称重复校验")
    public ResponseEntity<Map> checkCscpHxDicName(@RequestBody CscpHxDic cscpHxDic)  {
        log.debug("REST check CscpHxDic : {}", cscpHxDic);
        Map<String,Object> retMap = new HashMap(500);
        int ret = cscpHxDicService.checkCscpHxDicName(cscpHxDic);
        if(ret >= 1){
            retMap.put("msg",ValidatorDicEnums.DIC_NAME_REP.getMsg());
        }
        retMap.put("code",ret);
        return ResponseEntity.ok().body(retMap);
    }

    @ApiOperation(value = "字典编码重复校验")
    @PostMapping("/cscpHxDics/checkCscpHxDicCode")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "checkCscpHxDicCode")
    @Log(message = "字典编码重复校验")
    public ResponseEntity<Map> checkCscpHxDicCode(@RequestBody CscpHxDic cscpHxDic) {
        log.debug("REST check CscpHxDic : {}", cscpHxDic);
        Map<String,Object> retMap = new HashMap(500);
        int ret = cscpHxDicService.checkCscpHxDicCode(cscpHxDic);
        if(ret >= 1){
            retMap.put("msg", ValidatorDicEnums.DIC_CODE_REP.getMsg());
        }
        retMap.put("code",ret);
        return ResponseEntity.ok().body(retMap);
    }

    /**
     * PUT  /cscpHxDics : Updates an existing cscpHxDic.
     *
     * @param cscpHxDic the cscpHxDic to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpHxDic,
     * or with status 400 (Bad Request) if the cscpHxDic is not valid,
     * or with status 500 (Internal Server Error) if the cscpHxDic couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxDic",name = "cscpHxDic",value = "the cscpHxDic to update")
    })
    @ApiOperation(value = "PUT  /cscpHxDics : updates an existing cscpHxDic.",notes = "PUT  /cscpHxDics : updates an existing cscpHxDic.",httpMethod = "PUT")
    @PutMapping("/cscpHxDics")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "updateCscpHxDic")
    @Log
    public ResponseEntity<CscpHxDic> updateCscpHxDic(@RequestBody CscpHxDic cscpHxDic)  {
        log.debug("REST request to update CscpHxDic : {}", cscpHxDic);
        CscpHxDic result = cscpHxDicService.update(cscpHxDic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getDicId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpHxDics/:dicId : get the "dicId" cscpHxDic.
     *
     * @param dicId the id of the cscpHxDic to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpHxDic, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "dicId",value = "the dicId of the cscpHxDic to retrieve")
    })
    @ApiOperation(value = "GET  /cscpHxDics/dicId : get the dicId cscpHxDic.",notes = "GET  /cscpHxDics/dicId : get the dicId cscpHxDic.",httpMethod = "GET")
    @GetMapping("/cscpHxDics/{dicId}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getCscpHxDic")
    @Log
    public ResponseEntity<CscpHxDic> getCscpHxDic(@PathVariable Long dicId) {
        log.debug("REST request to get CscpHxDic : {}", dicId);
        CscpHxDic cscpHxDic = cscpHxDicService.findOne(dicId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpHxDic));
    }

    /**
    * GET  /cscpHxDics : get the cscpHxDics list.
    *
    * @return the ResponseEntity with status 200 (OK) and the list of cscpHxDics in body
    */
    @ApiOperation(value = "GET  /cscpHxDics : get the cscpHxDics list.",notes = "GET  /cscpHxDics : get the cscpHxDics list.",httpMethod = "GET")
    @GetMapping("/cscpHxDics")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getCscpHxDicsList")
    @Log
    public PageResult<CscpHxDic> getCscpHxDicsList(CscpHxDicExample cscpHxDicExample, Pageable pageable) {
        log.debug("REST request to get CscpHxDicsList");
        return cscpHxDicService.findByExample(cscpHxDicExample,pageable);
    }

    /**
     * GET  /cscpBasicHxDics : get the basic cscpHxDics list .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpHxDics in body
     */
    @ApiOperation(value = "GET  /cscpBasicHxDics : get the the basic cscpHxDics list",notes = "GET  /cscpBasicHxDics : get the the basic cscpHxDics list.",httpMethod = "GET")
    @GetMapping("/cscpBasicHxDics")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getCscpBasicHxDicsList")
    @Log
    public PageResult<CscpHxDic> getCscpBasicHxDicsList() {
        log.debug("REST request to get getCscpBasicHxDicsList");
        CscpHxDicExample cscpHxDicExample =new CscpHxDicExample();
        cscpHxDicExample.createCriteria().andDicAttrNotEqualTo(CscpDicAttrType.TREE);
        return cscpHxDicService.findByExample(cscpHxDicExample,null);
    }

    /**
     * DELETE  /cscpHxDics/:dicId : delete the "dicId" cscpHxDic.
     *
     * @param dicId the id of the cscpHxDic to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "dicId",value = "the dicId of the cscpHxDic to delete")
    })
    @ApiOperation(value = "DELETE  /cscpHxDics/dicId : delete the dicId cscpHxDic.",notes = "DELETE  /cscpHxDics/dicId : delete the dicId cscpHxDic.",httpMethod = "DELETE")
    @DeleteMapping("/cscpHxDics/{dicId}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "deleteCscpHxDic")
    @Log
    public ResponseEntity<Void> deleteCscpHxDic(@PathVariable Long dicId) {
        log.debug("REST request to delete CscpHxDic : {}", dicId);
        cscpHxDicService.deleteById(dicId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, dicId.toString())).build();
    }

    /**
    * GET  /cscpHxDics/:dicId : get the "dicId" cscpHxDic.
    *
    * @return the ResponseEntity with status 200 (OK) and with body the cscpHxDic, or with status 404 (Not Found)
    */
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "dicId",value = "the dicId of the cscpHxDic to retrieve")
    })
    @ApiOperation(value = "POST  /cscpHxDics/export : export the cscpHxDic to excel",notes = "POST  /cscpHxDics/export : export the cscpHxDic to excel",httpMethod = "POST")
    @PostMapping("/cscpHxDics/export")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "exportDics")
    @Log
    public ResponseEntity<byte[]> export() {
        log.debug("REST request to export CscpHxDic");
        PageResult<CscpHxDic> result = cscpHxDicService.findAll();
        List<CscpHxDic> list = result.getData();
        ExcelUtil<CscpHxDic> util = new ExcelUtil<CscpHxDic>(CscpHxDic.class);
        return util.exportExcel(list, "cscpHxDic");
    }

    /**
     * 批量删除
     * @param dicIds
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "BIGINT", name = "dicIds", value = "the id of the tenant to delete")
    })
    @ApiOperation(value = "DELETE  /jobs : delete the cscpHxDics.", notes = "DELETE  /dicIds : delete the ids .", httpMethod = "DELETE")
    @DeleteMapping("/cscpHxDics/delAll")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "deleteAll")
    @Log
    public ResponseEntity<Void> deleteAll(Long[] dicIds) {
        log.debug("REST request to delete Job : {}", dicIds);
        cscpHxDicService.deleteByIds(dicIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, HxStringUtils.join(dicIds))).build();
    }


}
