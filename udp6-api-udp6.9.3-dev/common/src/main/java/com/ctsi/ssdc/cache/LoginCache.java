package com.ctsi.ssdc.cache;

/**
 * 缓存接口
 */
public interface LoginCache {

    /**
     * 设置key
     * @param key
     * @param value
     * @return
     */
     boolean put(String key, Integer value);


    /**
     * 获取key值
     * @param key
     * @return
     */
     Integer get(String key) ;

    /**
     * 删除key
     * @param key
     * @return
     */
     boolean delete(String key) ;

    /**
     * 获取过期时间
     * @param key
     * @return
     */
     Long getExpire(String key);
}
