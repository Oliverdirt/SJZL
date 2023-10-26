package com.ctsi.ssdc.sysconfig.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.poi.excel.util.ExcelUtil;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.sysconfig.constants.SysconfigConstant;
import com.ctsi.ssdc.sysconfig.domain.CscpSysConfig;
import com.ctsi.ssdc.sysconfig.domain.CscpSysConfigExample;
import com.ctsi.ssdc.sysconfig.service.CscpSysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/**
 * REST controller for managing CscpSysConfig.
 *
 * @author hx
 * @date 2022-08-24 14:40:57
 *
 */

@Api(value = "/api",tags = {"系统参数配置API"})
@RestController
@RequestMapping("/api")
public class CscpSysConfigController {


    private final Logger log = LoggerFactory.getLogger(CscpSysConfigController.class);

    private static final String ENTITY_NAME = "cscpSysConfig";

    private final CscpSysConfigService cscpSysConfigService;

    public CscpSysConfigController(CscpSysConfigService cscpSysConfigService) {
        this.cscpSysConfigService = cscpSysConfigService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * POST  /cscpSysConfigs : Create a new cscpSysConfig.
     *
     * @param cscpSysConfig the cscpSysConfig to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cscpSysConfig, or with status 400 (Bad Request) if the cscpSysConfig has already an ${primaryKeyParamNameList}
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "body",dataType = "CscpSysConfig",name = "cscpSysConfig",value = "the cscpSysConfig to create")
    })
    @ApiOperation(value = "POST  /cscpSysConfigs : create a new cscpSysConfig.",notes = "POST  /cscpSysConfigs : create a new cscpSysConfig.",httpMethod = "POST")
    @PostMapping("/cscpSysConfigs")
    @ComponentCallAnno(component = SysconfigConstant.SYSCONFIG,method = "createCscpSysConfig")
    public AjaxResult createCscpSysConfig(@RequestBody @Valid CscpSysConfig cscpSysConfig) throws URISyntaxException {

        log.debug("REST request to save CscpSysConfig : {}", cscpSysConfig);
        // 添加创建人、创建时间
        cscpSysConfig.setCreateBy(SecurityHxUtils.getCurrentUserId());
        cscpSysConfig.setCreateTime(ZonedDateTime.now());
        AjaxResult x = uniquenessCheck(cscpSysConfig,false);
        if (x != null) {
            return x;
        }
//        //根据ConfigName唯一性校验
//        CscpSysConfigExample configExample = new CscpSysConfigExample();
//        configExample.createCriteria().andConfigNameEqualTo(cscpSysConfig.getConfigName());
//        List<CscpSysConfig> data = cscpSysConfigService.findByExample(configExample).getData();
//        if(null != data && data.size()>0){
//            return AjaxResult.error("参数名必须唯一");
//        }
//        // ConfigKey唯一性校验
//        CscpSysConfig config = cscpSysConfigService.getCscpSysConfigByConfigKey(cscpSysConfig.getConfigKey());
//        if(null != config){
//            return AjaxResult.error("参数key必须唯一");
//        }

        CscpSysConfig result = cscpSysConfigService.insert(cscpSysConfig);
        return AjaxResult.success("新增参数成功",result.getConfigId());
    }
    /**
     * PUT  /cscpSysConfigs : Updates an existing cscpSysConfig.
     *
     * @param cscpSysConfig the cscpSysConfig to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cscpSysConfig,
     * or with status 400 (Bad Request) if the cscpSysConfig is not valid,
     * or with status 500 (Internal Server Error) if the cscpSysConfig couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body",dataType = "CscpSysConfig",name = "cscpSysConfig",value = "the cscpSysConfig to update")
    })
    @ApiOperation(value = "PUT  /cscpSysConfigs : updates an existing cscpSysConfig.",notes = "PUT  /cscpSysConfigs : updates an existing cscpSysConfig.",httpMethod = "PUT")
    @PutMapping("/cscpSysConfigs")
    @ComponentCallAnno(component = SysconfigConstant.SYSCONFIG,method = "updateCscpSysConfig")
    public AjaxResult updateCscpSysConfig(@RequestBody @Valid CscpSysConfig cscpSysConfig) throws URISyntaxException {

        log.debug("REST request to update CscpSysConfig : {}", cscpSysConfig);

        if (cscpSysConfig.getConfigId() == null) {
            return createCscpSysConfig(cscpSysConfig);
        }
        AjaxResult x = uniquenessCheck(cscpSysConfig,true);
        if (x != null) {
            return x;
        }

        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        // 添加更新人与更新时间
        cscpSysConfig.setUpdateBy(currentUserId);
        cscpSysConfig.setUpdateTime(ZonedDateTime.now());
        CscpSysConfig result = cscpSysConfigService.update(cscpSysConfig);
        return AjaxResult.success("参数配置更新成功",result.getConfigId());
    }

    private AjaxResult uniquenessCheck( CscpSysConfig cscpSysConfig,boolean isUpdate) {
        //根据ConfigName唯一性校验
        CscpSysConfigExample configExample = new CscpSysConfigExample();
        configExample.createCriteria().andConfigNameEqualTo(cscpSysConfig.getConfigName());
        List<CscpSysConfig> data = cscpSysConfigService.findByExample(configExample).getData();
        if(null != data && data.size()>0 &&(isUpdate?!data.get(0).getConfigId().equals(cscpSysConfig.getConfigId()):true)){
            return AjaxResult.error("参数名必须唯一");
        }
        // ConfigKey唯一性校验
        configExample.createCriteria().andConfigKeyEqualTo(cscpSysConfig.getConfigKey());
        List<CscpSysConfig> data2 = cscpSysConfigService.findByExample(configExample).getData();
//        CscpSysConfig config = cscpSysConfigService.getCscpSysConfigByConfigKey(cscpSysConfig.getConfigKey());

        if(null != data2 && data2.size()>0 && !data2.get(0).getConfigId().equals(cscpSysConfig.getConfigId())){
            return AjaxResult.error("参数key必须唯一");
        }
        return null;
    }

    /**
     * @param configKey the id of the cscpSysConfig to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cscpSysConfig, or with status 404 (Not Found)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "String",name = "configKey",value = "根据系统参数配置key获取系统参数配置详情")
    })
    @ApiOperation(value = "根据系统参数配置key获取系统参数配置详情",httpMethod = "GET")
    @GetMapping("/getCscpSysConfigByConfigKey/{configKey}")
    public AjaxResult getCscpSysConfigByConfigKey(@PathVariable String configKey) {
        log.debug("REST request to get CscpSysConfig : {}", configKey);
        CscpSysConfig cscpSysConfig = cscpSysConfigService.getCscpSysConfigByConfigKey(configKey);
        return AjaxResult.success(cscpSysConfig);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "configId",value = "the configId of the cscpSysConfig to retrieve")
    })
    @ApiOperation(value = "GET  /cscpSysConfigs/configId : get the configId cscpSysConfig.",notes = "GET  /cscpSysConfigs/configId : get the configId cscpSysConfig.",httpMethod = "GET")
    @GetMapping("/cscpSysConfigs/{configId}")
    @ComponentCallAnno(component = SysconfigConstant.SYSCONFIG,method = "getCscpSysConfig")
    public AjaxResult getCscpSysConfig(@PathVariable Long configId) {
        log.debug("REST request to get CscpSysConfig : {}", configId);
        CscpSysConfig cscpSysConfig = cscpSysConfigService.findOne(configId);
        return AjaxResult.success(cscpSysConfig);
    }


    /**
     * GET  /cscpSysConfigs : get the cscpSysConfigs .
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cscpSysConfigs in body
     */
    @ApiOperation(value = "GET  /cscpSysConfigs ")
    @GetMapping("/cscpSysConfigs")
    @ComponentCallAnno(component = SysconfigConstant.SYSCONFIG,method = "getCscpSysConfigsList")
    public AjaxResult getCscpSysConfigsList(CscpSysConfigExample cscpSysConfigExample,Pageable pageable) {
        log.debug("REST request to get CscpSysConfigsList");
        return AjaxResult.success("数据查询成功",cscpSysConfigService.findByExample(cscpSysConfigExample,pageable));
    }

    /**
     * DELETE  /cscpSysConfigs/:configId : delete the "configId" cscpSysConfig.
     *
     * @param configId the id of the cscpSysConfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "configId",value = "the configId of the cscpSysConfig to delete")
    })
    @ApiOperation(value = "DELETE  /cscpSysConfigs/configId : delete the configId cscpSysConfig.",notes = "DELETE  /cscpSysConfigs/configId : delete the configId cscpSysConfig.",httpMethod = "DELETE")
    @DeleteMapping("/cscpSysConfigs/{configId}")
    @ComponentCallAnno(component = SysconfigConstant.SYSCONFIG,method = "deleteCscpSysConfig")
    public AjaxResult deleteCscpSysConfig(@PathVariable Long configId) {
        log.debug("REST request to delete CscpSysConfig : {}", configId);
        cscpSysConfigService.deleteById(configId);
        return AjaxResult.success("数据删除成功",configId);
    }


    /**
     * GET  /cscpSysConfigs/:configId : get the "configId" cscpSysConfig.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the cscpSysConfig, or with status 404 (Not Found)
     */
    @ApiOperation(value = "POST  /cscpSysConfigs/export : export the cscpSysConfig to excel",notes = "DELETE  /cscpSysConfigs/configId : delete the configId cscpSysConfig.",httpMethod = "DELETE")
    @PostMapping("/cscpSysConfigs/export")
    @ComponentCallAnno(component = SysconfigConstant.SYSCONFIG,method = "export")
    public AjaxResult export() {

        log.debug("REST request to export CscpSysConfig");

        PageResult<CscpSysConfig> result = cscpSysConfigService.findAll();
        List<CscpSysConfig> list = result.getData();
        ExcelUtil<CscpSysConfig> util = new ExcelUtil<CscpSysConfig>(CscpSysConfig.class);
        return AjaxResult.success("数据导出成功",util.exportExcel(list, "cscpSysConfig"));
    }


    /**
    * DELETE  /configIds : delete the cscpSysConfig.", notes = "DELETE  /configIds : delete the configIds.", httpMethod = "DELETE"
    *
    * @return
    */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long[]", name = "configIds", value = "the configIds of the cscpSysConfig to delete")
    })
    @ApiOperation(value = "DELETE  /configIds : delete the cscpSysConfig.", notes = "DELETE  /configIds : delete the configIds.", httpMethod = "DELETE")
    @DeleteMapping("/cscpSysConfigs/delAll")
    @ComponentCallAnno(component = SysconfigConstant.SYSCONFIG,method = "deleteCscpSysConfig")
    public AjaxResult deleteCscpSysConfig(Long[] configIds) {
        log.debug("REST request to delete configIds");
        cscpSysConfigService.deleteByIds(configIds);
        return AjaxResult.success("数据删除成功",configIds);
    }

}
