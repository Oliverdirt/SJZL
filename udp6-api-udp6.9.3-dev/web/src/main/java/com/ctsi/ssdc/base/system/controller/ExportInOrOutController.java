package com.ctsi.ssdc.base.system.controller;

import com.ctsi.ssdc.base.system.service.ExportInOrOutService;
import com.ctsi.ssdc.model.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author Len
 * @Date 2023/7/14 9:25
 */
@RestController
@RequestMapping("/api/export")
public class ExportInOrOutController {

    @Autowired
    private ExportInOrOutService exportInOrOutService;

    /**
     * 用户信息导入模板下载
     * @param response
     */
    @PostMapping("/userTemplateDownload")
    public void  userTemplateDownload(HttpServletResponse response) {
        exportInOrOutService.userTemplateDownload(response);
    }

    /**
     * jues信息导入模板下载
     * @param response
     */
    @PostMapping("/roleTemplateDownload")
    public void  roleTemplateDownload(HttpServletResponse response) {
        exportInOrOutService.roleTemplateDownload(response);
    }

    /**
     * 导入用户信息
     * @param file
     * @return
     */
    @PostMapping("/uploadUserInfo")
    public R<String> uploadUserInfo(@RequestParam("file") MultipartFile file) {
        return exportInOrOutService.uploadUserInfo(file);
    }

    /**
     * 导入角色信息
     * @param file
     * @return
     */
    @PostMapping("/uploadRole")
    public R<String> uploadRole(@RequestParam("file") MultipartFile file) {
        return exportInOrOutService.uploadRole(file);
    }




}
