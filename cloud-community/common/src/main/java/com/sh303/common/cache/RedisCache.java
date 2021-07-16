package com.sh303.common.cache;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存
 */

public class RedisCache implements Cache {

    private StringRedisTemplate redisTemplate;

    /**
     * redis缓存模板
     * @param redisTemplate
     */
    public RedisCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 列出所有的key
     */
    @Override
    public Set<String> getKeys() {
        return redisTemplate.keys("*");
    }

    /**
     * 列出单个key中的key
     */
    @Override
    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 检查给定key是否存在
     */
    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 移除给定的一个或多个key。如果key不存在，则忽略该命令。
     */
    @Override
    public void del(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 简单的字符串设置
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置 缓存值 和 缓存时间
     * @param key    缓存key
     * @param value  缓存值
     * @param expire 缓存时间
     */
    @Override
    public void set(String key, String value, Integer expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 返回key所关联的字符值
     */
    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * key seconds 为给定key设置生存时间。当key过期时，它会被自动删除。
     * @param key
     * @param expire
     */
    @Override
    public void expire(String key, int expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值之后。
     */
    @Override
    public void append(String key, String value) {
        redisTemplate.opsForValue().append(key, value);
    }

    /**
     * 获取旧值返回新值，不存在返回nil
     */
    @Override
    public String getAndSet(String key, String newValue) {
        return redisTemplate.opsForValue().getAndSet(key, newValue);
    }

    /**
     * 分布式锁
     */
    @Override
    public Boolean setIfAbsent(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 计数器
     */
    @Override
    public Long increment(String key, Long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }
}
