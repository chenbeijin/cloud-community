package com.sh303.circle.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: cloud-community
 * @description: mybatisPlus 扫描 分页 配置
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Configuration
@MapperScan("com.sh303.**.mapper")
public class MybatisPlusConfig {

    /**
     * create by: Chen Bei Jin
     * description: 分页插件，自动识别数据库类型
     * create time: 2021/8/16 10:30
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * create by: Chen Bei Jin
     * description: 启用性能分析插件
     * create time: 2021/8/16 10:30
     */
    @Bean
    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
        return new PerformanceMonitorInterceptor();
    }

}
