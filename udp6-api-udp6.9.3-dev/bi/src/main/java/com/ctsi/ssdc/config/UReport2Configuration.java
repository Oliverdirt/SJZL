package com.ctsi.ssdc.config;

import com.ctsi.ssdc.console.UreportServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ImportResource({"classpath:ureport-console-context.xml"})
@PropertySource(value = {"classpath:ureport.properties"})
public class UReport2Configuration {

    @Bean
    public ServletRegistrationBean<UreportServlet> registerUreportServlet(){
        return new ServletRegistrationBean<>(new UreportServlet(), "/ureport/*");
    }

    @Bean
    @ConditionalOnMissingBean
    public PropertySourcesPlaceholderConfigurer propertySourceLoader() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(true);
        configurer.setOrder(1);
        return configurer;
    }
}

