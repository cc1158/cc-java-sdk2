package com.cc.sdk2.task.xxljob;

import com.cc.sdk2.task.xxljob.spring.XxlJobConfigProperties;
import com.xxl.job.core.executor.impl.XxlJobSimpleExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * All rights reserved, copyright@cc.hu
 * xxl job 操作类
 *
 * @author cc
 * @version 1.0
 * @date 2019/11/12 22:09
 **/

public class XxlJobTask {



    public static class Builder {

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

        /**
         * xxl-job 对象
         */
        private List<Object> jobBeanList = new ArrayList<>();


        private Builder() {
        }

        public Builder adminAddresses(String adminAddresses) {
            this.adminAddresses = adminAddresses;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder logPath(String logPath) {
            this.logPath = logPath;
            return this;
        }

        public Builder logRetentionDays(int logRetentionDays) {
            this.logRetentionDays = logRetentionDays;
            return this;
        }

        public Builder addJobBeans(Object ...jobs) {
            this.jobBeanList.addAll(Arrays.asList(jobs));
            return this;
        }

        public XxlJobTask build() {
            XxlJobTask xxlJobTask = new XxlJobTask();
            xxlJobTask.builder = this;
            return xxlJobTask;
        }
    }


    public static Builder builder() {
        return new Builder();
    }

    /**
     * 任务创建器
     */
    private Builder builder;
    /**
     * xxljob 执行器
     */
    private XxlJobSimpleExecutor xxlJobExecutor = null;
    /**
     * 是否启动
     */
    private boolean started = false;
    /**
     * 是否需要停止
     */
    private boolean stop = false;


    public XxlJobTask init() {
        // init executor
        xxlJobExecutor = new XxlJobSimpleExecutor();
        xxlJobExecutor.setAdminAddresses(this.builder.adminAddresses);
        xxlJobExecutor.setAccessToken(this.builder.accessToken);
        xxlJobExecutor.setAppname(this.builder.appName);
        xxlJobExecutor.setAddress(this.builder.address);
        xxlJobExecutor.setIp(this.builder.ip);
        xxlJobExecutor.setPort(this.builder.port);
        xxlJobExecutor.setLogPath(this.builder.logPath);
        xxlJobExecutor.setLogRetentionDays(this.builder.logRetentionDays);

        // registry job bean
        xxlJobExecutor.setXxlJobBeanList(this.builder.jobBeanList);
        return this;
    }


    public void start() {
        if (this.xxlJobExecutor == null) {
            throw new RuntimeException("please call init first");
        }

        try {
            xxlJobExecutor.start();
            this.started = true;
            Thread thread = new Thread(()-> {
                while (!stop) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                    }
                }
                if (started && xxlJobExecutor != null) {
                    xxlJobExecutor.destroy();
                }
            });
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("xxl-job start failed....");
        }
    }

    public void stop() {
        this.stop = true;
    }


    public String getConfig() {
        return this.builder.toString();
    }

}
