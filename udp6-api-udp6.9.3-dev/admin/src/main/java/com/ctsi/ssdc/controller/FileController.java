package com.ctsi.ssdc.controller;

import com.ctsi.ssdc.config.MinioProperty;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/11/2 17:29  by xx
 */
@Slf4j
@RestController
@RequestMapping("/api/system")
public class FileController {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperty minioProperty;

    /**
     * Description:
     * Copyright (c) CSII.
     * All Rights Reserved.
     * 2022/11/3 14:51  by xx
     */
    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("file")MultipartFile file) throws Exception{
        if (!makeBucket(minioProperty.getBucketName())) {
            log.debug("存在当前存储桶");
        }else {
            log.debug("创建当前存储桶");
        }
        String fileName;
        byte[] byteArr = file.getBytes();
        String originalFileName = file.getOriginalFilename();
        fileName = (int)(System.currentTimeMillis())+originalFileName;
        InputStream inputStream = new ByteArrayInputStream(byteArr);
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .contentType(file.getContentType()) //文件类型
                .stream(inputStream, inputStream.available(), -1) //文件流
                .bucket(minioProperty.getBucketName()) //存储空间
                .object(fileName) //文件
                .build();
        minioClient.putObject(objectArgs);
        return minioProperty.getEndpoint()+"/"+minioProperty.getBucketName()+"/"+fileName;
    }

    private boolean makeBucket(String bucketName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return true;
        } else {
            return false;
        }
    }
    private boolean bucketExists(String bucketName) throws Exception {
        boolean flag = false;
        flag = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (flag) {
            return true;
        }
        return false;
    }

}
