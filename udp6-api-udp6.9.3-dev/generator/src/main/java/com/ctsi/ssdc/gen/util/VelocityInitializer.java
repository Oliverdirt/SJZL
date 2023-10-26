package com.ctsi.ssdc.gen.util;

import com.ctsi.ssdc.gen.constant.GenConstants;
import org.apache.velocity.app.Velocity;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
/**
 * VelocityEngine工厂
 *
 * @author hx
 */
public class VelocityInitializer
{
    /**
     * 初始化vm方法
     */
    public static void initVelocity()
    {
        Properties p = new Properties();
        try
        {
            // 加载classpath目录下的vm文件
            p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//            p.setProperty("file.resource.loader.class", "com.ctsi.ssdc.gen.util.FileResourceLoader");
            // 定义字符集
            p.setProperty(Velocity.INPUT_ENCODING, GenConstants.UTF8);
            p.setProperty(Velocity.OUTPUT_ENCODING, GenConstants.UTF8);
            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(p);

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    /**
     * 初始化自定义vm方法
     */
    public static void initCustomerVelocity()
    {
        Properties p = new Properties();
        try
        {
            // 加载classpath目录下的vm文件
            p.setProperty("file.resource.loader.class", "com.ctsi.ssdc.gen.util.FileResourceLoader");
            // 定义字符集
            p.setProperty(Velocity.INPUT_ENCODING, GenConstants.UTF8);
            p.setProperty(Velocity.OUTPUT_ENCODING, GenConstants.UTF8);
            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(p);

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
