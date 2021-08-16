package com.sh303.common.cache;

import java.util.Set;

/**
 * 缓存接口
 */

public interface Cache {

    /**
     * 列出所有的key
     */
    Set<String> getKeys();

    /**
     * 列出单个key中的key
     */
    Set<String> getKeys(String pattern);

    /**
     * 检查给定key是否存在
     */
    Boolean exists(String key);

    /**
     * 移除给定的一个或多个key。如果key不存在，则忽略该命令。
     */
    void del(String key);

    /**
     * 简单的字符串设置
     * @param key
     * @param value
     */
    void set(String key, Object value);

    /**
     * 设置 缓存值 和 缓存时间
     * @param key    缓存key
     * @param value  缓存值
     * @param expire 缓存时间
     */
    void set(String key, Object value, Integer expire);

    /**
     * 返回key所关联的字符值
     */
    Object get(String key);

    /**
     * key seconds 为给定key设置生存时间。当key过期时，它会被自动删除。
     * @param key
     * @param expire
     */
    void expire(String key, int expire);

    /**
     * 如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值之后。
     */
    void append(String key, String value);

    /**
     * 获取旧值返回新值，不存在返回nil
     */
    Object getAndSet(String key, String newValue);

    /**
     * 分布式锁
     */
    Boolean setIfAbsent(String key, String value);

    /**
     * 计数器
     */
    Long increment(String key, Long delta);

}
