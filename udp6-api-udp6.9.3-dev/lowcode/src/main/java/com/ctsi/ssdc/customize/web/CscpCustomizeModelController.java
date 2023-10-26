package com.ctsi.ssdc.customize.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModelType;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModelTypeExample;
import com.ctsi.ssdc.customize.service.CscpCustomizeModelTypeService;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.service.CscpHxFormTableService;
import com.ctsi.ssdc.security.SecurityHxUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.lang.Long;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    import com.ctsi.ssdc.customize.service.CscpCustomizeModelService;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModelExample;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModel;

import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.util.HeaderUtil;
import com.ctsi.ssdc.util.ResponseUtil;

/**
 * @author hx
 * @date 2022-08-29 16:22:46
 * @description ：低代码-数据模型
 */

@Api(value = "/api",tags = {"低代码-数据模型"})
@RestController
@RequestMapping("/api")
public class CscpCustomizeModelController {

    private final Logger log = LoggerFactory.getLogger(CscpCustomizeModelController.class);

    private static final String ENTITY_NAME = "cscpCustomizeModel";

    @Autowired
    private CscpCustomizeModelService cscpCustomizeModelService;
    @Autowired
    private CscpHxFormTableService cscpHxFormTableService;
    @Autowired
    private CscpCustomizeModelTypeService cscpCustomizeModelTypeService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 数据模型基本信息的新增
     */
    @ApiOperation(value = "数据模型基本信息的新增",httpMethod = "POST")
    @PostMapping("/cscpCustomizeModels")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "createCscpCustomizeModel")
    public ResponseEntity<CscpCustomizeModel> createCscpCustomizeModel(@RequestBody  CscpCustomizeModel cscpCustomizeModel) throws URISyntaxException {
        log.debug("REST request to save CscpCustomizeModel : {}", cscpCustomizeModel);
        cscpCustomizeModel.setCreatedBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeModel.setCreateTime(ZonedDateTime.now());
        cscpCustomizeModel.setTenantId(SecurityHxUtils.getCurrentTenantId());
        CscpCustomizeModel result = cscpCustomizeModelService.insert(cscpCustomizeModel);
        return ResponseEntity.created(new URI("/api/cscpCustomizeModels/" + result.getModelId() ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getModelId().toString()))
            .body(result);
    }

    /**
     * 数据模型基本信息的修改
     */
    @ApiOperation(value = "数据模型基本信息的修改",httpMethod = "PUT")
    @PutMapping("/cscpCustomizeModels")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "updateCscpCustomizeModel")
    public ResponseEntity<CscpCustomizeModel> updateCscpCustomizeModel(@RequestBody  CscpCustomizeModel cscpCustomizeModel) throws URISyntaxException {
        log.debug("REST request to update CscpCustomizeModel : {}", cscpCustomizeModel);
        if (cscpCustomizeModel.getModelId() == null) {
            return createCscpCustomizeModel(cscpCustomizeModel);
        }
        cscpCustomizeModel.setUpdateBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeModel.setUpdateTime(ZonedDateTime.now());
        CscpCustomizeModel result = cscpCustomizeModelService.update(cscpCustomizeModel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getModelId().toString()))
            .body(result);
    }

    /**
     * 数据模型查询单条基本信息
     */
    @ApiOperation(value = "数据模型查询单条基本信息",httpMethod = "GET")
    @GetMapping("/cscpCustomizeModels/{modelId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "getCscpCustomizeModel")
    public ResponseEntity<CscpCustomizeModel> getCscpCustomizeModel(@PathVariable Long modelId) {
        log.debug("REST request to get CscpCustomizeModel : {}", modelId);
        CscpCustomizeModel cscpCustomizeModel = cscpCustomizeModelService.findOne(modelId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeModel));
    }

    /**
     * 数据模型基本信息根据条件查询
     */
    @ApiOperation(value = "数据模型基本信息根据条件查询",httpMethod = "GET")
    @GetMapping("/cscpCustomizeModels")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "getCscpCustomizeModelsList")
    public PageResult<CscpCustomizeModel> getCscpCustomizeModelsList(CscpCustomizeModelExample cscpCustomizeModelExample,Pageable pageable) {
        log.debug("REST request to get CscpCustomizeModelsList");
        return cscpCustomizeModelService.findByExample(cscpCustomizeModelExample,pageable);
    }

    /**
     * 数据模型基本信息删除单条数据
     */
    @ApiOperation(value = "数据模型基本信息删除单条数据",httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeModels/{modelId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "deleteCscpCustomizeModel")
    public Map<String, Object> deleteCscpCustomizeModel(@PathVariable Long modelId) {
        log.debug("REST request to delete CscpCustomizeModel : {}", modelId);
        return cscpCustomizeModelService.delOne(modelId);
    }

    /**
     * 批量删除数据模型基本信息
     */
    @ApiOperation(value = "批量删除数据模型基本信息",  httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeModels/delAll")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "deleteAllCscpCustomizeModels")
    public Map<String, Object> deleteAllCscpCustomizeModels(Long[] modelIds) {
        log.debug("REST request to delete moduleId");
        return cscpCustomizeModelService.delAll(modelIds);
    }

    /**
     * 新增数据模型类型
     */
    @ApiOperation(value = "新增数据模型类型",httpMethod = "POST")
    @PostMapping("/cscpCustomizeModelTypes")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "createCscpCustomizeModelType")
    public ResponseEntity<CscpCustomizeModelType> createCscpCustomizeModelType(@RequestBody  CscpCustomizeModelType cscpCustomizeModelType) throws URISyntaxException {
        log.debug("REST request to save CscpCustomizeModelType : {}", cscpCustomizeModelType);
        cscpCustomizeModelType.setCreatedBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeModelType.setCreateTime(ZonedDateTime.now());
        cscpCustomizeModelType.setTenantId(SecurityHxUtils.getCurrentTenantId());
        CscpCustomizeModelType result = cscpCustomizeModelTypeService.insert(cscpCustomizeModelType);
        return ResponseEntity.created(new URI("/api/cscpCustomizeModelTypes/" + result.getModelTypeId() ))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getModelTypeId().toString()))
                .body(result);
    }

    /**
     * 修改数据模型类型
     */
    @ApiOperation(value = "修改数据模型类型",httpMethod = "PUT")
    @PutMapping("/cscpCustomizeModelTypes")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "updateCscpCustomizeModelType")
    public ResponseEntity<CscpCustomizeModelType> updateCscpCustomizeModelType(@RequestBody  CscpCustomizeModelType cscpCustomizeModelType) throws URISyntaxException {
        log.debug("REST request to update CscpCustomizeModelType : {}", cscpCustomizeModelType);
        if (cscpCustomizeModelType.getModelTypeId() == null) {
            return createCscpCustomizeModelType(cscpCustomizeModelType);
        }
        cscpCustomizeModelType.setUpdateBy(SecurityHxUtils.getCurrentUserId());
        cscpCustomizeModelType.setUpdateTime(ZonedDateTime.now());
        CscpCustomizeModelType result = cscpCustomizeModelTypeService.update(cscpCustomizeModelType);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getModelTypeId().toString()))
                .body(result);
    }

    /**
     * 查询单条数据模型类型
     */
    @ApiOperation(value = "查询单条数据模型类型",httpMethod = "GET")
    @GetMapping("/cscpCustomizeModelTypes/{modelTypeId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "getCscpCustomizeModelType")
    public ResponseEntity<CscpCustomizeModelType> getCscpCustomizeModelType(@PathVariable Long modelTypeId) {
        log.debug("REST request to get CscpCustomizeModelType : {}", modelTypeId);
        CscpCustomizeModelType cscpCustomizeModelType = cscpCustomizeModelTypeService.findOne(modelTypeId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cscpCustomizeModelType));
    }

    /**
     * 根据条件查询数据模型类型
     */
    @ApiOperation(value = "根据条件查询数据模型类型",httpMethod = "GET")
    @GetMapping("/cscpCustomizeModelTypes")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "getCscpCustomizeModelTypesList")
    public PageResult<CscpCustomizeModelType> getCscpCustomizeModelTypesList(CscpCustomizeModelTypeExample cscpCustomizeModelTypeExample, Pageable pageable) {
        log.debug("REST request to get CscpCustomizeModelTypesList");
        return cscpCustomizeModelTypeService.findByExample(cscpCustomizeModelTypeExample,pageable);
    }

    /**
     * 删除数据模型类型
     */
    @ApiOperation(value = "删除数据模型类型",httpMethod = "DELETE")
    @DeleteMapping("/cscpCustomizeModelTypes/{modelTypeId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "deleteCscpCustomizeModelType")
    public ResponseEntity<Void> deleteCscpCustomizeModelType(@PathVariable Long modelTypeId) {
        log.debug("REST request to delete CscpCustomizeModelType : {}", modelTypeId);
        cscpCustomizeModelTypeService.deleteById(modelTypeId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, modelTypeId.toString())).build();
    }
}
