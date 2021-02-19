package com.cc.sdk2.redis.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cc-redis")
class RedisClientConfigurationProperties {
    /**
     * redis address eg:127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381
     */
    private String hosts;
    /**
     * the redis auth
     */
    private String auth;
    /**
     * whether is a cluster server
     */
    private boolean cluster = false;

    private Integer connTimeout = 0;

    private Integer soTimeout = 0;

    private Integer maxTries = 0;


    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public boolean isCluster() {
        return cluster;
    }

    public void setCluster(boolean cluster) {
        this.cluster = cluster;
    }

    public Integer getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(Integer connTimeout) {
        this.connTimeout = connTimeout;
    }

    public Integer getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(Integer soTimeout) {
        this.soTimeout = soTimeout;
    }

    public Integer getMaxTries() {
        return maxTries;
    }

    public void setMaxTries(Integer maxTries) {
        this.maxTries = maxTries;
    }
}
