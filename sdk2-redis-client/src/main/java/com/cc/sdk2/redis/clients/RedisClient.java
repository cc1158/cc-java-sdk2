package com.cc.sdk2.redis.clients;

import com.cc.sdk2.redis.serialize.*;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * redis client supply operation
 */
public abstract class RedisClient {

    final static class Tools {
        /**
         * 对象池配置
         * @param maxTotal      最大jedis对象数
         * @param maxIdle       最大空闲数
         * @param minIdle       最小空闲数
         * @param blockWhenExhausted     是否阻塞
         * @param maxWaitTimeMillis     最大等待时间
         * @return
         */
        public static JedisPoolConfig genericPoolConfig(int maxTotal, int maxIdle, int minIdle, boolean blockWhenExhausted, long maxWaitTimeMillis) {
            return genericPoolConfig(maxTotal, maxIdle, minIdle, blockWhenExhausted, maxWaitTimeMillis, MIN_EVICTABLE_IDLE_TIME_MILLIS, DEFAULT_TEST_ON_BORROW, DEFAULT_TEST_WHILE_IDLE);
        }

        public static JedisPoolConfig genericPoolConfig(int maxTotal, int maxIdle, int minIdle, boolean blockWhenExhausted, long maxWaitTimeMillis, long minEvictableIdleTimeMillis, boolean testOnBorrow, boolean testWhileIdle) {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
            jedisPoolConfig.setMaxWaitMillis(maxWaitTimeMillis);
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPoolConfig.setMinIdle(minIdle);
            jedisPoolConfig.setMaxTotal(maxTotal);
            jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            jedisPoolConfig.setTestOnBorrow(testOnBorrow);
            jedisPoolConfig.setTestWhileIdle(testWhileIdle);
            return jedisPoolConfig;
        }
    }

    /**
     * 获取分布式锁成功标识
     */
    protected final static String GET_LOCK_SUCCESS = "OK";
    /**
     * 释放分布式锁成功标识
     */
    protected final static long RELEASE_LOCK_SUCCESS = 1;


    /**
     * 连接池耗尽，是否阻塞等待
     */
    protected static final boolean DEFAULT_BLOCK_WHEN_EXHAUSTED = true;
    /**
     * 当连接满的时候，最大等待时间
     */
    protected static final long DEFAULT_MAX_WAIT_TIME = 20 * 1000;
    /**
     * 连接超时
     */
    protected static final int DEFAULT_CONN_TIMEOUT = 5 * 1000;
    /**
     * 读写数据超时
     */
    protected static final int DEFAULT_SOCKET_TIMEOUT = 5 * 1000;
    /**
     * 最大连接数
     */
    protected static final int DEFAULT_MAX_TOTAL = 20;
    /**
     * 最大空闲数
     */
    protected static final int DEFAULT_MAX_IDLE = 16;
    /**
     * 最小空闲
     */
    protected static final int DEFALUT_MIN_IDLE = 4;
    /**
     * 是否测试，当空闲时
     */
    protected static final boolean DEFAULT_TEST_ON_BORROW = false;
    /**
     * 是否测试，获取连接时
     */
    protected static final boolean DEFAULT_TEST_WHILE_IDLE = false;
    /**
     * 对象池最少存活时间（毫秒）,超过时间则evicted
     */
    protected static final long MIN_EVICTABLE_IDLE_TIME_MILLIS = 10 * 60 * 1000;


    protected int connTimeout;
    protected int socketTimeout;
    /**
     * 最大重试次数
     */
    protected int maxTries = 2;

    protected ReentrantLock initLock = new ReentrantLock();

    protected boolean redisConnStatus = false;

    protected JedisPoolConfig jedisPoolConfig;
    /**
     * redis hosts
     */
    protected String hosts;

    protected String auth;


    protected final Set<HostAndPort> redisHosts = new HashSet<>(6);


    protected RedisClient(String hosts, String auth, JedisPoolConfig jedisPoolConfig) {
        this.hosts = hosts;
        this.auth = auth;
        this.jedisPoolConfig = jedisPoolConfig == null ? Tools.genericPoolConfig(DEFAULT_MAX_TOTAL, DEFAULT_MAX_IDLE, DEFALUT_MIN_IDLE, DEFAULT_BLOCK_WHEN_EXHAUSTED, DEFAULT_MAX_WAIT_TIME) : jedisPoolConfig;
        this.genericRedisHosts();
    }

    private void genericRedisHosts() {
        com.cc.sdk2.jsdk.base.net.HostAndPort.processAddressStr(this.hosts).forEach(item -> {
            HostAndPort hostAndPort = new HostAndPort(item.getHost(), item.getPort());
            redisHosts.add(hostAndPort);
        });
    }

    /**
     * initialize the connection
     */
    protected abstract void initConnection();

    /**
     * get jedis  operation
     *
     * @return
     */
    public abstract Jedis getJedis();

    /**
     * get the redis server time
     *
     * @return
     */
    public abstract List<String> time();

    /**
     * set the redis database
     *
     * @param index
     */
    public abstract void setIndex(int index);

    /**
     * set to the default database
     */
    public abstract void setDefaultIndex();

    /**
     * to judge the key exists
     *
     * @param key
     * @return
     */
    public abstract boolean exists(String key);

    public abstract boolean exists(byte[] key);

    public abstract Long del(String key);

    public abstract Long del(String... keys);

    public abstract Long del(byte[] key);

    public abstract Long del(byte[]... keys);

    public abstract String get(String key);

    public abstract <R> R get(String key, StringDeserializer<R> deserializer);

    public abstract byte[] get(byte[] key);

    public abstract <R> R get(byte[] key, BytesDeserializer<R> deserializer);

    public abstract String set(String key, String value);

    public abstract String set(byte[] key, byte[] value);

    public abstract long setnx(String key, String value);

    public abstract long setnx(byte[] key, byte[] value);

    public abstract String setex(String key, int secs, String value);

    public abstract String setex(byte[] key, int secs, byte[] value);

    public abstract Long expire(String key, int secs);

    public abstract Long expire(byte[] key, int secs);

    public abstract Long expireAt(String key, long unixTime);

    public abstract Long expireAt(byte[] key, long unixTime);

    public abstract Long llen(String key);

    public abstract Long llen(byte[] key);

    public abstract Long lpush(String key, String... values);

    public abstract Long lpush(byte[] key, byte[]... values);

    public abstract String rpop(String key);

    public abstract <R> R rpop(String key, StringDeserializer<R> deserializer);

    public abstract byte[] rpop(byte[] key);

    public abstract <R> R rpop(byte[] key, BytesDeserializer<R> deserializer);

    public abstract Long rpush(String key, String... values);

    public abstract Long rpush(byte[] key, byte[]... values);

    public abstract String lpop(String key);

    public abstract <R> R lpop(String key, StringDeserializer<R> deserializer);

    public abstract byte[] lpop(byte[] key);

    public abstract <R> R lpop(byte[] key, BytesDeserializer<R> deserializer);

    public abstract List<String> lrange(String key, long start, long end);

    public abstract <R> List<R> lrange(String key, long start, long end, StringDeserializer<R> deserializer);

    public abstract List<byte[]> lrange(byte[] key, long start, long end);

    public abstract <R> List<R> lrang(byte[] key, long start, long end, BytesDeserializer<R> deserializer);

    public abstract Long incr(String key);

    public abstract Long incr(byte[] key);

    public abstract Long incrBy(String key, long incrs);

    public abstract Long decr(String key);

    public abstract Long decr(byte[] key);

    public abstract Long decrBy(String key, long decrs);

    public abstract String hget(String key, String field);

    public abstract <R> R hget(String key, String field, StringDeserializer<R> deserializer);

    public abstract byte[] hget(byte[] key, byte[] field);

    public abstract Map<String, String> hgetAll(String key);

    public abstract Map<byte[], byte[]> hgetAll(byte[] key);

    public abstract Long hset(String key, Map<String, String> values);

    public abstract Long hset(String key, String field, String value);

    public abstract Long hset(byte[] key, Map<byte[], byte[]> values);

    public abstract Long hset(byte[] key, byte[] field, byte[] value);

    public abstract Long hsetnx(String key, String field, String value);

    public abstract Long hsetnx(byte[] key, byte[] field, byte[] value);

    public abstract String hmset(String key, Map<String, String> hash);

    public abstract String hmset(byte[] key, Map<byte[], byte[]> hash);

    public abstract List<String> hmget(String key, String... fields);

    public abstract List<byte[]> hmget(byte[] key, byte[]... fields);

    public abstract long sadd(String key, String... members);

    public abstract long sadd(byte[] key, byte[]... members);

    public abstract Set<String> smembers(String key);

    public abstract Set<byte[]> smembers(byte[] key);

    public abstract boolean getDistributeLock(String key, String requestId, int expSec);

    public abstract boolean releaseDistributeLock(String key, String requestId);

}
