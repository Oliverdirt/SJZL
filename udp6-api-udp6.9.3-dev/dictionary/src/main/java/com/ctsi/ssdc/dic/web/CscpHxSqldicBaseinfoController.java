package com.ctsi.ssdc.dic.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.criteria.StringCriteria;
import com.ctsi.ssdc.dic.domain.CscpHxDicExample;
import com.ctsi.ssdc.dic.domain.CscpHxSqldicBaseinfo;
import com.ctsi.ssdc.dic.domain.CscpHxSqldicBaseinfoExample;
import com.ctsi.ssdc.dic.service.CscpHxDicService;
import com.ctsi.ssdc.dic.service.CscpHxSqldicBaseinfoService;
import com.ctsi.ssdc.model.AjaxBackData;
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
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing CscpHxSqldicBaseinfo.
 *
 * @author ctsi-biyi-generator
 *
 */
@Api(value = "/api",tags = {"cscp-hx-sqldic-baseinfo-controller"})
@RestController
@RequestMapping("/api")
public class CscpHxSqldicBaseinfoController {

    private final Logger log = LoggerFactory.getLogger(CscpHxSqldicBaseinfoController.class);

    private static final String ENTITY_NAME = "cscpHxSqldicBaseinfo";

//    @Autowired
//    private CscpDicService cscpDicService;
    @Autowired
    private CscpHxDicService cscpHxDicService;
    private final CscpHxSqldicBaseinfoService cscpHxSqldicBaseinfoService;

    public CscpHxSqldicBaseinfoController(CscpHxSqldicBaseinfoService cscpHxSqldicBaseinfoService) {
        this.cscpHxSqldicBaseinfoService = cscpHxSqldicBaseinfoService;
    }

    @InitBinder   
    public void initBinder(WebDataBinder binder) {   
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(true);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    } 
   
    /**
     * POST  /cscpHxSqldicBaseinfos : Create a new cscpHxSqldicBaseinfo.
     *
     * @param cscpHxSqldicBaseinfo the cscpHxSqldicBaseinfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpHxSqldicBaseinfo, or with status 400 (Bad Request) if the cscpHxSqldicBaseinfo has already an id
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxSqldicBaseinfo",name = "cscpHxSqldicBaseinfo",value = "the cscpHxSqldicBaseinfo to create")
    })
    @ApiOperation(value = "POST  /cscpHxSqldicBaseinfos : create a new cscpHxSqldicBaseinfo.",notes = "POST  /cscpHxSqldicBaseinfos : create a new cscpHxSqldicBaseinfo.",httpMethod = "POST")
    @PostMapping("/cscpHxSqldicBaseinfos")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "createCscpHxSqldicBaseinfo")
    @Log
    public ResponseEntity<CscpHxSqldicBaseinfo> createCscpHxSqldicBaseinfo(@RequestBody CscpHxSqldicBaseinfo cscpHxSqldicBaseinfo) throws URISyntaxException {
        log.debug("REST request to save CscpHxSqldicBaseinfo : {}", cscpHxSqldicBaseinfo);
        // TODO: 2022/2/10  字典编码唯一性校验
        boolean flag = dicCodeExited(cscpHxSqldicBaseinfo.getDicCode());
        if(flag){
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "Dictionary code cannot be repeated: "+cscpHxSqldicBaseinfo.getDicCode()))
                    .body(null);
        }

        CscpHxSqldicBaseinfo result = cscpHxSqldicBaseinfoService.insert(cscpHxSqldicBaseinfo);
        return ResponseEntity.created(new URI("/api/cscpHxSqldicBaseinfos" + "/" +result.getId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
	
    /**
     * PUT  /cscpHxSqldicBaseinfos : Updates an existing cscpHxSqldicBaseinfo.
     *
     * @param cscpHxSqldicBaseinfo the cscpHxSqldicBaseinfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpHxSqldicBaseinfo,
     * or with status 400 (Bad Request) if the cscpHxSqldicBaseinfo is not valid,
     * or with status 500 (Internal Server Error) if the cscpHxSqldicBaseinfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpHxSqldicBaseinfo",name = "cscpHxSqldicBaseinfo",value = "the cscpHxSqldicBaseinfo to update")
    })
    @ApiOperation(value = "PUT  /cscpHxSqldicBaseinfos : updates an existing cscpHxSqldicBaseinfo.",notes = "PUT  /cscpHxSqldicBaseinfos : updates an existing cscpHxSqldicBaseinfo.",httpMethod = "PUT")
    @PutMapping("/cscpHxSqldicBaseinfos")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "updateCscpHxSqldicBaseinfo")
    @Log
    public ResponseEntity<CscpHxSqldicBaseinfo> updateCscpHxSqldicBaseinfo(@RequestBody CscpHxSqldicBaseinfo cscpHxSqldicBaseinfo)  {
        log.debug("REST request to update CscpHxSqldicBaseinfo : {}", cscpHxSqldicBaseinfo);
        CscpHxSqldicBaseinfo result = cscpHxSqldicBaseinfoService.update(cscpHxSqldicBaseinfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpHxSqldicBaseinfos/:id : get the "id" cscpHxSqldicBaseinfo.
     *
     * @param id the id of the cscpHxSqldicBaseinfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpHxSqldicBaseinfo, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "id",value = "the id of the cscpHxSqldicBaseinfo to retrieve")
    })
    @ApiOperation(value = "GET  /cscpHxSqldicBaseinfos/id : get the id cscpHxSqldicBaseinfo.",notes = "GET  /cscpHxSqldicBaseinfos/id : get the id cscpHxSqldicBaseinfo.",httpMethod = "GET")
    @GetMapping("/cscpHxSqldicBaseinfos/{id}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getCscpHxSqldicBaseinfo")
    @Log
    public ResponseEntity<CscpHxSqldicBaseinfo> getCscpHxSqldicBaseinfo(@PathVariable Long id) {
        log.debug("REST request to get CscpHxSqldicBaseinfo : {}", id);
        CscpHxSqldicBaseinfo cscpHxSqldicBaseinfo = cscpHxSqldicBaseinfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpHxSqldicBaseinfo));
    }

    /**
    * GET  /cscpHxSqldicBaseinfos : get the cscpHxSqldicBaseinfos firstStringBaseColumn.
    *
    * @return the ResponseEntity with status 200 (OK) and the list of cscpHxSqldicBaseinfos in body
    */
    @ApiOperation(value = "GET  /cscpHxSqldicBaseinfos : get the cscpHxSqldicBaseinfos firstStringBaseColumn.",notes = "GET  /cscpHxSqldicBaseinfos : get the cscpHxSqldicBaseinfos firstStringBaseColumn.",httpMethod = "GET")
    @GetMapping("/cscpHxSqldicBaseinfos")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "getCscpHxSqldicBaseinfosList")
    @Log
    public PageResult<CscpHxSqldicBaseinfo> getCscpHxSqldicBaseinfosList(CscpHxSqldicBaseinfoExample cscpHxSqldicBaseinfoExample, Pageable pageable) {
        log.debug("REST request to get CscpHxSqldicBaseinfosList");
        return cscpHxSqldicBaseinfoService.findByExample(cscpHxSqldicBaseinfoExample,pageable);
    }
	
    /**
     * DELETE  /cscpHxSqldicBaseinfos/:id : delete the "id" cscpHxSqldicBaseinfo.
     *
     * @param id the id of the cscpHxSqldicBaseinfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "id",value = "the id of the cscpHxSqldicBaseinfo to delete")
    })
    @ApiOperation(value = "DELETE  /cscpHxSqldicBaseinfos/id : delete the id cscpHxSqldicBaseinfo.",notes = "DELETE  /cscpHxSqldicBaseinfos/id : delete the id cscpHxSqldicBaseinfo.",httpMethod = "DELETE")
    @DeleteMapping("/cscpHxSqldicBaseinfos/{id}")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "deleteCscpHxSqldicBaseinfo")
    @Log
    public ResponseEntity<Void> deleteCscpHxSqldicBaseinfo(@PathVariable Long id) {
        log.debug("REST request to delete CscpHxSqldicBaseinfo : {}", id);
        cscpHxSqldicBaseinfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
    * GET  /cscpHxSqldicBaseinfos/:id : get the "id" cscpHxSqldicBaseinfo.
    *
    * @return the ResponseEntity with status 200 (OK) and with body the cscpHxSqldicBaseinfo, or with status 404 (Not Found)
    */
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "path",dataType = "BIGINT",name = "id",value = "the id of the cscpHxSqldicBaseinfo to retrieve")
    })
    @ApiOperation(value = "POST  /cscpHxSqldicBaseinfos/export : export the cscpHxSqldicBaseinfo to excel",notes = "POST  /cscpHxSqldicBaseinfos/export : export the cscpHxSqldicBaseinfo to excel",httpMethod = "POST")
    @PostMapping("/cscpHxSqldicBaseinfos/export")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "exportSqldic")
    @Log
    public ResponseEntity<byte[]> export() {
        log.debug("REST request to export CscpHxSqldicBaseinfo");
        PageResult<CscpHxSqldicBaseinfo> result = cscpHxSqldicBaseinfoService.findAll();
        List<CscpHxSqldicBaseinfo> list = result.getData();
        ExcelUtil<CscpHxSqldicBaseinfo> util = new ExcelUtil<CscpHxSqldicBaseinfo>(CscpHxSqldicBaseinfo.class);
        return util.exportExcel(list, "cscpHxSqldicBaseinfo");
    }


    @GetMapping("/cscpHxSqldicBaseinfos/validateDicExisted")
    @ComponentCallAnno(component = HxComponentConstant.DICTIONARY,method = "validateDicExisted")
    @Log
    public AjaxBackData validateDicExisted(String dicCode) {
        // TODO: 2022/2/10 根据字典编码判断是否重复
        boolean flag = dicCodeExited(dicCode);
        AjaxBackData ajaxBackData = new AjaxBackData();
        if(flag){
            ajaxBackData.setCode(-1);
            ajaxBackData.setMsg("字典编码不能重复!");
        }else{
            ajaxBackData.setCode(200);
            ajaxBackData.setMsg("校验成功");
        }
        return ajaxBackData;
    }

    private boolean dicCodeExited(String dicCode) {
        // 字典表编码是否存在
        CscpHxDicExample cscpHxDicExample = new CscpHxDicExample();
        StringCriteria stringCriteria = new StringCriteria();
        stringCriteria.setEquals(dicCode);
        cscpHxDicExample.setDicCode(stringCriteria);
        boolean isExitedDic = cscpHxDicService.findByExample(cscpHxDicExample).getRecordsTotal() == 0 ? false:true;


        // sqlbase表编码是否存在
        CscpHxSqldicBaseinfoExample cscpHxSqldicBaseinfoExample = new CscpHxSqldicBaseinfoExample();
        StringCriteria stringCriteria2 = new StringCriteria();
        stringCriteria2.setEquals(dicCode);
        cscpHxSqldicBaseinfoExample.setDicCode(stringCriteria2);
        boolean isExitedSqlDic =cscpHxSqldicBaseinfoService.findByExample(cscpHxSqldicBaseinfoExample).getRecordsTotal() == 0? false:true;

        return isExitedDic || isExitedSqlDic;
    }


}
