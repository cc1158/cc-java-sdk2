package com.cc.sdk2.rabbitmq.spring;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2019/10/31 20:46
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "cc-rabbitmq")
public class RabbitMQProperties {
    /**
     * 客户端名称
     */
    private String clientName = "rabbitMQClient";
    /**
     * the rabbit mq addresses and ports
     */
    private String hosts;
    /**
     * use name
     */
    private String user;
    /**
     * auth string
     */
    private String password;
    /**
     * vhost
     */
    private String vHost;
    /**
     * message consumers
     */
    private List<Consumer> consumers = new ArrayList<>();
    /**
     * message producers
     */
    private List<Producer> producers = new ArrayList<>();

    /**
     * mq message consumer
     */
    @Getter
    @Setter
    @ToString
    public static class Consumer {
        /**
         * service bean id
         */
        private String id;
        /**
         * 交换机名称
         */
        private String exchange;
        /**
         * 交换机类型
         */
        private String exchangeType;
        /**
         * 队列名称
         */
        private String queue;
        /**
         * 路由key
         */
        private String routingKey;
        /**
         * 消费者数量
         */
        private int num = 1;
        /**
         * 是否是持久的
         */
        private boolean durable = true;
        /**
         * 排他
         */
        private boolean exclusive = false;
        /**
         * 自动删除
         */
        private boolean autoDeleted = false;
        /**
         * 队列长度
         */
        private int queueLength = 0;
        /**
         *
         */
        private long ttl = 0;
        private String ddlExchange;
        private String ddlRoutingKey;
        private boolean declare = true;

    }

    /**
     * mq message producer
     */
    @Getter
    @Setter
    @ToString
    public static class Producer {
        private String id;
        private String exchange;
        private String exchangeType;
        private String routingKey;
        private boolean declare = true;
    }

}
