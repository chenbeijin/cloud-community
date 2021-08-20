package com.sh303.common.cache;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Set;

/**
 * @program: cloud-community
 * @description: 缓存接口
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public interface Cache {

    /**
     * create by: Chen Bei Jin
     * description: 列出所有的key
     * create time: 2021/8/19 8:41
     */
    Set<String> getKeys();

    /**
     * create by: Chen Bei Jin
     * description: 列出单个key中的key
     * create time: 2021/8/19 8:41
     */
    Set<String> getKeys(String pattern);

    /**
     * create by: Chen Bei Jin
     * description: 检查给定key是否存在
     * create time: 2021/8/19 8:41
     */
    Boolean exists(String key);

    /**
     * create by: Chen Bei Jin
     * description: 移除给定的一个或多个key。如果key不存在，则忽略该命令。
     * create time: 2021/8/19 8:41
     */
    void del(String key);

    /**
     * create by: Chen Bei Jin
     * description: 简单的字符串设置
     * create time: 2021/8/19 8:42
     */
    void set(String key, Object value);

    /**
     * create by: Chen Bei Jin
     * description: 设置 缓存值 和 缓存时间
     * create time: 2021/8/19 8:42
     * @param key    缓存key
     * @param value  缓存值
     * @param expire 缓存时间
     */
    void set(String key, Object value, Integer expire);

    /**
     * create by: Chen Bei Jin
     * description: 返回key所关联的字符值
     * create time: 2021/8/19 8:42
     * @return
     * @params
     */
    Object get(String key);

    /**
     * create by: Chen Bei Jin
     * description: key seconds 为给定key设置生存时间。当key过期时，它会被自动删除。
     * create time: 2021/8/19 8:42
     * @param key
     * @param expire
     */
    void expire(String key, int expire);

    /**
     * create by: Chen Bei Jin
     * description: 如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值之后。
     * create time: 2021/8/19 8:42
     */
    void append(String key, String value);

    /**
     * create by: Chen Bei Jin
     * description: 获取旧值返回新值，不存在返回nil
     * create time: 2021/8/19 8:42
     */
    String getAndSet(String key, String newValue);

    /**
     * create by: Chen Bei Jin
     * description: 分布式锁
     * create time: 2021/8/19 8:43
     */
    Boolean setIfAbsent(String key, String value);

    /**
     * create by: Chen Bei Jin
     * description: 计数器
     * create time: 2021/8/19 8:43
     */
    Long increment(String key, Long delta);

    /**
     * create by: Chen Bei Jin
     * description: 将指定的IP计入UV
     * create time: 2021/8/19 8:52
     */
    Long addUV(String key, String value);

    /**
     * create by: Chen Bei Jin
     * description: 合并UV数据
     * create time: 2021/8/19 8:52
     */
    Long unionUV(String key, List<String> value);

    /**
     * create by: Chen Bei Jin
     * description: 统计数量
     * create time: 2021/8/19 8:52
     */
    Long sizeUV(String key);

    /**
     * create by: Chen Bei Jin
     * description: 设置用户计入DAU
     * create time: 2021/8/19 8:52
     */
    Boolean setBit(String key, long offset, boolean value);

    /**
     * create by: Chen Bei Jin
     * description: 获取缓存模板
     * create time: 2021/8/19 9:01
     */
    RedisTemplate getRedisTemplate();
}
