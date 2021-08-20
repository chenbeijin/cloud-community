package com.sh303.common.util;

import java.io.Serializable;

/**
 * @program: cloud-community
 * @description: redis 工具类
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public class RedisUtil implements Serializable {

    /**
     * 主数据系统标识
     */
    public static final String KEY_PREFIX = "FP_PAY_PARAM";

    /**
     * 分割字符，默认[:]，使用:可用于rdm分组查看
     */
    private static final String KEY_SPLIT_CHAR = ":";

    /**
     * create by: Chen Bei Jin
     * description: redis的key键规则定义
     * create time: 2021/8/16 14:24
     * @param module 模块名称
     * @param func   方法名称
     * @param args   参数..
     * @return key
     */
    public static String keyBuilder(String module, String func, String... args) {
        return keyBuilder(null, module, func, args);
    }

    /**
     * create by: Chen Bei Jin
     * description: redis的key键规则定义
     * create time: 2021/8/16 14:24
     * @param module 模块名称
     * @param func   方法名称
     * @param objStr 对象.toString()
     * @return key
     */
    public static String keyBuilder(String module, String func, String objStr) {
        return keyBuilder(null, module, func, new String[]{objStr});
    }

    /**
     * create by: Chen Bei Jin
     * description: redis的key键规则定义
     * create time: 2021/8/16 14:23
     * @param prefix 项目前缀
     * @param module 模块名称
     * @param func   方法名称
     * @param objStr 对象.toString()
     * @return key
     */
    public static String keyBuilder(String prefix, String module, String func, String objStr) {
        return keyBuilder(prefix, module, func, new String[]{objStr});
    }

    /**
     * create by: Chen Bei Jin
     * description: redis的key键规则定义
     * create time: 2021/8/16 14:23
     * @param prefix 项目前缀
     * @param module 模块名称
     * @param func   方法名称
     * @param args   参数..
     * @return key
     */
    public static String keyBuilder(String prefix, String module, String func, String... args) {
        // 项目前缀
        if (prefix == null) {
            prefix = KEY_PREFIX;
        }
        StringBuilder key = new StringBuilder(prefix);
        // KEY_SPLIT_CHAR 为分割字符
        key.append(KEY_SPLIT_CHAR).append(module).append(KEY_SPLIT_CHAR).append(func);
        for (String arg : args) {
            key.append(KEY_SPLIT_CHAR).append(arg);
        }
        return key.toString();
    }

    /**
     * create by: Chen Bei Jin
     * description: redis的key键规则定义
     * create time: 2021/8/16 14:23
     * @param redisEnum 枚举对象
     * @param objStr    对象.toString()
     */
    public static String keyBuilder(RedisEnum redisEnum, String objStr) {
        return keyBuilder(redisEnum.getKeyPrefix(), redisEnum.getModule(), redisEnum.getFunc(), objStr);
    }
}
