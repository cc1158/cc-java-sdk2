package com.cc.sdk2.jsdk.commons.processor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/4/11 10:27
 **/
public abstract class ChainProcessor<T> {

    /**
     * 上下文
     */
    public class Context {
        private Map<String, Object> contextData = new ConcurrentHashMap<>();

        public void addConData(String key, Object data) {
            this.contextData.put(key, data);
        }

        public <T> T getContextData(String key) {
            if (this.contextData.containsKey(key)) {
                return (T) contextData.get(key);
            }
            return null;
        }

    }

    /**
     * 链式处理上线文
     */
    private Context context;
    /**
     * 下一个要处理
     */
    private ChainProcessor<T> next;

    /**
     *
     * @param data 要处理的数据
     * @return  处理是否成功
     */
    protected abstract boolean process(T data);

    /**
     * 设置上下文
     * @param context
     * @return 当前对象
     */
    public ChainProcessor setContext(Context context) {
        this.context = context;
        return this;
    }

    /**
     * 获取处理器上下文
     * @return 上下文
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * 开始启动处理
     * @param data 要处理的数据
     */
    public void start(T data) {
        long startTime = System.currentTimeMillis();
        boolean success = this.process(data);
        System.out.printf("[layer-%s] process cost %d milliseconds \n", this.toString(), System.currentTimeMillis() - startTime);
        if (success && next != null) {
            this.next.process(data);
        }
    }

    /**
     * 添加下一个处理器
     * @param chainProcessor 处理器
     */
    public void addProcessor(ChainProcessor<T> chainProcessor) {
        if (this.next == null) {
            this.next = chainProcessor;
            return ;
        }
        ChainProcessor<T> nextChain = this.next;
        while (nextChain.next != null) {
            nextChain = nextChain.next;
        }
        nextChain.next = chainProcessor;
    }

}
