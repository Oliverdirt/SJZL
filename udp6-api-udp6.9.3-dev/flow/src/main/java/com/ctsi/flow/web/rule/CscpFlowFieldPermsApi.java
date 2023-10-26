package com.ctsi.flow.web.rule;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.alibaba.fastjson.JSONObject;
import com.ctsi.flow.param.Response;
import com.ctsi.flow.param.model.CscpFlowFieldPerms;
import com.ctsi.flow.param.model.CscpFlowFieldPermsExample;
import com.ctsi.flow.param.model.CscpTableOperation;
import com.ctsi.flow.server.rule.CscpFlowFieldPermsService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.model.AjaxResult;
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

import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;


/**
 * REST controller for managing CscpFlowFieldPerms.
 *
 * @author hx
 * @date 2022-10-24 15:47:57
 */

@Api(value = "/api", tags = {"cscp-flow-field-perms-controller"})
@RestController
@RequestMapping("/api")
public class CscpFlowFieldPermsApi {


    private final Logger log = LoggerFactory.getLogger(CscpFlowFieldPermsApi.class);

    private static final String ENTITY_NAME = "cscpFlowFieldPerms" ;

    private final CscpFlowFieldPermsService cscpFlowFieldPermsService;

    public CscpFlowFieldPermsApi(CscpFlowFieldPermsService cscpFlowFieldPermsService) {
        this.cscpFlowFieldPermsService = cscpFlowFieldPermsService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpFlowFieldPermss : Create a new cscpFlowFieldPerms.
     *
     * @param cscpFlowFieldPerms the cscpFlowFieldPerms to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpFlowFieldPerms, or with status 400 (Bad Request) if the cscpFlowFieldPerms has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CscpFlowFieldPerms", name = "cscpFlowFieldPerms", value = "the cscpFlowFieldPerms to create")
    })
    @ApiOperation(value = "POST  /cscpFlowFieldPermss : create a new cscpFlowFieldPerms.", notes = "POST  /cscpFlowFieldPermss : create a new cscpFlowFieldPerms.", httpMethod = "POST")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "createCscpFlowFieldPerms")
    @PostMapping("/cscpFlowFieldPermss")
    public ResponseEntity<CscpFlowFieldPerms> createCscpFlowFieldPerms(@RequestBody CscpFlowFieldPerms cscpFlowFieldPerms) throws URISyntaxException {

        log.debug("REST request to save CscpFlowFieldPerms : {}", cscpFlowFieldPerms);

        CscpFlowFieldPerms result = cscpFlowFieldPermsService.insert(cscpFlowFieldPerms);
        return ResponseEntity.created(new URI("/api/cscpFlowFieldPermss/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /cscpFlowFieldPermss : Updates an existing cscpFlowFieldPerms.
     *
     * @param cscpFlowFieldPerms the cscpFlowFieldPerms to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpFlowFieldPerms,
     * or with status 400 (Bad Request) if the cscpFlowFieldPerms is not valid,
     * or with status 500 (Internal Server Error) if the cscpFlowFieldPerms couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CscpFlowFieldPerms", name = "cscpFlowFieldPerms", value = "the cscpFlowFieldPerms to update")
    })
    @ApiOperation(value = "PUT  /cscpFlowFieldPermss : updates an existing cscpFlowFieldPerms.", notes = "PUT  /cscpFlowFieldPermss : updates an existing cscpFlowFieldPerms.", httpMethod = "PUT")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "updateCscpFlowFieldPerms")
    @PutMapping("/cscpFlowFieldPermss")
    public ResponseEntity<CscpFlowFieldPerms> updateCscpFlowFieldPerms(@RequestBody CscpFlowFieldPerms cscpFlowFieldPerms) throws URISyntaxException {

        log.debug("REST request to update CscpFlowFieldPerms : {}", cscpFlowFieldPerms);

        if (cscpFlowFieldPerms.getId() == null) {
            return createCscpFlowFieldPerms(cscpFlowFieldPerms);
        }
        CscpFlowFieldPerms result = cscpFlowFieldPermsService.update(cscpFlowFieldPerms);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * GET  /cscpFlowFieldPermss/:id : get the "id" cscpFlowFieldPerms.
     *
     * @param id the id of the cscpFlowFieldPerms to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpFlowFieldPerms, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "the id of the cscpFlowFieldPerms to retrieve")
    })
    @ApiOperation(value = "GET  /cscpFlowFieldPermss/id : get the id cscpFlowFieldPerms.", notes = "GET  /cscpFlowFieldPermss/id : get the id cscpFlowFieldPerms.", httpMethod = "GET")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getCscpFlowFieldPerms")
    @GetMapping("/cscpFlowFieldPermss/{id}")
    public ResponseEntity<CscpFlowFieldPerms> getCscpFlowFieldPerms(@PathVariable Long id) {

        log.debug("REST request to get CscpFlowFieldPerms : {}", id);

        CscpFlowFieldPerms cscpFlowFieldPerms = cscpFlowFieldPermsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpFlowFieldPerms));
    }

    /**
     * GET  /cscpFlowFieldPermss : get the cscpFlowFieldPermss .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpFlowFieldPermss in body
     */
    @ApiOperation(value = "GET  /cscpFlowFieldPermss ")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getCscpFlowFieldPermssList")
    @GetMapping("/cscpFlowFieldPermss")
    public PageResult<CscpFlowFieldPerms> getCscpFlowFieldPermssList(CscpFlowFieldPermsExample cscpFlowFieldPermsExample, Pageable pageable) {

        log.debug("REST request to get CscpFlowFieldPermssList");

        return cscpFlowFieldPermsService.findByExample(cscpFlowFieldPermsExample, pageable);
    }

    /**
     * DELETE  /cscpFlowFieldPermss/:id : delete the "id" cscpFlowFieldPerms.
     *
     * @param id the id of the cscpFlowFieldPerms to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "the id of the cscpFlowFieldPerms to delete")
    })
    @ApiOperation(value = "DELETE  /cscpFlowFieldPermss/id : delete the id cscpFlowFieldPerms.", notes = "DELETE  /cscpFlowFieldPermss/id : delete the id cscpFlowFieldPerms.", httpMethod = "DELETE")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "deleteCscpFlowFieldPerms")
    @DeleteMapping("/cscpFlowFieldPermss/{id}")
    public ResponseEntity<Void> deleteCscpFlowFieldPerms(@PathVariable Long id) {
        log.debug("REST request to delete CscpFlowFieldPerms : {}", id);
        cscpFlowFieldPermsService.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * GET  /cscpFlowFieldPermss/:id : get the "id" cscpFlowFieldPerms.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cscpFlowFieldPerms, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /cscpFlowFieldPermss/export : export the cscpFlowFieldPerms to excel", notes = "DELETE  /cscpFlowFieldPermss/id : delete the id cscpFlowFieldPerms.", httpMethod = "DELETE")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "exportCscpFlowFieldPermss")
    @PostMapping("/cscpFlowFieldPermss/export")
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export CscpFlowFieldPerms");

        PageResult<CscpFlowFieldPerms> result = cscpFlowFieldPermsService.findAll();
        List<CscpFlowFieldPerms> list = result.getData();
        ExcelUtil<CscpFlowFieldPerms> util = new ExcelUtil<CscpFlowFieldPerms>(CscpFlowFieldPerms.class);
        return util.exportExcel(list, "cscpFlowFieldPerms");
    }


    /**
     * DELETE  /ids : delete the cscpFlowFieldPerms.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE"
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "ids", value = "the ids of the cscpFlowFieldPerms to delete")
    })
    @ApiOperation(value = "DELETE  /ids : delete the cscpFlowFieldPerms.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "delAllCscpFlowFieldPermss")
    @DeleteMapping("/cscpFlowFieldPermss/delAll")
    public ResponseEntity<Void> deleteCscpFlowFieldPerms(Long[] ids) {

        log.debug("REST request to delete ids");

        cscpFlowFieldPermsService.deleteByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(ids))).build();
    }


    @ApiOperation(value = "根据任务标识修改字段权限", httpMethod = "POST")
    @PostMapping("/updateFieldsPerm")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "updateFieldsPerm")
    @Log
    public Response<Boolean> updateFieldsPerm(@RequestBody CscpFlowFieldPerms cscpFlowFieldPerms) {
        cscpFlowFieldPerms.setFieldPerms(JSONObject.toJSONString(cscpFlowFieldPerms.getFieldsPerms()));
        CscpFlowFieldPerms fieldPerms = cscpFlowFieldPermsService.findByTaskDefKey(cscpFlowFieldPerms);
        if (fieldPerms!=null){
            // 更新
            cscpFlowFieldPermsService.updateFieldsPerm(cscpFlowFieldPerms);
        } else {
            // 新增
            cscpFlowFieldPermsService.insert(cscpFlowFieldPerms);
        }
        return Response.ok();
    }


    @ApiOperation(value = "根据流程标识和任务标识查询字段权限",httpMethod = "GET")
    @GetMapping("/queryFieldPerms")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "queryFieldPerms")
    @Log
    public Response quryFieldPerms(CscpFlowFieldPerms cscpFlowFieldPerms){
        return Response.ok(cscpFlowFieldPermsService.findByTaskDefKey(cscpFlowFieldPerms));
    }


}
