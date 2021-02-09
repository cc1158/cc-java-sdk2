package com.cc.sdk2.jsdk.commons.async;

import com.cc.sdk2.jsdk.commons.AppRunEnvs;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2020/2/12 10:51
 **/
public abstract class AbstractTaskPool implements TaskCounter {

    static final int DEFAULT_TASK_POOL_SIZE = AppRunEnvs.ProcessorNumber;

    static final int DEFAULT_TASK_LOW_THRESHOLD_SIZE = DEFAULT_TASK_POOL_SIZE * 2;

    private AtomicBoolean shutDownFlag = new AtomicBoolean(false);
    final Integer poolSize;
    final Integer taskThreshold;
    /**
     * unfinished task counter
     */
    private AtomicInteger unfinishedTaskNumber = new AtomicInteger(0);
    private CountDownLatch awaitLoadCountDown = new CountDownLatch(1);
    private CountDownLatch noMoreTaskCountDown = new CountDownLatch(1);

    public AbstractTaskPool(int taskPoolSize, int taskThreshold) {
        this.poolSize = taskPoolSize > 0 ? taskPoolSize : DEFAULT_TASK_POOL_SIZE;
        this.taskThreshold = taskThreshold > 0 ? taskThreshold : this.poolSize * 2;
    }


    @Override
    public int increase() {
        return this.unfinishedTaskNumber.incrementAndGet();
    }

    @Override
    public int decrease() {
        int unfinishedNumber = unfinishedTaskNumber.decrementAndGet();
        if (unfinishedNumber < taskThreshold) {
            awaitLoadCountDown.countDown();
        }
        if (unfinishedNumber == 0 && shutDownFlag.get()) {
            noMoreTaskCountDown.countDown();
        }
        return unfinishedNumber;
    }

    /**
     * task pool shutdown
     */
    public void shutdown() {
        if (!shutDownFlag.get()) {
            shutDownFlag.set(true);
            if (noMoreTaskCountDown.getCount() != 0) {
                try {
                    noMoreTaskCountDown.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            shutAsyncQueue();
        }
    }

    abstract void shutAsyncQueue();

    protected void awaitLoad() {
        if (awaitLoadCountDown.getCount() == 0) {
            synchronized (this) {
                if (awaitLoadCountDown.getCount() == 0) {
                    awaitLoadCountDown = new CountDownLatch(1);
                }
            }
        }
        synchronized (this) {
            if (unfinishedTaskNumber.get() < taskThreshold) {
                return;
            }
        }
        try {
            awaitLoadCountDown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public Future<Void> submit(Runnable task) {
        if (shutDownFlag.get()) {
            throw new RuntimeException("The Async Task Pool Has been shutdown");
        }
        return this.submit(() -> {
            task.run();
            return null;
        });
    }

    public <T> Future<T> submit(Callable<T> task) {
        this.awaitLoad();
        return this.addAsyncTaskQueue(new TaskWrapper<>(this, task));
    }

    abstract <T> Future<T> addAsyncTaskQueue(TaskWrapper<T> taskWrapper);

}
