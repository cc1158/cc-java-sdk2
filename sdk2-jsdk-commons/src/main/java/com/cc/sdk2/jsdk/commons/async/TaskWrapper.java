package com.cc.sdk2.jsdk.commons.async;

import java.util.concurrent.Callable;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2020/2/12 10:41
 **/
class TaskWrapper<T> implements Callable<T> {

    private TaskCounter taskCounter;
    private Callable<T> callableTask;

    public TaskWrapper(TaskCounter taskCounter, Callable<T> callableTask) {
        taskCounter.increase();
        this.taskCounter = taskCounter;
        this.callableTask = callableTask;
    }

    @Override
    public T call() throws Exception {
        long startTime = System.currentTimeMillis();
        try {
           return this.callableTask.call();
        } finally {
            System.out.println(String.format("[TaskWrapper-execute] cost time(millis)=%d", (System.currentTimeMillis() - startTime)));
            taskCounter.decrease();
        }
    }
}
