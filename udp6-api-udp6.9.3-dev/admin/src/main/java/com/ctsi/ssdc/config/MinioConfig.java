package com.ctsi.ssdc.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * 2022/11/3 19:45  by @author xx
 */
@Configuration
public class MinioConfig {
    @Autowired
    private MinioProperty minioProperty;

    @Bean
    public MinioClient buildMinioClient(){
        return MinioClient
                .builder()
                .credentials(minioProperty.getAccessKey(),minioProperty.getSecretKey())
                .endpoint(minioProperty.getEndpoint())
                .build();
    }
}
