package com.cc.sdk2.jsdk.datasource.spring;

import com.cc.sdk2.jsdk.datasource.GroupDataSource;
import com.cc.sdk2.jsdk.datasource.router.DataSourceRouter;
import com.cc.sdk2.jsdk.datasource.spring.advisor.*;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;

/**
 * All rights reserved, copyright@cc.hu
 * 组数据源切面拦截器，继承spring的切面
 *
 * @author cc
 * @version 1.0
 * @date 2021/1/3 13:29
 **/
public class GroupDataSourceAspect extends AbstractPointcutAdvisor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Pointcut pointcut = new RouterAnnotatedPointcut();

    private volatile RouterAnnotatedInterceptor interceptor;

    private Advice advice = (MethodInterceptor) invocation -> {
        RouterAnnotatedMethod method = RouterAnnotatedMethod.of(invocation);
        return generateInterceptor().invoke(method);
    };


    private RouterAnnotatedInterceptor generateInterceptor() {
        if (null == interceptor) {
            synchronized (this) {
                if (null == interceptor) {
                    GroupDataSource groupDataSource = applicationContext.getBean(GroupDataSource.class);
                    List<DataSourceRouter> routers = new ArrayList<>(applicationContext.getBeansOfType(DataSourceRouter.class).values());
                    this.interceptor = new RouterAnnotatedInterceptor(groupDataSource, routers);
                }
            }
        }
        return interceptor;
    }


    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public int getOrder() {
        //* 保证在@Transactional之前执行
        return -1;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        AopConfigUtils.registerAutoProxyCreatorIfNecessary(registry);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
