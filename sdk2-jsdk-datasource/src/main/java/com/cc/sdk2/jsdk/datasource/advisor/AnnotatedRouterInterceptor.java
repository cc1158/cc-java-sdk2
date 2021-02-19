package com.cc.sdk2.jsdk.datasource.advisor;


import com.cc.sdk2.jsdk.datasource.GroupDataSource;
import com.cc.sdk2.jsdk.datasource.router.DataSourceRouter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotatedRouterInterceptor {

    private GroupDataSource groupDataSource;


    private Map<Class<? extends DataSourceRouter>, DataSourceRouter> routerRules = new ConcurrentHashMap<>();


    public AnnotatedRouterInterceptor(GroupDataSource groupDataSource, List<DataSourceRouter> dataSourceRouters) {
        this.groupDataSource = groupDataSource;
        dataSourceRouters.forEach(item -> routerRules.put(item.getClass(), item));
    }


}
