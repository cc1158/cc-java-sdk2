package com.cc.sdk2.jsdk.datasource.router;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Map;

public class DefaultDataSourceRouter implements DataSourceRouter {

    @Override
    public String calculateDataSourceKey(Method method, Object[] args, Class<?> clazz, Map<String, DataSource> dataSources) {
        //返回空，就使用当前组内的默认数据源
        return null;
    }
}
