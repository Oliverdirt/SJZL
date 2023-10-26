package com.ctsi.ssdc.customize.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.customize.domain.*;
import com.ctsi.ssdc.customize.service.CscpCustomizeModelService;
import com.ctsi.ssdc.customize.service.CscpCustomizeVfieldPageService;
import com.ctsi.ssdc.customize.service.CscpCustomizeVpageService;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.service.CscpHxFormTableService;
import com.ctsi.ssdc.security.SecurityHxUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import java.lang.Long;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    import com.ctsi.ssdc.customize.service.CscpCustomizeModuleService;

import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;

/**
 * @author hx
 * @date 2022-08-29 16:34:09
 * @description ：低代码-资源模块
 */

@Api(value = "/api",tags = {"低代码-资源模块"})
@RestController
@RequestMapping("/api")
public class CscpCustomizeModuleController {


    private final Logger log = LoggerFactory.getLogger(CscpCustomizeModuleController.class);

    private static final String ENTITY_NAME = "cscpCustomizeModule";

    private final CscpCustomizeModuleService cscpCustomizeModuleService;
    @Autowired
    private CscpCustomizeModelService cscpCustomizeModelService;
    @Autowired
    private CscpHxFormTableService cscpHxFormTableService;
    @Autowired
    private CscpCustomizeVpageService cscpCustomizeVpageService;
    @Autowired
    private CscpCustomizeVfieldPageService cscpCustomizeVfieldPageService;

    public CscpCustomizeModuleController(CscpCustomizeModuleService cscpCustomizeModuleService) {
        this.cscpCustomizeModuleService = cscpCustomizeModuleService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 模块的新增
     */
    @ApiOperation(value = "模块的新增",httpMethod = "POST")
    @PostMapping("/cscpCustomizeModules")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "createCscpCustomizeModule")
    public ResponseEntity<CscpCustomizeModule> createCscpCustomizeModule(@RequestBody  CscpCustomizeModule cscpCustomizeModule) throws URISyntaxException {
        log.debug("REST request to save CscpCustomizeModule : {}", cscpCustomizeModule);

        cscpCustomizeModule.setCreatedBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeModule.setCreateTime(ZonedDateTime.now());
        cscpCustomizeModule.setUpdateTime(ZonedDateTime.now());
        cscpCustomizeModule.setTenantId(SecurityHxUtils.getCurrentTenantId());
        CscpCustomizeModule result = cscpCustomizeModuleService.insert(cscpCustomizeModule);
        return ResponseEntity.created(new URI("/api/cscpCustomizeModules/" + result.getModuleId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getModuleId().toString()))
            .body(result);
    }
    /**
     * 模块的修改
     */
    @ApiOperation(value = "模块的修改",httpMethod = "PUT")
    @PutMapping("/cscpCustomizeModules")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "updateCscpCustomizeModule")
    public ResponseEntity<CscpCustomizeModule> updateCscpCustomizeModule(@RequestBody  CscpCustomizeModule cscpCustomizeModule) throws URISyntaxException {
        log.debug("REST request to update CscpCustomizeModule : {}", cscpCustomizeModule);

        if (cscpCustomizeModule.getModuleId() == null) {
            return createCscpCustomizeModule(cscpCustomizeModule);
        }
        cscpCustomizeModule.setUpdateBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeModule.setUpdateTime(ZonedDateTime.now());
        CscpCustomizeModule result = cscpCustomizeModuleService.update(cscpCustomizeModule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getModuleId().toString()))
            .body(result);
    }

    /**
     * 获取单条模块信息
     */
    @ApiOperation(value = "获取单条模块信息",httpMethod = "GET")
    @GetMapping("/cscpCustomizeModules/{moduleId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "getCscpCustomizeModule")
    public ResponseEntity<CscpCustomizeModule> getCscpCustomizeModule(@PathVariable Long moduleId) {
        log.debug("REST request to get CscpCustomizeModule : {}", moduleId);

        CscpCustomizeModule cscpCustomizeModule = cscpCustomizeModuleService.findOne(moduleId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeModule));
    }
    /**
     * 根据条件查询模块
     */
    @ApiOperation(value = "根据条件查询模块",httpMethod = "GET")
    @GetMapping("/cscpCustomizeModules")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "getCscpCustomizeModulesList")
    public PageResult<CscpCustomizeModule> getCscpCustomizeModulesList(CscpCustomizeModuleExample cscpCustomizeModuleExample,Pageable pageable) {
        log.debug("REST request to get CscpCustomizeModulesList");

        return cscpCustomizeModuleService.findByExample(cscpCustomizeModuleExample,pageable);
    }

    /**
     * 删除单条模块数据
     */
    @ApiOperation(value = "删除单条模块数据",httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeModules/{moduleId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "deleteCscpCustomizeModule")
    public ResponseEntity<Void> deleteCscpCustomizeModule(@PathVariable Long moduleId) {
        log.debug("REST request to delete CscpCustomizeModule : {}", moduleId);
        cscpCustomizeModuleService.deleteOne(moduleId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, moduleId.toString())).build();
    }

    /**
     * 批量删除模块数据
     */
    @ApiOperation(value = "批量删除模块数据",  httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeModules/delAll")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "deleteAllCscpCustomizeModule")
    public ResponseEntity<Void> deleteAllCscpCustomizeModule(Long[] moduleId) {
        log.debug("REST request to delete moduleId");
        cscpCustomizeModuleService.deleteAll(moduleId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, StringUtils.join(moduleId))).build();
    }

    /**
     * 校验模块名称是否重复
     */
    @ApiOperation(value = "校验模块名称是否重复",httpMethod = "GET")
    @GetMapping("/getCscpCustomizeModuleByName")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE,method = "getCscpCustomizeModuleByName")
    public ResponseEntity<List<CscpCustomizeModule>> getCscpCustomizeModuleByName(String moduleName) {

        log.debug("REST request to get CscpCustomizeModule : {}", moduleName);
        CscpCustomizeModuleExample cscpCustomizeModuleExample = new CscpCustomizeModuleExample();
        cscpCustomizeModuleExample.createCriteria().andModuleNameEqualTo(moduleName);
        List<CscpCustomizeModule> cscpCustomizeModule = cscpCustomizeModuleService.findByExample(cscpCustomizeModuleExample).getData();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeModule));
    }

}
