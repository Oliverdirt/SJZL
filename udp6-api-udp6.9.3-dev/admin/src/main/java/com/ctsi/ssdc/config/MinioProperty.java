package com.ctsi.ssdc.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Description: minio属性配置
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/11/4 10:18  by xx
 */
@ConfigurationProperties(prefix = "minio")
@Configuration
@Data
public class MinioProperty {
    @Value("accessKey")
    private String accessKey;
    @Value("secretKey")
    private String secretKey;
    @Value("endpoint")
    private String endpoint;
    @Value("bucketName")
    private String bucketName;
}
