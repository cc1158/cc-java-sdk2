package com.cc.sdk2.rabbitmq.producer;

import com.cc.sdk2.rabbitmq.RabbitMQClient;
import com.cc.sdk2.rabbitmq.spring.RabbitMQProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  抽象消息生产者封装
 * @author sen.hu
 * @date 2019/5/21 16:26
 **/
public class RabbitMQProducer {

    protected RabbitMQClient rabbitMQClient;
    private RabbitMQProperties.Producer producer;
    private Channel channel;
    private ReentrantLock lock = new ReentrantLock();

    public RabbitMQProducer(RabbitMQClient rabbitMQClient, RabbitMQProperties.Producer producer) throws IOException, TimeoutException {
        this.rabbitMQClient = rabbitMQClient;
        this.producer = producer;
        if (this.producer.isDeclare()) {
            declare();
        }
    }

    private void declare() throws IOException, TimeoutException {
        this.rabbitMQClient.exchangeDeclare(producer.getExchange(), producer.getExchangeType());
    }

    /**
     * 消息构建
     */
    public class MessageBuilder {

        private Map<String, Object> headers = new HashMap<>();

        private String expiration;
        private int deliverMode = 2;
        private String contentType = "text/plain";
        private byte[] body;

        private MessageBuilder(){}

        public MessageBuilder addHeader(String key, Object value) {
            headers.put(key, value);
            return this;
        }

        public MessageBuilder expiration(String expiration) {
            this.expiration = expiration;
            return this;
        }

        /**
         * 持久化消息，写磁盘  1 ram  2 disc
         * @param deliverMode
         * @return
         */
        public MessageBuilder deliverMode(int deliverMode) {
            this.deliverMode = deliverMode;
            return this;
        }

        public MessageBuilder contentType(String contentType) {
            if (contentType != null && !contentType.isEmpty()) {
                this.contentType = contentType;
            }
            return this;
        }

        public MessageBuilder body(byte[] body) {
            this.body = body;
            return this;
        }

        public void send() throws Exception {
            if (body == null) {
                throw new IllegalArgumentException("body is null");
            }

            lock.lock();
            try {
                if (null == channel) {
                    channel = rabbitMQClient.getConnection().createChannel();
                }
            } finally {
                lock.unlock();
            }
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
                    .contentType(contentType)
                    .expiration(expiration)
                    //持久化消息，写磁盘  1 ram  2 disc
                    .deliveryMode(deliverMode)
                    .headers(headers)
                    .build();
            channel.basicPublish(producer.getExchange(), producer.getRoutingKey(), basicProperties, body);
        }


    }

    /**消息构建器*/
    public MessageBuilder messageBuilder() {
        return new MessageBuilder();
    }

}
