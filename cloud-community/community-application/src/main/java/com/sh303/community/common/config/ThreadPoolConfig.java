package com.sh303.community.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @program: cloud-community
 * @description: 配置spring线程池的Scheduler.
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
