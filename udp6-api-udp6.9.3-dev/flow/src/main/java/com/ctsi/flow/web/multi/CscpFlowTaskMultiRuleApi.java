package com.ctsi.flow.web.multi;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ctsi.flow.multi.loader.MultiStrategyDetail;
import com.ctsi.flow.param.Response;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.commons.lang.StringUtils;
import java.lang.Long;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.ctsi.flow.multi.service.CscpFlowTaskMultiRuleService;
import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRuleExample;
import com.ctsi.flow.multi.domain.CscpFlowTaskMultiRule;

import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;

/**
 * REST controller for managing CscpFlowTaskMultiRule.
 *
 * @author hx
 * @date 2022-10-24 16:49:41
 *
 */

@Api(value = "/api",tags = {"cscp-flow-task-multi-rule-controller"})
@RestController
@RequestMapping("/api/flow/multi")
public class CscpFlowTaskMultiRuleApi {


    private final Logger log = LoggerFactory.getLogger(CscpFlowTaskMultiRuleApi.class);

    private static final String ENTITY_NAME = "cscpFlowTaskMultiRule";

    private final CscpFlowTaskMultiRuleService cscpFlowTaskMultiRuleService;

    public CscpFlowTaskMultiRuleApi(CscpFlowTaskMultiRuleService cscpFlowTaskMultiRuleService) {
        this.cscpFlowTaskMultiRuleService = cscpFlowTaskMultiRuleService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpFlowTaskMultiRules : Create a new cscpFlowTaskMultiRule.
     *
     * @param cscpFlowTaskMultiRule the cscpFlowTaskMultiRule to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpFlowTaskMultiRule, or with status 400 (Bad Request) if the cscpFlowTaskMultiRule has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpFlowTaskMultiRule",name = "cscpFlowTaskMultiRule",value = "the cscpFlowTaskMultiRule to create")
    })
    @ApiOperation(value = "POST  /cscpFlowTaskMultiRules : create a new cscpFlowTaskMultiRule.",notes = "POST  /cscpFlowTaskMultiRules : create a new cscpFlowTaskMultiRule.",httpMethod = "POST")
    @PostMapping("/cscpFlowTaskMultiRules")
    public ResponseEntity<CscpFlowTaskMultiRule> createCscpFlowTaskMultiRule(@RequestBody  CscpFlowTaskMultiRule cscpFlowTaskMultiRule) throws URISyntaxException {

        log.debug("REST request to save CscpFlowTaskMultiRule : {}", cscpFlowTaskMultiRule);

        CscpFlowTaskMultiRule result = cscpFlowTaskMultiRuleService.insert(cscpFlowTaskMultiRule);
        return ResponseEntity.created(new URI("/api/cscpFlowTaskMultiRules/" + result.getId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /cscpFlowTaskMultiRules : Updates an existing cscpFlowTaskMultiRule.
     *
     * @param cscpFlowTaskMultiRule the cscpFlowTaskMultiRule to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpFlowTaskMultiRule,
     * or with status 400 (Bad Request) if the cscpFlowTaskMultiRule is not valid,
     * or with status 500 (Internal Server Error) if the cscpFlowTaskMultiRule couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpFlowTaskMultiRule",name = "cscpFlowTaskMultiRule",value = "the cscpFlowTaskMultiRule to update")
    })
    @ApiOperation(value = "PUT  /cscpFlowTaskMultiRules : updates an existing cscpFlowTaskMultiRule.",notes = "PUT  /cscpFlowTaskMultiRules : updates an existing cscpFlowTaskMultiRule.",httpMethod = "PUT")
    @PutMapping("/cscpFlowTaskMultiRules")
    public ResponseEntity<CscpFlowTaskMultiRule> updateCscpFlowTaskMultiRule(@RequestBody  CscpFlowTaskMultiRule cscpFlowTaskMultiRule) throws URISyntaxException {

        log.debug("REST request to update CscpFlowTaskMultiRule : {}", cscpFlowTaskMultiRule);

        if (cscpFlowTaskMultiRule.getId() == null) {
            return createCscpFlowTaskMultiRule(cscpFlowTaskMultiRule);
        }
        CscpFlowTaskMultiRule result = cscpFlowTaskMultiRuleService.update(cscpFlowTaskMultiRule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cscpFlowTaskMultiRules/:id : get the "id" cscpFlowTaskMultiRule.
     *
     * @param id the id of the cscpFlowTaskMultiRule to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpFlowTaskMultiRule, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id",value = "the id of the cscpFlowTaskMultiRule to retrieve")
    })
    @ApiOperation(value = "GET  /cscpFlowTaskMultiRules/id : get the id cscpFlowTaskMultiRule.",notes = "GET  /cscpFlowTaskMultiRules/id : get the id cscpFlowTaskMultiRule.",httpMethod = "GET")
    @GetMapping("/cscpFlowTaskMultiRules/{id}")
    public ResponseEntity<CscpFlowTaskMultiRule> getCscpFlowTaskMultiRule(@PathVariable Long id) {

        log.debug("REST request to get CscpFlowTaskMultiRule : {}", id);

        CscpFlowTaskMultiRule cscpFlowTaskMultiRule = cscpFlowTaskMultiRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpFlowTaskMultiRule));
    }

    /**
     * GET  /cscpFlowTaskMultiRules : get the cscpFlowTaskMultiRules .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpFlowTaskMultiRules in body
     */
    @ApiOperation(value = "GET  /cscpFlowTaskMultiRules ")
    @GetMapping("/cscpFlowTaskMultiRules")
    public PageResult<CscpFlowTaskMultiRule> getCscpFlowTaskMultiRulesList(CscpFlowTaskMultiRuleExample cscpFlowTaskMultiRuleExample,Pageable pageable) {

        log.debug("REST request to get CscpFlowTaskMultiRulesList");

        return cscpFlowTaskMultiRuleService.findByExample(cscpFlowTaskMultiRuleExample,pageable);
    }

    /**
     * DELETE  /cscpFlowTaskMultiRules/:id : delete the "id" cscpFlowTaskMultiRule.
     *
     * @param id the id of the cscpFlowTaskMultiRule to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id",value = "the id of the cscpFlowTaskMultiRule to delete")
    })
    @ApiOperation(value = "DELETE  /cscpFlowTaskMultiRules/id : delete the id cscpFlowTaskMultiRule.",notes = "DELETE  /cscpFlowTaskMultiRules/id : delete the id cscpFlowTaskMultiRule.",httpMethod = "DELETE")
    @DeleteMapping("/cscpFlowTaskMultiRules/{id}")
    public ResponseEntity<Void> deleteCscpFlowTaskMultiRule(@PathVariable Long id) {
        log.debug("REST request to delete CscpFlowTaskMultiRule : {}", id);
        cscpFlowTaskMultiRuleService.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * GET  /cscpFlowTaskMultiRules/:id : get the "id" cscpFlowTaskMultiRule.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cscpFlowTaskMultiRule, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /cscpFlowTaskMultiRules/export : export the cscpFlowTaskMultiRule to excel",notes = "DELETE  /cscpFlowTaskMultiRules/id : delete the id cscpFlowTaskMultiRule.",httpMethod = "DELETE")
    @PostMapping("/cscpFlowTaskMultiRules/export")
    public ResponseEntity<byte[]> export() {

        log.debug("REST request to export CscpFlowTaskMultiRule");

        PageResult<CscpFlowTaskMultiRule> result = cscpFlowTaskMultiRuleService.findAll();
        List<CscpFlowTaskMultiRule> list = result.getData();
        ExcelUtil<CscpFlowTaskMultiRule> util = new ExcelUtil<CscpFlowTaskMultiRule>(CscpFlowTaskMultiRule.class);
        return util.exportExcel(list, "cscpFlowTaskMultiRule");
    }


    /**
    * DELETE  /ids : delete the cscpFlowTaskMultiRule.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE"
    *
    * @return
    */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "ids", value = "the ids of the cscpFlowTaskMultiRule to delete")
    })
    @ApiOperation(value = "DELETE  /ids : delete the cscpFlowTaskMultiRule.", notes = "DELETE  /ids : delete the ids.", httpMethod = "DELETE")
    @DeleteMapping("/cscpFlowTaskMultiRules/delAll")
    public ResponseEntity<Void> deleteCscpFlowTaskMultiRule(Long[] ids) {

        log.debug("REST request to delete ids");

        cscpFlowTaskMultiRuleService.deleteByIds(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(ids))).build();
    }

    @GetMapping("/deleteMultiByKey")
    @ApiOperation(value = "删除节点多实例配置")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "deleteMultiByKey")
    @Log
    public Response<Void> deleteMultiByKey(@RequestParam String taskKey) {
        cscpFlowTaskMultiRuleService.deleteMultiByKey(taskKey);
        return Response.ok();
    }


    @GetMapping("/selectMultiByKey")
    @ApiOperation(value = "查询多实例节点配置")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "selectMultiByKey")
    @Log
    public Response<CscpFlowTaskMultiRule> selectMultiByKey(@RequestParam String taskKey) {
        return Response.ok(cscpFlowTaskMultiRuleService.selectMultiByKey(taskKey));
    }

    @PostMapping("/updateMulti")
    @ApiOperation(value = "修改多实例配置")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI, method = "updateMulti")
    @Log
    public Response<CscpFlowTaskMultiRule> updateMulti(@RequestBody  CscpFlowTaskMultiRule cscpFlowTaskMultiRule) {
        return Response.ok(cscpFlowTaskMultiRuleService.updateMulti(cscpFlowTaskMultiRule));
    }


}
