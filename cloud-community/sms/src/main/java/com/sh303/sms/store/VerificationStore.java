package com.sh303.sms.store;

/**
 * 验证信息存储 key value
 */

public interface VerificationStore {

    /**
     * 设置 缓存值 和 缓存时间
     * @param key    缓存key
     * @param value  缓存值
     * @param expire 缓存时间
     */
    void set(String key, String value, Integer expire);

    /**
     * 返回key所关联的字符值
     */
    String get(String key);

}
