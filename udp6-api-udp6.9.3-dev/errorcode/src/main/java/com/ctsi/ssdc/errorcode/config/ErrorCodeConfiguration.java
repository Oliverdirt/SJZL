package com.ctsi.ssdc.errorcode.config;


import com.ctsi.ssdc.errorcode.core.generator.ErrorCodeAutoGenerator;
import com.ctsi.ssdc.errorcode.core.generator.ErrorCodeAutoGeneratorImpl;
import com.ctsi.ssdc.errorcode.core.loader.ErrorCodeLoader;
import com.ctsi.ssdc.errorcode.core.loader.ErrorCodeLoaderImpl;
import com.ctsi.ssdc.errorcode.core.service.ErrorCodeFrameworkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 错误码配置类
 * @author hx
 */
@Configuration
@ConditionalOnProperty(prefix = "ctsi.error-code", value = "enable", matchIfMissing = true) // 允许使用 error-code.enable=false 禁用访问日志
@EnableConfigurationProperties(ErrorCodeProperties.class)
@EnableScheduling // 开启调度任务的功能，因为 ErrorCodeRemoteLoader 通过定时刷新错误码
public class ErrorCodeConfiguration {

    @Bean
    public ErrorCodeAutoGenerator errorCodeAutoGenerator(@Value("${spring.application.name}") String applicationName,
                                                         ErrorCodeProperties errorCodeProperties,
                                                         ErrorCodeFrameworkService errorCodeFrameworkService) {
        return new ErrorCodeAutoGeneratorImpl(applicationName, errorCodeProperties.getConstantsClassList(),
                errorCodeFrameworkService);
    }

    @Bean
    public ErrorCodeLoader errorCodeLoader(@Value("${spring.application.name}") String applicationName,
                                           ErrorCodeFrameworkService errorCodeFrameworkService) {
        return new ErrorCodeLoaderImpl(applicationName, errorCodeFrameworkService);
    }

}
