package com.ctsi.ssdc.base.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ctsi.ssdc.base.system.domain.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxs
 * @since 2023-04-21
 */
public interface IFileInfoService extends IService<FileInfo> {

    /**
     * 上传图片
     * @param file
     * @return
     */
    FileInfo uploadImgs(MultipartFile file);
}
