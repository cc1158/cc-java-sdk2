package com.cc.sdk2.task.xxljob.spring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * All rights reserved, copyright@cc.hu
 * xxljob 配置属性
 * @author cc
 * @version 1.0
 * @date 2019/11/12 22:09
 **/

@Getter
@Setter
@ConfigurationProperties(prefix = "cc-task.xxljob")
public class XxlJobConfigProperties {

    /**
     * 管理端地址
     */
    private String adminAddresses;

    /**
     * access token
     */
    private String accessToken;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
     */
    private String address;

    /**
     * IP地址，在多网卡环境下指定一个特定的ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private int port = 9999;

    /**
     * 日志路径
     */
    private String logPath = "/data/applogs/xxl-job/jobhandler";

    /**
     * 日志保留天数
     */
    private int logRetentionDays = -1;
}
