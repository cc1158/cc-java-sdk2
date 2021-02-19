package com.cc.sdk2.redis.clients;

import com.cc.sdk2.redis.serialize.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.*;

/**
 * All rights reserved, copyright@cc.hu
 * 集群客户端
 * @author cc
 * @version 1.0
 * @date 2019/11/12 22:09
 **/
public class ClusterRedisClient extends RedisClient {


    private JedisCluster jedisCluster;

    public ClusterRedisClient(String hosts) {
        this(hosts, null);
    }


    public ClusterRedisClient(String hosts, String auth) {
        this(hosts, auth, DEFAULT_CONN_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, 2);
    }

    public ClusterRedisClient(String hosts, String auth, int connTimeout, int soTimeout, int maxTries) {
        this(hosts, auth, connTimeout, soTimeout, maxTries, null);
    }

    public ClusterRedisClient(String hosts, String auth, int connTimeout, int soTimeout, int maxTries, JedisPoolConfig jedisPoolConfig) {
        super(hosts, auth, jedisPoolConfig);
        this.connTimeout = connTimeout > 0 ? connTimeout : DEFAULT_CONN_TIMEOUT;
        this.socketTimeout = soTimeout > 0 ? soTimeout : DEFAULT_SOCKET_TIMEOUT;
        this.maxTries = maxTries > 0 ? maxTries : 2;
        this.initConnection();
    }

    @Override
    protected void initConnection() {
        try {
            initLock.lock();
            if (!this.redisConnStatus) {
                this.jedisCluster = new JedisCluster(this.redisHosts, this.connTimeout, this.socketTimeout, this.maxTries, this.auth, this.jedisPoolConfig);
            }
        } finally {
            initLock.unlock();
        }
    }

    @Override
    public Jedis getJedis() {
        throw new RuntimeException("redis cluster has no this method");
    }

    @Override
    public List<String> time() {
        throw new RuntimeException("redis cluster has no such method");
    }

    @Override
    public void setIndex(int index) {
        throw new RuntimeException("redis cluster has no such method");
    }

    @Override
    public void setDefaultIndex() {
        throw new RuntimeException("redis cluster has no such method");
    }

    @Override
    public boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    @Override
    public boolean exists(byte[] key) {
        return jedisCluster.exists(key);
    }

    @Override
    public Long del(String key) {
        return jedisCluster.del(key);
    }

    @Override
    public Long del(String... keys) {
        return jedisCluster.del(keys);
    }

    @Override
    public Long del(byte[] key) {
        return jedisCluster.del(key);
    }

    @Override
    public Long del(byte[]... keys) {
        return jedisCluster.del(keys);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public <T> T get(String key, StringDeserializer deserializer, Class<T> clazz) {
        return deserializer.deserialize(jedisCluster.get(key), clazz);
    }

    @Override
    public byte[] get(byte[] key) {
        return jedisCluster.get(key);
    }

    @Override
    public <T> T get(byte[] key, BytesDeserializer deserializer, Class<T> clazz) {
        return deserializer.deserialize(jedisCluster.get(key), clazz);
    }

    @Override
    public String set(String key, String value) {
        return jedisCluster.set(key, value);
    }

    @Override
    public <T> String set(String key, T value, StringSerializer serializer) {
        return jedisCluster.set(key, serializer.serialize(value));
    }

    @Override
    public String set(byte[] key, byte[] value) {
        return jedisCluster.set(key, value);
    }

    @Override
    public <T> String set(byte[] key, T value, BytesSerializer serializer) {
        return jedisCluster.set(key, serializer.serialize(value));
    }

    @Override
    public long setnx(String key, String value) {
        return jedisCluster.setnx(key, value);
    }

    @Override
    public long setnx(byte[] key, byte[] value) {
        return jedisCluster.setnx(key, value);
    }

    @Override
    public String setex(String key, int secs, String value) {
        return jedisCluster.setex(key, secs, value);
    }

    @Override
    public <T> String setex(String key, int secs, T value, StringSerializer serializer) {
        return jedisCluster.setex(key, secs, serializer.serialize(value));
    }

    @Override
    public String setex(byte[] key, int secs, byte[] value) {
        return jedisCluster.setex(key, secs, value);
    }

    @Override
    public <T> String setex(byte[] key, int secs, T value, BytesSerializer serializer) {
        return jedisCluster.setex(key, secs, serializer.serialize(value));
    }

    @Override
    public Long expire(String key, int secs) {
        return jedisCluster.expire(key, secs);
    }

    @Override
    public Long expire(byte[] key, int secs) {
        return jedisCluster.expire(key, secs);
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        return jedisCluster.expireAt(key, unixTime);
    }

    @Override
    public Long expireAt(byte[] key, long unixTime) {
        return jedisCluster.expireAt(key, unixTime);
    }

    @Override
    public Long llen(String key) {
        return jedisCluster.llen(key);
    }

    @Override
    public Long llen(byte[] key) {
        return jedisCluster.llen(key);
    }

    @Override
    public Long lpush(String key, String... values) {
        return jedisCluster.lpush(key, values);
    }

    @Override
    public <T> Long lpush(String key, T[] values, StringSerializer serializer) {
        if (values != null && values.length > 0) {
            List<String> strList = new ArrayList<>();
            for (T item : values) {
                strList.add(serializer.serialize(item));
            }
            return jedisCluster.lpush(key, (String[]) strList.toArray());
        }
        throw new IllegalArgumentException("values are null");
    }

    @Override
    public Long lpush(byte[] key, byte[]... values) {
        return jedisCluster.lpush(key, values);
    }

    @Override
    public <T> Long lpush(byte[] key, T[] values, BytesSerializer serializer) {
        if (values != null && values.length > 0) {
            Long ret = null;
            for (T item : values) {
                ret = jedisCluster.lpush(key, serializer.serialize(item));
            }
            return ret;
        }
        throw new IllegalArgumentException("values are null");
    }

    @Override
    public String rpop(String key) {
        return jedisCluster.rpop(key);
    }

    @Override
    public <T> T rpop(String key, StringDeserializer deserializer, Class<T> clazz) {
        return deserializer.deserialize(jedisCluster.get(key), clazz);
    }

    @Override
    public byte[] rpop(byte[] key) {
        return jedisCluster.rpop(key);
    }

    @Override
    public <T> T rpop(byte[] key, BytesDeserializer deserializer, Class<T> clazz) {
        return deserializer.deserialize(jedisCluster.get(key), clazz);
    }

    @Override
    public Long rpush(String key, String... values) {
        return jedisCluster.rpush(key, values);
    }

    @Override
    public <T> Long rpush(String key, T[] values, StringSerializer serializer) {
        if (values != null && values.length > 0) {
            List<String> strList = new ArrayList<>();
            for (T item : values) {
                strList.add(serializer.serialize(item));
            }
            return jedisCluster.rpush(key, (String[]) strList.toArray());
        }
        throw new RuntimeException("values are null");
    }

    @Override
    public Long rpush(byte[] key, byte[]... values) {
        return jedisCluster.rpush(key, values);
    }

    @Override
    public <T> Long rpush(byte[] key, T[] values, BytesSerializer serializer) {
        if (values != null && values.length > 0) {
            Long ret = null;
            for (T item : values) {
                jedisCluster.rpush(key, serializer.serialize(item));
            }
            return ret;
        }
        throw new RuntimeException("values are null");
    }

    @Override
    public String lpop(String key) {
        return jedisCluster.lpop(key);
    }

    @Override
    public <T> T lpop(String key, StringDeserializer deserializer, Class<T> clazz) {
        return deserializer.deserialize(jedisCluster.get(key), clazz);
    }

    @Override
    public byte[] lpop(byte[] key) {
        return jedisCluster.lpop(key);
    }

    @Override
    public <T> T lpop(byte[] key, BytesDeserializer deserializer, Class<T> clazz) {
        return deserializer.deserialize(jedisCluster.get(key), clazz);
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        return jedisCluster.lrange(key, start, end);
    }

    @Override
    public <T> List<T> lrange(String key, long start, long end, StringDeserializer deserializer, Class<T> clazz) {
        List<T> ret = null;
        List<String> values = jedisCluster.lrange(key, start, end);
        if (values != null && values.size() > 0) {
            ret = new ArrayList<>();
            for (String item : values) {
                ret.add(deserializer.deserialize(item, clazz));
            }
        }
        return ret;
    }

    @Override
    public List<byte[]> lrange(byte[] key, long start, long end) {
        return jedisCluster.lrange(key, start, end);
    }

    @Override
    public <T> List<T> lrang(byte[] key, long start, long end, BytesDeserializer deserializer, Class<T> clazz) {
        List<T> ret = null;
        List<byte[]> values = jedisCluster.lrange(key, start, end);
        if (values != null && values.size() > 0) {
            ret = new ArrayList<>();
            for (byte[] item : values) {
                ret.add(deserializer.deserialize(item, clazz));
            }
        }
        return ret;
    }

    @Override
    public Long incr(String key) {
        return jedisCluster.incr(key);
    }

    @Override
    public Long incr(byte[] key) {
        return jedisCluster.incr(key);
    }

    @Override
    public Long incrBy(String key, long incrs) {
        return jedisCluster.incrBy(key, incrs);
    }

    @Override
    public Long decr(String key) {
        return jedisCluster.decr(key);
    }

    @Override
    public Long decr(byte[] key) {
        return jedisCluster.decr(key);
    }

    @Override
    public Long decrBy(String key, long decrs) {
        return jedisCluster.decrBy(key, decrs);
    }

    @Override
    public String hget(String key, String field) {
        return jedisCluster.hget(key, field);
    }

    @Override
    public <T> T hget(String key, String field, StringDeserializer deserializer, Class<T> clazz) {
        return deserializer.deserialize(jedisCluster.hget(key, field), clazz);
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        return jedisCluster.hget(key, field);
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return jedisCluster.hgetAll(key);
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return jedisCluster.hgetAll(key);
    }

    @Override
    public Long hset(String key, Map<String, String> values) {
        return jedisCluster.hset(key, values);
    }

    @Override
    public Long hset(String key, String field, String value) {
        return jedisCluster.hset(key, field, value);
    }

    @Override
    public <T> Long hset(String key, String field, T value, StringSerializer serializer) {
        return jedisCluster.hset(key, field, serializer.serialize(value));
    }

    @Override
    public Long hset(byte[] key, Map<byte[], byte[]> values) {
        return jedisCluster.hset(key, values);
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        return jedisCluster.hset(key, field, value);
    }

    @Override
    public <T> Long hset(byte[] key, byte[] field, T value, BytesSerializer serializer) {
        return jedisCluster.hset(key, field, serializer.serialize(value));
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        return jedisCluster.hsetnx(key, field, value);
    }

    @Override
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        return jedisCluster.hsetnx(key, field, value);
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        return jedisCluster.hmset(key, hash);
    }

    @Override
    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        return jedisCluster.hmset(key, hash);
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        return jedisCluster.hmget(key, fields);
    }

    @Override
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        return jedisCluster.hmget(key, fields);
    }

    @Override
    public long sadd(String key, String... members) {
        return jedisCluster.sadd(key, members);
    }

    @Override
    public long sadd(byte[] key, byte[]... members) {
        return jedisCluster.sadd(key, members);
    }

    @Override
    public Set<String> smembers(String key) {
        return jedisCluster.smembers(key);
    }

    @Override
    public Set<byte[]> smembers(byte[] key) {
        return jedisCluster.smembers(key);
    }

    @Override
    public boolean getDistributeLock(String key, String requestId, int expSec) {
        String result = jedisCluster.set(key, requestId, SetParams.setParams().nx().ex(expSec));
        if (result.equals(GET_LOCK_SUCCESS)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean releaseDistributeLock(String key, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedisCluster.eval(script, Collections.singletonList(key), Collections.singletonList(requestId));
        if (result.equals(RELEASE_LOCK_SUCCESS)) {
            return true;
        }
        return false;
    }
}
