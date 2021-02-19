package com.cc.sdk2.jsdk.datasource.spring.advisor;

import com.cc.sdk2.jsdk.datasource.GroupDataSource;
import com.cc.sdk2.jsdk.datasource.annotation.Router;
import com.cc.sdk2.jsdk.datasource.router.DataSourceRouter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/1/3 22:08
 **/
public class RouterAnnotatedInterceptor {

    private GroupDataSource groupDataSource;

    private Map<Class<? extends DataSourceRouter>, DataSourceRouter> routeRules = new ConcurrentHashMap<>();

    public RouterAnnotatedInterceptor(GroupDataSource groupDataSource, List<DataSourceRouter> dataSourceRouters) {
        this.groupDataSource = groupDataSource;
        for (DataSourceRouter router : dataSourceRouters) {
            this.routeRules.put(router.getClass(), router);
        }
    }

    private DataSourceRouter getRouter(Router router) {
        Class<? extends DataSourceRouter> routerClass = router.value();
        return routeRules.computeIfAbsent(routerClass, (clazz)-> {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getDataSourceKey(DataSourceRouter dataSourceRouter, Method method, Object[] args, Class<?> targetClass, String group) {
        try {
            return dataSourceRouter.calculateDataSourceKey(method, args, targetClass, groupDataSource.getDataSourcesOfGroup(group));
        } catch (Exception e) {
            throw new RuntimeException("get data source key for method " + method.getName() + " error! router " + dataSourceRouter.getClass().getSimpleName(), e);
        }
    }


    public Object invoke(RouterAnnotatedMethod annotatedMethod) throws Throwable {
        Router methodAnnotation = annotatedMethod.getMethodAnnotation();
        DataSourceRouter router = getRouter(methodAnnotation);
        Method method = annotatedMethod.getMethod();
        Object[] args = annotatedMethod.getArgs();
        Class<?> targetClass = annotatedMethod.getTargetClass();
        String group = methodAnnotation.group();
        String dataSourceKey = getDataSourceKey(router, method, args, targetClass, group);
        return process(annotatedMethod.getProcessor(), method.getName(), args, group, dataSourceKey);
    }

    private Object process(RouterAnnotatedMethod.Processor processor, String methodName, Object[] args, String group, String dataSourceKey) throws Throwable {
        long startTime = System.currentTimeMillis();
        groupDataSource.setCurrentDataSource(group, dataSourceKey);
        try {
            return processor.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            groupDataSource.clearCurrentDataSource();
        }
    }


}
