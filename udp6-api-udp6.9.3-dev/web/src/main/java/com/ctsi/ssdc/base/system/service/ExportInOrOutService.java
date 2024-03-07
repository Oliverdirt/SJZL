package com.ctsi.ssdc.base.system.service;

import com.ctsi.ssdc.model.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author Len
 * @Date 2023/7/14 9:26
 */
public interface ExportInOrOutService {

    /**
     * 用户信息导入模板下载
     *
     * @param response
     */
    void userTemplateDownload(HttpServletResponse response);
    /**
     * 角色信息导入模板下载
     *
     * @param response
     */
    void roleTemplateDownload(HttpServletResponse response);

    /**
     * 导入用户信息
     * @param file
     * @return
     */
    R<String> uploadUserInfo(MultipartFile file);

    /**
     * 导入角色信息
     * @param multipartFile
     * @return
     */
    R<String> uploadRole(MultipartFile multipartFile);

}
