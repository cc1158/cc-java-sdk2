package com.cc.sdk2.rabbitmq.consumer;

import com.cc.sdk2.rabbitmq.MessageProcessor;
import com.cc.sdk2.rabbitmq.RabbitMQClient;
import com.cc.sdk2.rabbitmq.spring.RabbitMQProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 消息消费者
 *
 * @author cc.hu
 * @date 2019/5/21 13:46
 **/
public class RabbitMQConsumer {


    public static final String destroyMethod = "stop";

    /**
     * rabbitmq客户端
     */
    protected RabbitMQClient rabbitMQClient;

    private boolean started = false;

    private ReentrantLock lock = new ReentrantLock();

    private Map<String, Channel> consumers = new HashMap<>(5);
    private RabbitMQProperties.Consumer consumer;

    /**
     *
     * @param rabbitMQClient
     * @param consumer
     * @throws IOException
     * @throws TimeoutException
     */
    public RabbitMQConsumer(RabbitMQClient rabbitMQClient, RabbitMQProperties.Consumer consumer) throws IOException, TimeoutException {
        this.rabbitMQClient = rabbitMQClient;
        this.consumer = consumer;
        if (consumer.isDeclare()) {
            init();
        }
    }

    void init() throws IOException, TimeoutException {
        //声明交换机
        this.rabbitMQClient.exchangeDeclare(consumer.getExchange(), consumer.getExchangeType());
        //声明队列
        this.rabbitMQClient.queueDeclare(consumer.getQueue(), consumer.isDurable(), consumer.isExclusive(), consumer.isAutoDeleted(),
                consumer.getQueueLength(), consumer.getTtl(), consumer.getDdlExchange(), consumer.getDdlRoutingKey());
        //绑定队列到交换机
        this.rabbitMQClient.queueBind(consumer.getQueue(), consumer.getExchange(), consumer.getRoutingKey());
    }

    /**
     * 开始消费
     */
    public void startConsuming(MessageProcessor processor) throws Exception {
        lock.lock();
        try {
            if (started) {
                System.out.println("this consumer has been started...");
                return;
            }
            for (int i = 0; i < this.consumer.getNum(); i++) {
                Channel channel = rabbitMQClient.getPooledConnection().createChannel();
                String tag = channel.basicConsume(this.consumer.getQueue(), false, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String routingKey = envelope.getRoutingKey();
                        long deliveryTag = envelope.getDeliveryTag();
                        Map<String, Object> headers = properties.getHeaders();
                        //调用消息处理函数
                        try {
                            processor.process(routingKey, deliveryTag, headers, body);
                        } catch (Exception e) {
                        }
                        channel.basicAck(deliveryTag, false);
                    }
                });
                consumers.put(tag, channel);
            }
            this.started = true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 停止消费
     */
    public void stop() {
        Set<String> keys = consumers.keySet();
        for (String key : keys) {
            try {
                Channel channel = consumers.get(key);
                channel.basicCancel(key);
                channel.close();
            } catch (Exception e) {
            }
        }
        consumers.clear();
    }
}
