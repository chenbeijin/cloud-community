package com.sh303.common.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @program: cloud-community
 * @description: redis缓存
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public class RedisCache implements Cache {

    private RedisTemplate redisTemplate;

    /**
     * create by: Chen Bei Jin
     * description: redis缓存模板
     * create time: 2021/8/19 8:44
     * @param redisTemplate
     */
    public RedisCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * create by: Chen Bei Jin
     * description: 列出所有的key
     * create time: 2021/8/19 8:44
     */
    @Override
    public Set<String> getKeys() {
        return redisTemplate.keys("*");
    }

    /**
     * create by: Chen Bei Jin
     * description: 列出单个key中的key
     * create time: 2021/8/19 8:44
     */
    @Override
    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * create by: Chen Bei Jin
     * description: 检查给定key是否存在
     * create time: 2021/8/19 8:44
     */
    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * create by: Chen Bei Jin
     * description: 移除给定的一个或多个key。如果key不存在，则忽略该命令。
     * create time: 2021/8/19 8:44
     */
    @Override
    public void del(String key) {
        redisTemplate.delete(key);
    }

    /**
     * create by: Chen Bei Jin
     * description: 简单的字符串设置
     * create time: 2021/8/19 8:44
     * @param key
     * @param value
     */
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * create by: Chen Bei Jin
     * description: 设置 缓存值 和 缓存时间
     * create time: 2021/8/19 8:45
     * @param key    缓存key
     * @param value  缓存值
     * @param expire 缓存时间
     */
    @Override
    public void set(String key, Object value, Integer expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * create by: Chen Bei Jin
     * description: 返回key所关联的字符值
     * create time: 2021/8/19 8:45
     */
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * create by: Chen Bei Jin
     * description: key seconds 为给定key设置生存时间。当key过期时，它会被自动删除。
     * create time: 2021/8/19 8:45
     * @param key
     * @param expire
     */
    @Override
    public void expire(String key, int expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * create by: Chen Bei Jin
     * description: 如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值之后。
     * create time: 2021/8/19 8:45
     */
    @Override
    public void append(String key, String value) {
        redisTemplate.opsForValue().append(key, value);
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取旧值返回新值，不存在返回nil
     * create time: 2021/8/19 8:46
     */
    @Override
    public String getAndSet(String key, String newValue) {
        return (String) redisTemplate.opsForValue().getAndSet(key, newValue);
    }

    /**
     * create by: Chen Bei Jin
     * description: 分布式锁
     * create time: 2021/8/19 8:46
     */
    @Override
    public Boolean setIfAbsent(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * create by: Chen Bei Jin
     * description: 计数器
     * create time: 2021/8/19 8:46
     */
    @Override
    public Long increment(String key, Long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * create by: Chen Bei Jin
     * description: 将指定的IP计入UV
     * create time: 2021/8/19 8:52
     */
    @Override
    public Long addUV(String key, String value) {
        return redisTemplate.opsForHyperLogLog().add(key, value);
    }

    /**
     * create by: Chen Bei Jin
     * description: 合并UV数据
     * create time: 2021/8/19 8:52
     */
    @Override
    public Long unionUV(String key, List<String> value) {
        return redisTemplate.opsForHyperLogLog().union(key, value.toArray());
    }

    /**
     * create by: Chen Bei Jin
     * description: 统计数量
     * create time: 2021/8/19 8:52
     */
    @Override
    public Long sizeUV(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

    /**
     * create by: Chen Bei Jin
     * description: 设置用户计入DAU
     * create time: 2021/8/19 8:52
     */
    @Override
    public Boolean setBit(String key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取缓存模板
     * create time: 2021/8/19 9:01
     */
    @Override
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * create by: Chen Bei Jin
     * description: 无序列表添加数据
     * create time: 2021/8/20 10:20
     */
    @Override
    public Long addSet(String key, Object value) {
        return redisTemplate.opsForSet().add(key, value);
    }
}
