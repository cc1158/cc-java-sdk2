package com.cc.sdk2.jsdk.datasource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 数据源组
 */
public class DataSourceGroup {

    public static final String DEFAULT_GROUP_KEY = "default";

    public static final String DEFAULT_KEY_PREFIX = "group";

    /**组名*/
    private final String name;

    /**
     * 多个数据源中，默认返回数据源
     */
    private DataSource defaultDataSource;
    /**
     * 组中全部的数据源
     */
    private Map<String, DataSource> dataSources = Collections.emptyMap();

    public DataSourceGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DataSource getDefaultDataSource() {
        return defaultDataSource;
    }

    public void setDefaultDataSource(DataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    public DataSource getDataSource(String dataSourceKey) {
        return dataSources.get(dataSourceKey);
    }

    /**
     * 获取组数据源
     * @return
     */
    public Map<String, DataSource> getDataSources() {
        return dataSources;
    }

    /**
     * 设置数据源map
     * @param dataSources
     */
    public void setDataSources(Map<String, DataSource> dataSources) {
        this.dataSources = Collections.unmodifiableMap(dataSources);
    }

    public void setDataSources(List<DataSource> dataSources) {
        for (int i = 0; i < dataSources.size(); i++) {
            this.dataSources.put(DEFAULT_KEY_PREFIX + i, dataSources.get(i));
        }
    }
}
