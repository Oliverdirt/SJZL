package com.ctsi.ssdc.config;
import com.ctsi.ssdc.handle.JasyptPropertyValueHandler;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 数据库密码加解密
 * <p>
 * The code change the world !!!
 *
 * @author guochui
 * @create 2021-10-15 11:14
 **/
@Configuration
public class JasyptPropertyValueConfig {

    @Bean
    public PropertyOverrideConfigurer jasyptPropertyOverrideConfigurer() {
        return new JasyptPropertyValueHandler();
    }
}

