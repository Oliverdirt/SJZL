package com.ctsi.ssdc.base.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctsi.ssdc.base.system.domain.FileInfo;
import com.ctsi.ssdc.base.system.mapper.FileInfoMapper;
import com.ctsi.ssdc.base.system.service.IFileInfoService;
import com.ctsi.ssdc.utils.UpImgsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxs
 * @since 2023-04-21
 */
@Service
@RequiredArgsConstructor
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements IFileInfoService {

    @Autowired
    private UpImgsUtil upImgsUtil;

    @Override
    public FileInfo uploadImgs(MultipartFile file) {
        try{
            return upImgsUtil.uploadImg(file);
        }catch (Exception e) {
            return null;
        }
    }
}
