package com.cc.sdk2.jsdk.datasource.spring.boot;

import com.cc.sdk2.jsdk.datasource.GroupDataSource;
import com.cc.sdk2.jsdk.datasource.spring.GroupDataSourceAspect;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;

/**
 * All rights reserved, copyright@cc.hu
 * spring组数据源自动配置类，主要是切面拦截器，扫描相关注解，更改数据源
 * @author cc
 * @version 1.0
 * @date 2021/1/3 13:24
 **/
@Configuration
@ConditionalOnClass(GroupDataSource.class)
@EnableConfigurationProperties(GroupDataSourceProperties.class)
//在spring自己的数据源配置载入之前注册，才能阻止spring初始化数据源，达到组数据源初始化目的
@AutoConfigureBefore(org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class)
@Import(GroupDataSourceRegister.class)
public class GroupDataSourceAutoConfiguration {
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public static GroupDataSourceAspect groupDataSourceAspect() {
        return new GroupDataSourceAspect();
    }
}
