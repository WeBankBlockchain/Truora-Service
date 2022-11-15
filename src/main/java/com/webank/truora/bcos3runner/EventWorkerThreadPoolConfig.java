package com.webank.truora.bcos3runner;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
/*线程池配置*/
@Configuration
public class EventWorkerThreadPoolConfig {

    @Value("${eventWorker.pool.core-size:20}")
    private int corePoolSize ;
    @Value("${eventWorker.pool.max-size:50}")
    private int maxPoolSize ;
    @Value("${eventWorker.pool.queue-capacity:150}")
    private int queueCapacity ;
    @Value("${eventWorker.pool.thread-name-prefix:eventWorker}")
    private String namePrefix ;
    @Value("${eventWorker.pool.keep-alive:60}")
    private int keepAliveSeconds ;

    @Bean
    public Executor eventWorkerAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //核心线程数
        executor.setCorePoolSize(corePoolSize);
        //任务队列的大小
        executor.setQueueCapacity(queueCapacity);
        //线程前缀名
        executor.setThreadNamePrefix(namePrefix);
        //线程存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        /**
         * 拒绝处理策略
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //线程初始化
        executor.initialize();
        return executor;
    }
}
