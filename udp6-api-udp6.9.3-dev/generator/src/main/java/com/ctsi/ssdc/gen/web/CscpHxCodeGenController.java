package com.ctsi.ssdc.gen.web;

import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.gen.service.CscpHxCodeGenService;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-02-23 10:52
 * @description ：code generator api
 * @version : version1.0.0
 */


@Api(value = "/api/gen",tags = {"cscp-hx-code-gen-controller"})
@RestController
@RequestMapping("/api/gen")
public class CscpHxCodeGenController {


    @Autowired
    private CscpHxCodeGenService cscpHxCodeGenService;

    /**
     * create by: guoyanpei
     * description: generator code and download
     * params: Long tableId
     * return:
     */
    @GetMapping("/download/{tableId}")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "download")
    @Log
    public void download(HttpServletResponse response, @PathVariable("tableId") Long tableId) throws IOException {
        byte[] data = cscpHxCodeGenService.downloadCode(tableId);
        if (data != null){
            genCode(response, data);
        }
    }


    /**
     * create by: guoyanpei
     * description: generator code and preview
     * params: Long tableId
     * return: Map<String, String>
     */
    @GetMapping("/preview/{tableId}")
    @ComponentCallAnno(component = HxComponentConstant.GENERATOR,method = "preview")
    @Log
    public Map<String, String> preview(@PathVariable("tableId") Long tableId) {
        return cscpHxCodeGenService.preview(tableId);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException
    {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"biyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }


}
