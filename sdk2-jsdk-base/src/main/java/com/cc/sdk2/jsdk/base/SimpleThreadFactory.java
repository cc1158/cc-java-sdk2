package com.cc.sdk2.jsdk.base;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * All rights reserved, copyright@cc.hu
 *  线程工厂
 * @author cc
 * @version 1.0
 * @date 2020/4/27 22:33
 **/
public final class SimpleThreadFactory implements ThreadFactory {

    private String threadNamePrefix;
    private boolean daemon;
    private ThreadGroup threadGroup;
    private AtomicInteger threadCounter = new AtomicInteger(1);

    public SimpleThreadFactory(String threadNamePrefix) {
        this(threadNamePrefix, false);
    }

    public SimpleThreadFactory(String threadNamePrefix, boolean daemon) {
        this(threadNamePrefix, daemon, null);
    }

    public SimpleThreadFactory(String threadNamePrefix, boolean daemon, ThreadGroup threadGroup) {
        this.threadNamePrefix = threadNamePrefix == null ? "JsdkThread" : threadNamePrefix;
        this.daemon = daemon;
        if (threadGroup == null) {
            SecurityManager sm = System.getSecurityManager();
            this.threadGroup = sm != null ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
        } else {
            this.threadGroup = threadGroup;
        }
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(this.threadGroup, runnable, this.threadNamePrefix + "-" + threadCounter.getAndIncrement());
        thread.setDaemon(daemon);
        return thread;
    }
}
