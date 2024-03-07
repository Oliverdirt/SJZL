package com.ctsi.ssdc.base.system.controller;


import cn.hutool.core.util.ObjectUtil;
import com.ctsi.ssdc.base.system.domain.FileInfo;
import com.ctsi.ssdc.base.system.service.IFileInfoService;
import com.ctsi.ssdc.model.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传图片
 * @author
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fileInfo")
public class FileInfoController {

    private final IFileInfoService iFileInfoService;

    /**
     * 上传图片
     * @param file
     * @return
     */
    @PostMapping("/uploadImgs")
    public R<FileInfo> uploadImgs(@RequestParam("file") MultipartFile file) {
        FileInfo fileInfo = iFileInfoService.uploadImgs(file);
        return ObjectUtil.isAllEmpty(fileInfo) ? R.failed("上传失败") : R.ok(fileInfo);
    }


}
