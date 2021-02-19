package com.cc.sdk2.jsdk.datasource.utils;

import com.cc.sdk2.jsdk.datasource.GroupDataSource;
import com.cc.sdk2.jsdk.datasource.annotation.Router;
import com.cc.sdk2.jsdk.datasource.router.DataSourceRouter;
import com.cc.sdk2.jsdk.datasource.router.DefaultDataSourceRouter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceSelector implements MethodInterceptor {

    private GroupDataSource groupDataSource;

    private Map<Class<? extends DataSourceRouter>, DataSourceRouter> routerRules = new ConcurrentHashMap<>();

    public DataSourceSelector(GroupDataSource groupDataSource, List<DataSourceRouter> dataSourceRouters) {
        this.groupDataSource = groupDataSource;
        if (dataSourceRouters != null) {
            dataSourceRouters.forEach(item -> routerRules.put(item.getClass(), item));
        }
        if (routerRules.get(DefaultDataSourceRouter.class) == null) {
            routerRules.put(DefaultDataSourceRouter.class, new DefaultDataSourceRouter());
        }
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String group = "default";
        Class clazz = DefaultDataSourceRouter.class;
        Router router = method.getAnnotation(Router.class);
        if (router == null) {
            router = obj.getClass().getAnnotation(Router.class);
        }
        if (router != null) {
            group = router.group();
            clazz = router.value();
        }
        DataSourceRouter dataSourceRouter = routerRules.get(clazz);
        if (dataSourceRouter == null) {
            throw new RuntimeException("未找到到指定的数据源, group:" + group);
        }
        try {
            String dataSourceKey = dataSourceRouter.calculateDataSourceKey(method, args, obj.getClass(), groupDataSource.getDataSourcesOfGroup(group));
            groupDataSource.setCurrentDataSource(group, dataSourceKey);
            return proxy.invokeSuper(obj, args);
        } finally {
            groupDataSource.clearCurrentDataSource();
        }
    }

    public <T> T createDaoProxy(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }


}
