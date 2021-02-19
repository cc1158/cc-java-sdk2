package com.cc.sdk2.rabbitmq.consumer;

import java.util.Map;

/**
 * @Description 消费者回调函数
 * @Author sen.hu
 * @Date 2018/12/20 18:46
 **/
public interface ConsumeCallback {
    /**
     *
     * @param routingKey 从那个routingkey 来的消息
     * @param queueName 队列名称
     * @param headers   message 头
     * @param payload   消息载体
     */
    void doDelivery(String routingKey, String queueName, Map<String, Object> headers, byte[] payload);

}
