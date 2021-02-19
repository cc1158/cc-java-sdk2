package com.cc.sdk2.rabbitmq;

import com.cc.sdk2.jsdk.base.net.HostAndPort;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * rabbit mq 客户端
 * @author sen.hu
 * @date 2019/5/21 16:37
 **/
public class RabbitMQClient {
    /**用户名*/
    private String userName;
    /**密码*/
    private String password;
    /**集群节点*/
    private String addresses;
    /**rmq多租户*/
    private String vHost;
    /**客户端标识*/
    private String tag;


    private List<Address> addressList = new ArrayList<>(3);
    /**连接工厂*/
    private ConnectionFactory connectionFactory;
    /**连接*/
    private Connection connection;
    /**消费者连接*/
    private Connection pooledConnection;
    /**消费者线程池*/
    private ExecutorService threadPool;

    private ReentrantLock lock = new ReentrantLock();

    /***
     * 消费者打开channel数
     */
    private Map<String, Channel> openChannels = new ConcurrentHashMap<>(10);
    /**
     * 消费者tags
     */
    private Map<String, List<String>> consumersMap = new ConcurrentHashMap<>(10);

    public RabbitMQClient(String userName, String password, String addresses) {
        this(userName, password, addresses, "/");
    }

    public RabbitMQClient(String userName, String password, String addresses, String vHost) {
        this.userName = userName;
        this.password = password;
        this.addresses = addresses;
        this.vHost = vHost;
        setRMQAddress();
        String key = userName + addresses + vHost;
        this.tag = Base64.getEncoder().encodeToString(key.getBytes());
        try {
            this.init();
        } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException("RabbitMQ Client Init Failed" + e.getMessage());
        }
    }

    private void setRMQAddress() {
        Set<HostAndPort> hostAndPorts = HostAndPort.processAddressStr(this.addresses);
        if (hostAndPorts != null) {
            hostAndPorts.forEach(item -> {
                Address address = new Address(item.getHost(), item.getPort());
                addressList.add(address);
            });
        }
    }

    private void init() throws IOException, TimeoutException {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setUsername(userName);
        this.connectionFactory.setPassword(password);
        this.connectionFactory.setVirtualHost(vHost);
        //连接自动回复
        this.connectionFactory.setAutomaticRecoveryEnabled(true);
        //10秒重试
        this.connectionFactory.setNetworkRecoveryInterval(10000);
        this.connection = this.connectionFactory.newConnection(addressList);
        threadPool = new ThreadPoolExecutor(20, 200, 0L, TimeUnit.MILLISECONDS,  new LinkedBlockingQueue<Runnable>(1024), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        this.pooledConnection = this.connectionFactory.newConnection(threadPool, addressList);
    }

    public Connection getConnection() {
        return connection;
    }

    public Connection getPooledConnection() {
        return pooledConnection;
    }

    /**
     * 交换机声明
     * @param exchangeName  交换机名称
     * @param exchangeType  交换机类型
     * @return  当前实例
     * @throws IOException
     */
    public RabbitMQClient exchangeDeclare(String exchangeName, String exchangeType) throws IOException, TimeoutException {
        return exchangeDeclare(exchangeName, exchangeType, true);
    }

    /**
     * 交换机声明
     * @param exchangeName 交换机名称
     * @param exchangeType 交换机类型
     * @param durable   是否是持久
     * @return
     * @throws IOException
     */
    public RabbitMQClient exchangeDeclare(String exchangeName, String exchangeType, boolean durable) throws IOException, TimeoutException {
        Channel channel = null;
        try {
            channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, exchangeType, durable);
            return this;
        } finally {
            if (channel != null) {
                channel.close();
            }
        }
    }

    /**
     * 队列声明
     * @param queueName 队列名称
     * @return
     * @throws IOException
     */
    public RabbitMQClient queueDeclare(String queueName) throws IOException, TimeoutException {
        return queueDeclare(queueName, true, false, false);
    }

    /**
     * 队列声明
     * @param queueName     队列名称
     * @param durable   持久化
     * @param exclusive 排他队列
     * @param autoDelete    自动删除
     * @return
     * @throws IOException
     */
    public RabbitMQClient queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete) throws IOException, TimeoutException {
        return this.queueDeclare(queueName, durable, exclusive, autoDelete, 0);
    }

    public RabbitMQClient queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete, int queueLength) throws IOException, TimeoutException {
        return this.queueDeclare(queueName, durable, exclusive, autoDelete, queueLength, 0, null, null);
    }

    /**
     * 声明延时队列
     * @param queueName     队列名称
     * @param durable   持久化
     * @param exclusive 排他队列
     * @param autoDelete    自动删除
     * @param ttl   消息过期时间
     * @param deadLetterExchange        死信交换机
     * @param deadLetterRoutingKey      私信交换机key
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public RabbitMQClient queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete, int queueLength, long ttl, String deadLetterExchange, String deadLetterRoutingKey) throws IOException, TimeoutException {
        Channel channel = null;
        try {
            Map<String, Object> args = new HashMap<>();
            if (queueLength > 0) {
                args.put("x-max-length", queueLength);
            }
            if (ttl > 0) {
                args.put("x-expires", ttl);
            }
            if (deadLetterExchange != null && !"".equals(deadLetterExchange)) {
                args.put("dead-letter-exchange", deadLetterExchange);
            }
            if (deadLetterRoutingKey != null && !"".equals(deadLetterRoutingKey)) {
                args.put("x-dead-letter-routing-key", deadLetterRoutingKey);
            }
            channel = connection.createChannel();
            channel.queueDeclare(queueName, durable, exclusive, autoDelete, args);
            return this;
        } finally {
            if (channel != null) {
                channel.close();
            }
        }
    }

    private RabbitMQClient queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> args) throws IOException, TimeoutException {
        Channel channel = null;
        try {
            channel = connection.createChannel();
            channel.queueDeclare(queueName, durable, exclusive, autoDelete, args);
            return this;
        } finally {
            if (channel != null) {
                channel.close();
            }
        }
    }

    /**
     * 绑定关系声明
     * @param queueName     队列名称
     * @param exchangeName      交换机名称
     * @param routingKey        路由key
     * @return
     * @throws IOException
     */
    public RabbitMQClient queueBind(String queueName, String exchangeName, String routingKey) throws IOException, TimeoutException {
        Channel channel = null;
        try {
            channel = connection.createChannel();
            channel.queueBind(queueName, exchangeName, routingKey);
            return this;
        } finally {
            if (channel != null) {
                channel.close();
            }
        }
    }
}
