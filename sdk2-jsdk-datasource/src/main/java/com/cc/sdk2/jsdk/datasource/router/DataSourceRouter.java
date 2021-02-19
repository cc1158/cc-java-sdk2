package com.cc.sdk2.jsdk.datasource.router;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 组数据源开关
 */
public interface DataSourceRouter {

    //计算使用哪个数据源
    String calculateDataSourceKey(Method method, Object[] args, Class<?> clazz, Map<String, DataSource> dataSources);


}
