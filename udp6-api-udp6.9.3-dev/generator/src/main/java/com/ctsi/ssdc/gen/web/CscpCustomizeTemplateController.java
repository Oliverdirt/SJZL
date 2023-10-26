package com.ctsi.ssdc.gen.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.gen.domain.TemplateEntity;
import com.ctsi.ssdc.gen.service.CscpCustomizeTemplateService;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/lowcode/customize/template")
@Api(value = "/api/lowcode/customize/template", tags = {"cscp-customize-template-controller"})
public class CscpCustomizeTemplateController {
    private final Logger log = LoggerFactory.getLogger(CscpCustomizeTemplateController.class);
    @Autowired
    private CscpCustomizeTemplateService cscpCustomizeTemplateService;


    /**
     * 新增
     */
    @PostMapping("/add/{formId}")
//    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "addTemplate")
    @Log(message = "新增")
    public AjaxResult addTemplate(@PathVariable("formId") Long formId, @RequestBody Map<String, Object> addMap) {
        return toAjax(cscpCustomizeTemplateService.addTemplate(formId, addMap));
    }

    /**
     * 修改
     */
    @PutMapping("/update/{formId}")
//    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "updateTemplate")
    @Log(message = "修改")
    public AjaxResult updateTemplate(@PathVariable("formId") Long formId, @RequestBody Map<String, Object> updateMap) {
        return toAjax(cscpCustomizeTemplateService.updateTemplate(formId, updateMap));
    }

    /**
     * 删除
     */
    @DeleteMapping("/delByPk/{formId}")
//    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "delTemplate")
    @Log(message = "删除")
    public AjaxResult delTemplate(@PathVariable("formId") Long formId, @RequestParam Map<String, Object> delMap) {
        return toAjax(cscpCustomizeTemplateService.delTemplate(formId, delMap));
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delByPks/{formId}")
//    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "batchDelTemplate")
    @Log(message = "批量删除")
    public AjaxResult batchDelTemplate(@PathVariable("formId") Long formId, @RequestParam Map<String, Object> delMap) {
        return toAjax(cscpCustomizeTemplateService.batchDelTemplate(formId, delMap));
    }

    /**
     * 根据主键id查询
     */
    @GetMapping("/queryByPk/{formId}")
//    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "queryTemplate")
    @Log(message = "根据主键id查询")
    public AjaxResult queryTemplate(@PathVariable("formId") Long formId, @RequestParam Map<String, Object> queryMap) {
        Map map = cscpCustomizeTemplateService.queryTemplate(formId, queryMap);
        return AjaxResult.success(map);
    }

    /**
     * 查询列表
     */
    @GetMapping("/query/{formId}")
//    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "queryTemplatePage")
    @Log(message = "查询列表")
    public PageResult<Map> queryTemplatePage(@PathVariable("formId") Long formId, TemplateEntity templateEntity, Pageable pageable) {
        return cscpCustomizeTemplateService.queryTemplatePage(formId, templateEntity, pageable);
    }

    /**
     * 根据主键id查询
     */
    @GetMapping("/queryByInstanceId/{formId}")
    //    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "queryTemplate")
    @Log(message = "根据主键id查询")
    public AjaxResult queryByInstanceId(@PathVariable("formId") Long formId, @RequestParam Map<String, Object> queryMap) {
        Map map = cscpCustomizeTemplateService.queryByInstanceId(formId, queryMap);
        return AjaxResult.success(map);
    }

    @PostMapping("/import")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "importTemplate")
    @ApiOperation("导入excel")
    public AjaxResult importTemplte(@RequestParam(value = "file") MultipartFile file, Long formId) throws Exception {
        return cscpCustomizeTemplateService.taskUploadExcel(file, formId);
    }

    @GetMapping("/export")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "exportTemplate")
    @ApiOperation("导出excel")
    public void export(Long formId, TemplateEntity templateEntity, HttpServletResponse response) throws IOException {
        cscpCustomizeTemplateService.export(formId, templateEntity, response);
    }


    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
