package com.ctsi.ssdc.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 环境配置工具类
 * @author ctsi
 *
 */
@Service
public class EnvironmentUtil {
    Environment env;

    @Autowired
    void setEnviroment(Environment environment) {
//        System.out.println("my-config.appName from env: " + env.getProperty("user.all"));
        this.env = environment;
    }

    Environment getEnviroment() {
        return env;
    }
    
    /**
     * 根据属性名获取配置
     * @param name 属性名
     * @return 属性值
     */
    public String getConfigInName(String name) {
        return env.getProperty(name);
    }
}
