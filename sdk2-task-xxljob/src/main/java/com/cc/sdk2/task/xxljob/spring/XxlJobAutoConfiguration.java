package com.cc.sdk2.task.xxljob.spring;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * All rights reserved, copyright@cc.hu
 * xxljob spring 自动配置
 * @author cc
 * @version 1.0
 * @date 2019/11/12 22:09
 **/
@Configuration
@EnableConfigurationProperties(value = XxlJobConfigProperties.class)
public class XxlJobAutoConfiguration {


    @Bean
    @ConditionalOnProperty(value = "cc-task.xxljob", name = {"adminAddresses"})
    public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobConfigProperties jobConfigProperties) {
        System.out.println(">>>>>>>>>>> xxl-job config init. <<<<<<<<<<<<<");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(jobConfigProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(jobConfigProperties.getAppName());
        xxlJobSpringExecutor.setAddress(jobConfigProperties.getAddress());
        xxlJobSpringExecutor.setIp(jobConfigProperties.getIp());
        xxlJobSpringExecutor.setPort(jobConfigProperties.getPort());
        xxlJobSpringExecutor.setAccessToken(jobConfigProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(jobConfigProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(jobConfigProperties.getLogRetentionDays());

        return xxlJobSpringExecutor;
    }

}
