package com.sh303.sms.store;

import com.sh303.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * redis 验证储存
 */

@Component
public class RedisVerificationStore implements VerificationStore {

    @Autowired
    public Cache cache;

    /**
     * 设置 缓存值 和 缓存时间
     * @param key    缓存key
     * @param value  缓存值
     * @param expire 缓存时间
     */
    @Override
    public void set(String key, String value, Integer expire) {
        cache.set(key, value, expire);
    }

    /**
     * 返回key所关联的字符值
     */
    @Override
    public String get(String key) {
        return (String) cache.get(key);
    }
}
