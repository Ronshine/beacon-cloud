package com.chl.gateway.config;

import cn.hippo4j.core.executor.DynamicThreadPool;
import cn.hippo4j.core.executor.support.ThreadPoolBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor cmppSubmitPool() {
        String threadPoolId = "cmpp-submit";
        return ThreadPoolBuilder.builder()
                // 指定线程名称的前缀
                .threadFactory(threadPoolId)
                // 线程池在Hippo4j中的唯一标识
                .threadPoolId(threadPoolId)
                // 代表动态线程池
                .dynamicPool()
                .build();
    }

    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor cmppDeliverPool() {
        String threadPoolId = "cmpp-deliver";
        return ThreadPoolBuilder.builder()
                .threadFactory(threadPoolId)
                .threadPoolId(threadPoolId)
                .dynamicPool()
                .build();
    }

}