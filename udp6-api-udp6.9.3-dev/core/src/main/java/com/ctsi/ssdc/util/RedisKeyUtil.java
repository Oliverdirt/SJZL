//package com.ctsi.ssdc.util;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///**
// * redisKey设计
// * @author fuxiang
// */
//@Component
//public class RedisKeyUtil {
//
//    @Value("${spring.application.name}")
//    private String appName;
//
//    /**
//     * redis的key
//     * 形式为：
//     * 应用名称:表名:主键名:主键值:列名
//     *
//     * @param tableName 表名
//     * @param majorKey 主键名
//     * @param majorKeyValue 主键值
//     * @param column 列名
//     * @return
//     */
//    public String getKeyWithColumn(String tableName,String majorKey,String majorKeyValue,String column){
//        StringBuffer buffer = new StringBuffer();
//        buffer.append(appName).append(":");
//        buffer.append(tableName).append(":");
//        buffer.append(majorKey).append(":");
//        buffer.append(majorKeyValue).append(":");
//        buffer.append(column);
//        return buffer.toString();
//    }
//
//    /**
//     * redis的key
//     * 形式为：
//     * 应用名称:表名:主键名:主键值
//     *
//     * @param tableName 表名
//     * @param majorKey 主键名
//     * @param majorKeyValue 主键值
//     * @return
//     */
//    public String getKey(String tableName,String majorKey,String majorKeyValue){
//        StringBuffer buffer = new StringBuffer();
//        buffer.append(appName).append(":");
//        buffer.append(tableName).append(":");
//        buffer.append(majorKey).append(":");
//        buffer.append(majorKeyValue).append(":");
//        return buffer.toString();
//    }
//
//    /**
//     * redis的key
//     * 形式为：
//     * 应用名称:功能名称:主键值
//     *
//     * @param bizName 功能名称
//     * @param majorKeyValue 主键值
//     * @return
//     */
//    public String getBizKey(String bizName,String majorKeyValue){
//        StringBuffer buffer = new StringBuffer();
//        buffer.append(appName).append(":");
//        buffer.append(bizName).append(":");
//        buffer.append(majorKeyValue).append(":");
//        return buffer.toString();
//    }
//}