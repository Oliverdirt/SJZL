package com.ctsi.ssdc.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 接口重复提交
 * <p>
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-01-16 20:43
 **/
@Component
public class RepeatSubmitCache {

    @Value("${ctsi.repeat-submit-lock:1}")
    private int repeatSubmitLock;


    /*
     * 缓存,适用于单节点部署
     */
    private LoadingCache<String, String> cache;

    @PostConstruct
    public void initCache(){
        // CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
        // 在缓存不存在时通过CacheLoader的实现自动加载缓存
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(repeatSubmitLock, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        // TODO Auto-generated method stub
                        return null;
                    }

                });
    }


    public boolean put(String key, String value) {
        try {
            cache.put(key, value);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public String get(String key) {
        String str = null;
        try {
            str = cache.get(key);
        } catch (Exception e) {
        }
        return str;
    }

    public boolean delete(String key) {
        try {
            cache.invalidate(key);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

}
