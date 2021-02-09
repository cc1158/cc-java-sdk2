package com.cc.sdk2.jsdk.commons.async;



import com.cc.sdk2.jsdk.base.SimpleThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2020/2/12 10:53
 **/
public class SimpleAsyncTaskPool extends AbstractTaskPool {

    private ExecutorService threadPool;

    public SimpleAsyncTaskPool() {
        this(DEFAULT_TASK_POOL_SIZE, DEFAULT_TASK_LOW_THRESHOLD_SIZE);
    }

    public SimpleAsyncTaskPool(int taskPoolSize, int threshold) {
        this(taskPoolSize, threshold, new SimpleThreadFactory("SimpleAsyncTaskPool", false));
    }

    public SimpleAsyncTaskPool(int taskPoolSize, int threshold, ThreadFactory threadFactory) {
        super(taskPoolSize, threshold);
        threadPool = Executors.newFixedThreadPool(poolSize, threadFactory);
    }

    @Override
    void shutAsyncQueue() {
        this.threadPool.shutdown();
    }

    @Override
    <T> Future<T> addAsyncTaskQueue(TaskWrapper<T> taskWrapper) {
        return this.threadPool.submit(taskWrapper);
    }
}
