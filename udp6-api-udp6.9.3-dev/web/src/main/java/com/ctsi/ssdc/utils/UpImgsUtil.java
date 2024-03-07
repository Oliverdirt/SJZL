package com.ctsi.ssdc.utils;

import com.ctsi.ssdc.base.system.domain.FileInfo;
import com.ctsi.ssdc.base.system.mapper.FileInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * 上传图片
 */
@Slf4j
@Component
public class UpImgsUtil {

    @Value("${file.imgPath}")
    public String prefix;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    /**
     * 上传图片 -- 本地
     *
     * @param file
     * @return
     * @throws Exception
     */
    public FileInfo uploadImg(MultipartFile file) throws Exception {
        // 文件路径
        String path = null;

        //判断操作系统
        String returnUrl = "";
        // 判断上传的文件是否为空
        if (file != null) {
            // 文件类型
            String type = null;
            // 文件原名称
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) {
                return null;
            }
            log.info("上传的文件原名称:" + fileName);

            FileInfo entity = new FileInfo();
            entity.setCreateTime(new Date());
            entity.setName(fileName);
            // 判断文件类型
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
            // 判断文件类型是否为空
            if (type != null) {

                entity.setSize(file.getSize() + "");
                entity.setSuffix(type);
                if ("JPEG".equals(type.toUpperCase()) || "GIF".equals(type.toUpperCase())
                        || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase())) {

                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    fileName = uuid + "." + type;
                    log.info("文件名称：" + fileName);

                    // 添加日期
                    path = prefix + "/";
                    File f = new File(path);
                    if (!f.exists()) {
                        f.mkdirs();
                    }

                    String pathName = path + fileName;

                    log.info("存放图片文件的路径:" + pathName);

                    // 转存文件到指定的路径
                    file.transferTo(new File(pathName));

                    log.info("文件成功上传到指定目录下");
                    returnUrl = "/static" + "/" + fileName;

                    entity.setFileUrl(returnUrl);

                    fileInfoMapper.insert(entity);

                    return entity;
                }
            }
        }
        return null;
    }
}
