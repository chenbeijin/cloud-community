package com.sh303.sms.config;

import com.sh303.common.cache.Cache;
import com.sh303.common.cache.RedisCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * redis配置到bean 中
 */

@Configuration
public class RedisConfig {

    /**
     * 实例化redis
     * @param redisTemplate
     * @return
     */
    @Bean
    public Cache cache(StringRedisTemplate redisTemplate) {
        return new RedisCache(redisTemplate);
    }

}
