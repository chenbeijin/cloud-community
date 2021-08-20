package com.sh303.circle.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.sh303.common.cache.Cache;
import com.sh303.common.cache.RedisCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * @program: cloud-community
 * @description: redis配置到bean 中
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Configuration
public class RedisConfig implements Serializable {

    /**
     * create by: Chen Bei Jin
     * description: 实例化redis
     * create time: 2021/8/16 10:30
     * @param factory
     */
    @Bean
    public Cache cache(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 设置键（key）的序列化采用StringRedisSerializer。
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 设置值（value）的序列化采用jdk redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        // 设置值（value）的序列化采用FastJsonRedisSerializer。
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return new RedisCache(redisTemplate);
    }

}
