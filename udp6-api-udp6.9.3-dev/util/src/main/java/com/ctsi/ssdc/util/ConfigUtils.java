package com.ctsi.ssdc.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.DefaultConfigurationBuilder;

/**
 * 读取配置文件工具类
 * @author ctsi
 *
 */
public class ConfigUtils {

    private static Configuration config = null;
    
    static {
        try {
            DefaultConfigurationBuilder configurationBuilder = new DefaultConfigurationBuilder("app.config.xml");
            config = configurationBuilder.getConfiguration();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   public static Configuration getConfig() {
		return config;
	}
	public static void setConfig(Configuration config) {
		ConfigUtils.config = config;
	}
/**
    * 读取config的Int值 
    * @param key 节点名
    * @return Int值
    */
    

    public static int getIntValue(final String key) {
        int reInt = 0;
        try {
            reInt = config.getInt(key);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return reInt;
    }
    /**
     * 读取config的long值 
     * @param key 节点名
     * @return long值
     */
    

    public static long getLongValue(final String key) {
        long reLong = 0;
        try {
            reLong = config.getLong(key);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }

        return reLong;
    }
    /**
     * 读取config的Double值
     * @param key 节点名
     * @return Double值
     */
    

    public static double getDoubleValue(final String key) {
        double reDouble = 0;
        try {
            reDouble = config.getDouble(key);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }

        return reDouble;
    }
    /**
     * 读取config的String值
     * @param key 节点名
     * @return String值
     */
    

    public static String getStringValue(String key) {
        String str = "";
        try {
            str = config.getString(key);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return str;
    }
    /**
     * 读取config的boolean值
     * @param key 节点名
     * @return boolean值
     */
    

    public static boolean getBooleanValue(String key) {
        boolean flag = false;
        try {
            flag = config.getBoolean(key);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }

        return flag;
    }

}
