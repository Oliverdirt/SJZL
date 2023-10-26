package com.ctsi.ssdc.cache;

import com.ctsi.ssdc.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 基于google guava的实现，缓存验证码信息
 * @author ctsi
 *
 */

@Component
public class RedisLoginCache implements LoginCache {


	@Value("${ctsi.login.lockout-time:3600}")
	private int lockoutTime;

    @Autowired(
            required = false
    )
    private RedisUtil redisUtil;

    @Override
	public boolean put(String key, Integer value) {
		try {
            redisUtil.set(key, value.toString(),(long)lockoutTime);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

    @Override
	public Integer get(String key) {

        Object str = redisUtil.get(key);

        Integer result = null;
        if(str!=null && str instanceof Integer){
            result = (Integer) str;
        }
        if(str!=null && str instanceof String){
            result = Integer.valueOf((String)str) ;
        }

        return result;
	}

    @Override
    public boolean delete(String key) {
        return redisUtil.delete(key)!=null && redisUtil.delete(key)>0;
    }

    @Override
	public Long getExpire(String key){
        long expire = redisUtil.getExpire(key);
        return expire;
    }

}
