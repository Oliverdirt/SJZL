package com.ctsi.ssdc.customize.web;

import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.customize.service.CscpCustomizePageTemplateService;
import com.ctsi.ssdc.customize.service.CscpCustomizeVpageService;
import com.ctsi.ssdc.gen.domain.TemplateEntity;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/lowcode/customize/page/template")
@Api(value = "/api/lowcode/customize/template", tags = {"cscp-customize-page-template-controller"})
public class CscpCustomizePageTemplateController {
    private final Logger log = LoggerFactory.getLogger(CscpCustomizePageTemplateController.class);
    @Autowired
    private CscpCustomizePageTemplateService cscpCustomizePageTemplateService;
    @Autowired
    private CscpCustomizeVpageService cscpCustomizeVpageService;

    /**
     * 新增
     */
    @PostMapping("/add/{pageId}")
    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "addTemplate")
    @Log(message = "新增")
    public AjaxResult addTemplate(@PathVariable("pageId") Long pageId, @RequestBody Map<String, Object> addMap) {
        return toAjax(cscpCustomizePageTemplateService.addTemplate(pageId, addMap));
    }

    /**
     * 修改
     */
    @PutMapping("/update/{pageId}")
    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "updateTemplate")
    @Log(message = "修改")
    public AjaxResult updateTemplate(@PathVariable("pageId") Long pageId, @RequestBody Map<String, Object> updateMap) {
        return toAjax(cscpCustomizePageTemplateService.updateTemplate(pageId, updateMap));
    }

    /**
     * 删除
     */
    @DeleteMapping("/delByPk/{pageId}")
//    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "delTemplate")
    @Log(message = "删除")
    public AjaxResult delTemplate(@PathVariable("pageId") Long pageId, @RequestParam Map<String, Object> delMap) {
        return toAjax(cscpCustomizePageTemplateService.delTemplate(pageId, delMap));
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delByPks/{pageId}")
    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "batchDelTemplate")
    @Log(message = "批量删除")
    public AjaxResult batchDelTemplate(@PathVariable("pageId") Long pageId, @RequestParam Map<String, Object> delMap) {
        return toAjax(cscpCustomizePageTemplateService.batchDelTemplate(pageId, delMap));
    }

    /**
     * 根据主键id查询
     */
    @GetMapping("/queryByPk/{pageId}")
    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "queryTemplate")
    @Log(message = "根据主键id查询")
    public AjaxResult queryTemplate(@PathVariable("pageId") Long pageId, @RequestParam Map<String, Object> queryMap) {
        Map map = cscpCustomizePageTemplateService.queryTemplate(pageId, queryMap);
        return AjaxResult.success(map);
    }

    /**
     * 查询列表
     */
    @GetMapping("/query/{pageId}")
//    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "queryTemplatePage")
    @Log(message = "查询列表")
    public PageResult<Map> queryTemplatePage(@PathVariable("pageId") Long pageId, TemplateEntity templateEntity, Pageable pageable) {
        return cscpCustomizePageTemplateService.queryTemplatePageBatch(pageId, templateEntity, pageable);
    }

    /**
     * 根据主键id查询
     */
    @GetMapping("/queryByInstanceId/{pageId}")
//    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "queryTemplate")
    @Log(message = "根据主键id查询")
    public AjaxResult queryByInstanceId(@PathVariable("pageId") Long pageId, @RequestParam Map<String, Object> queryMap) {
        Map map = cscpCustomizePageTemplateService.queryByInstanceId(pageId, queryMap);
        return AjaxResult.success(map);
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


    @PostMapping("/vpage/import/{pageId}")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "importTemplate")
    @ApiOperation("导入excel")
    public AjaxResult importTemplte(@RequestParam(value = "file") MultipartFile file, Long pageId,String queryStr) throws Exception {
        return cscpCustomizePageTemplateService.taskUploadExcel(file,pageId,queryStr);
    }

    @PostMapping("/cscpCustomizeVpages/exportExcel/{pageId}")
    @PreAuthorize("@cscpCheckPermisionService.checkPermisionByUrl()")
    @ComponentCallAnno(component = HxComponentConstant.LOWCODE, method = "exportTemplate")
    @ApiOperation("导出excel")
    public void export(@PathVariable("pageId") Long pageId, @RequestBody Map<String, Object> addMap, HttpServletResponse response) throws IOException {
        cscpCustomizeVpageService.export(pageId, addMap, response);
    }
}
