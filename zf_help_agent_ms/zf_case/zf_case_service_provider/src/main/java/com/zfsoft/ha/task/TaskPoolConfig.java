package com.zfsoft.ha.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description //定时任务多线程配置类
 * 所有的定时任务都放这里,方便管理<br>
 * 如果想把所有定时任务全部关闭总开关 把@Configuration注释<br>
 * @Author: Wangyh
 * @Date: 2022/8/11 13:42
 */

@EnableAsync //开启异步
@Configuration
public class TaskPoolConfig {
    /**
     * 在线帮代办人员状态定时任务
     *
     * @return
     */
    @Bean("onlineStatusTaskExecutor")
    public Executor onlineStatusTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置为1，任务顺序执行
        executor.setCorePoolSize(10);//核心线程数：线程池创建时候初始化的线程数
        executor.setMaxPoolSize(100);//最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setQueueCapacity(20);//缓冲队列200：缓冲执行任务的队列
        executor.setKeepAliveSeconds(60);//允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setThreadNamePrefix("onlineStatusTaskExecutor-");//线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());// 线程池对拒绝任务的处理策略
        return executor;
    }

    /**
     * 办事人处理
     *
     * @return
     */
    @Bean("handlingAgentTaskExecutor")
    public Executor handlingAgentTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置为1，任务顺序执行
        executor.setCorePoolSize(1);//核心线程数：线程池创建时候初始化的线程数
        executor.setMaxPoolSize(1);//最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setQueueCapacity(20);//缓冲队列200：缓冲执行任务的队列
        executor.setKeepAliveSeconds(60);//允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setThreadNamePrefix("handlingAgentTaskExecutor-");//线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());// 线程池对拒绝任务的处理策略
        return executor;
    }
    /**
     * 每天下午五点半定时执行当天涉及到的未结束服务的服务中的(系统自己更新)
     *
     * @return
     */
    @Bean("serviceTimeEndTaskExecutor")
    public Executor serviceTimeEndTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置为1，任务顺序执行
        executor.setCorePoolSize(1);//核心线程数：线程池创建时候初始化的线程数
        executor.setMaxPoolSize(1);//最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setQueueCapacity(20);//缓冲队列200：缓冲执行任务的队列
        executor.setKeepAliveSeconds(60);//允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setThreadNamePrefix("serviceTimeEndTaskExecutor-");//线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());// 线程池对拒绝任务的处理策略
        return executor;
    }


}
