package com.ctsi.ssdc.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-08-30 10:55
 **/
public class ThreadPoolManagerUtils {
    /**
     * 初始线程池大小
     *
     */
    public static final int THREAD_POOL_SIZE = 16;

    public static void executeThread(Runnable runnable){
        //使用ThreadFactoryBuilder创建自定义线程名称
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("线程池创建线程-%d").build();
        // 创建线程池，其中任务队列需要结合实际情况设置合理的容量
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                THREAD_POOL_SIZE,
                THREAD_POOL_SIZE,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        //执行线程
        executor.execute(runnable);
        //关闭线程池
        executor.shutdown();
    }
}
