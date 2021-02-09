package com.cc.sdk2.jsdk.commons.async;

interface TaskCounter {
    /**
     * 线程加入
     * @return
     */
    int increase();

    /**
     * 线程执行完结束
     * @return
     */
    int decrease();
}
