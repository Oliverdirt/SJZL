package com.ctsi.ssdc.errorcode.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.errorcode.domain.CscpErrorCode;
import com.ctsi.ssdc.errorcode.domain.CscpErrorCodeExample;
import com.ctsi.ssdc.errorcode.service.CscpErrorCodeService;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;
import com.ctsi.ssdc.utils.HxStringUtils;
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
import java.time.ZonedDateTime;
import java.util.*;

import static com.ctsi.ssdc.errorcode.enums.ErrorCodeConstants.ERROR_CODE_DUPLICATE;
import static com.ctsi.ssdc.errorcode.util.ServiceExceptionUtil.exception;

/**
 * REST controller for managing CscpErrorCode.
 *
 * @author hx
 * @date 2022-09-05 15:56:17
 *
 */

@Api(value = "/api",tags = {"cscp-error-code-controller"})
@RestController
@RequestMapping("/api")
public class CscpErrorCodeController {


    private final Logger log = LoggerFactory.getLogger(CscpErrorCodeController.class);

    private static final String ENTITY_NAME = "cscpErrorCode";

    private final CscpErrorCodeService cscpErrorCodeService;

    public CscpErrorCodeController(CscpErrorCodeService cscpErrorCodeService) {
        this.cscpErrorCodeService = cscpErrorCodeService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpErrorCodes : Create a new cscpErrorCode.
     *
     * @param cscpErrorCode the cscpErrorCode to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpErrorCode, or with status 400 (Bad Request) if the cscpErrorCode has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpErrorCode",name = "cscpErrorCode",value = "the cscpErrorCode to create")
    })
    @ApiOperation(value = "POST  /cscpErrorCodes : create a new cscpErrorCode.",notes = "POST  /cscpErrorCodes : create a new cscpErrorCode.",httpMethod = "POST")
    @PostMapping("/cscpErrorCodes")
    @ComponentCallAnno(component = HxComponentConstant.ERRORCODE,method = "createCscpErrorCode")
    public ResponseEntity<CscpErrorCode> createCscpErrorCode(@RequestBody  CscpErrorCode cscpErrorCode) throws URISyntaxException {

        log.debug("REST request to save CscpErrorCode : {}", cscpErrorCode);

        cscpErrorCode.setCodeType("2");
        cscpErrorCode.setCreateTime(ZonedDateTime.now());
        CscpErrorCode result = cscpErrorCodeService.insert(cscpErrorCode);
        return ResponseEntity.created(new URI("/api/cscpErrorCodes/" + result.getCodeId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCodeId().toString()))
                .body(result);
    }


    /**
     * PUT  /cscpErrorCodes : Updates an existing cscpErrorCode.
     *
     * @param cscpErrorCode the cscpErrorCode to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpErrorCode,
     * or with status 400 (Bad Request) if the cscpErrorCode is not valid,
     * or with status 500 (Internal Server Error) if the cscpErrorCode couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpErrorCode",name = "cscpErrorCode",value = "the cscpErrorCode to update")
    })
    @ApiOperation(value = "PUT  /cscpErrorCodes : updates an existing cscpErrorCode.",notes = "PUT  /cscpErrorCodes : updates an existing cscpErrorCode.",httpMethod = "PUT")
    @PutMapping("/cscpErrorCodes")
    @ComponentCallAnno(component = HxComponentConstant.ERRORCODE,method = "updateCscpErrorCode")
    public ResponseEntity<CscpErrorCode> updateCscpErrorCode(@RequestBody  CscpErrorCode cscpErrorCode) throws URISyntaxException {

        log.debug("REST request to update CscpErrorCode : {}", cscpErrorCode);

        if (cscpErrorCode.getCodeId() == null) {
            return createCscpErrorCode(cscpErrorCode);
        }
        cscpErrorCode.setUpdateTime(ZonedDateTime.now());
        CscpErrorCode result = cscpErrorCodeService.update(cscpErrorCode);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getCodeId().toString()))
                .body(result);
    }

    /**
     * GET  /cscpErrorCodes/:codeId : get the "codeId" cscpErrorCode.
     *
     * @param codeId the id of the cscpErrorCode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpErrorCode, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "codeId",value = "the codeId of the cscpErrorCode to retrieve")
    })
    @ApiOperation(value = "GET  /cscpErrorCodes/codeId : get the codeId cscpErrorCode.",notes = "GET  /cscpErrorCodes/codeId : get the codeId cscpErrorCode.",httpMethod = "GET")
    @GetMapping("/cscpErrorCodes/{codeId}")
    @ComponentCallAnno(component = HxComponentConstant.ERRORCODE,method = "getCscpErrorCode")
    public ResponseEntity<CscpErrorCode> getCscpErrorCode(@PathVariable Long codeId) {

        log.debug("REST request to get CscpErrorCode : {}", codeId);

        CscpErrorCode cscpErrorCode = cscpErrorCodeService.findOne(codeId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpErrorCode));
    }

    /**
     * GET  /cscpErrorCodes : get the cscpErrorCodes .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpErrorCodes in body
     */
    @ApiOperation(value = "GET  /cscpErrorCodes ")
    @GetMapping("/cscpErrorCodes")
    @ComponentCallAnno(component = HxComponentConstant.ERRORCODE,method = "getCscpErrorCodesList")
    public PageResult<CscpErrorCode> getCscpErrorCodesList(CscpErrorCodeExample cscpErrorCodeExample, Pageable pageable) {

        log.debug("REST request to get CscpErrorCodesList");

        return cscpErrorCodeService.findByExample(cscpErrorCodeExample,pageable);
    }


    /**
     * 获取错误码列表
     */
    @GetMapping("/cscpErrorCode/getList")
    @ComponentCallAnno(component = HxComponentConstant.ERRORCODE,method = "getCscpErrorCodeList")
    public PageResult<CscpErrorCode> getCscpErrorCodeList(CscpErrorCode cscpErrorCode,Pageable pageable){
        log.debug("REST request to get ErrorCodeList");
        return cscpErrorCodeService.getCscpErrorCodes(cscpErrorCode,pageable);
    }

    /**
     * DELETE  /cscpErrorCodes/:codeId : delete the "codeId" cscpErrorCode.
     *
     * @param codeId the id of the cscpErrorCode to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "codeId",value = "the codeId of the cscpErrorCode to delete")
    })
    @ApiOperation(value = "DELETE  /cscpErrorCodes/codeId : delete the codeId cscpErrorCode.",notes = "DELETE  /cscpErrorCodes/codeId : delete the codeId cscpErrorCode.",httpMethod = "DELETE")
    @DeleteMapping("/cscpErrorCodes/{codeId}")
    @ComponentCallAnno(component = HxComponentConstant.ERRORCODE,method = "deleteCscpErrorCode")
    public ResponseEntity<Void> deleteCscpErrorCode(@PathVariable Long codeId) {
        log.debug("REST request to delete CscpErrorCode : {}", codeId);
        cscpErrorCodeService.deleteById(codeId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, codeId.toString())).build();
    }


    /**
     * GET  /cscpErrorCodes/:codeId : get the "codeId" cscpErrorCode.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cscpErrorCode, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /cscpErrorCodes/export : export the cscpErrorCode to excel",notes = "DELETE  /cscpErrorCodes/codeId : delete the codeId cscpErrorCode.",httpMethod = "DELETE")
    @PostMapping("/cscpErrorCodes/export")
    @ComponentCallAnno(component = HxComponentConstant.ERRORCODE,method = "export")
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export CscpErrorCode");

        PageResult<CscpErrorCode> result = cscpErrorCodeService.findAll();
        List<CscpErrorCode> list = result.getData();
        ExcelUtil<CscpErrorCode> util = new ExcelUtil<CscpErrorCode>(CscpErrorCode.class);
        return util.exportExcel(list, "cscpErrorCode");
    }


    /**
     * DELETE  /codeIds : delete the cscpErrorCode.", notes = "DELETE  /codeIds : delete the codeIds.", httpMethod = "DELETE"
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "codeIds", value = "the codeIds of the cscpErrorCode to delete")
    })
    @ApiOperation(value = "DELETE  /codeIds : delete the cscpErrorCode.", notes = "DELETE  /codeIds : delete the codeIds.", httpMethod = "DELETE")
    @DeleteMapping("/cscpErrorCodes/delAll")
    @ComponentCallAnno(component = HxComponentConstant.ERRORCODE,method = "deleteCscpErrorCode")
    public ResponseEntity<Void> deleteCscpErrorCode(Long[] codeIds) {

        log.debug("REST request to delete codeIds");

        cscpErrorCodeService.deleteByIds(codeIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(codeIds))).build();
    }

    @ApiOperation(value = "错误码编码重复校验")
    @PostMapping("/cscpErrorCodes/checkErrorCode")
    @ComponentCallAnno(component = HxComponentConstant.ERRORCODE,method = "checkErrorCode")
    @Log(message = "错误码编码重复校验")
    public ResponseEntity<Map> checkErrorCode(@RequestBody CscpErrorCode cscpErrorCode){
        log.debug("REST check CscpErrorCode ； {}",cscpErrorCode);
        Map<String,Object> retMap = new HashMap<>(500);
        int ret = cscpErrorCodeService.checkErrorCode(cscpErrorCode);
        if(ret >= 1){
            retMap.put("msg", exception(ERROR_CODE_DUPLICATE).getCodeMessage());
        }
        retMap.put("code",exception(ERROR_CODE_DUPLICATE).getCode());
        return ResponseEntity.ok().body(retMap);
    }

    /**
     *
     * 批量删除错误码
     */
    @DeleteMapping("/cscpErrorCodes/batchDel")
    @ApiOperation("批量删除错误码")
    @ComponentCallAnno(component = HxComponentConstant.ERRORCODE,method = "deleteErrorCodeByIds")
    public ResponseEntity<Void> deleteErrorCodeByIds(Long[] ids){
        log.debug("REST request to delete Code : {}", ids);
        cscpErrorCodeService.deleteErrorCodeByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, HxStringUtils.join(ids))).build();
    }



}
