package com.cc.sdk2.jsdk.datasource;

import com.cc.sdk2.jsdk.datasource.connection.TransactionalConnection;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * 组数据源，不是真正实现数据库连接
 * 只是封装，实现管理多个数据源及事务链式回滚
 */
public class GroupDataSource implements DataSource {

    /** 每个分组对应的可路由数据源组 */
    private Map<String, DataSourceGroup> groups = new ConcurrentHashMap<>();

    /** 存储当前使用的数据源的holder */
    private DataSourceHolder dataSourceHolder = new DataSourceHolder();


    public static class Builder {

        private GroupDataSource groupDataSource = new GroupDataSource();


        private Builder(){}

        /**
         * 获取已有的datasource group 或者新建
         * @param groupName
         * @return
         */
        private DataSourceGroup getOrGenerateGroup(String groupName) {
            return groupDataSource.groups.computeIfAbsent(groupName, groupName1 -> new DataSourceGroup(groupName));
        }


        /**
         * 设置某一个组的全部数据源
         * @param groupName     组名
         * @param realDataSources   真正的数据源
         */
        public Builder setGroupRealDataSources(String groupName, Map<String, DataSource> realDataSources) {
            DataSourceGroup group = getOrGenerateGroup(groupName);
            group.setDataSources(realDataSources);
            return this;
        }

        /**
         * 设置某一个组的的全部数据源，默认dataSourceKey=group+index
         * @param groupName     组名
         * @param realDataSources   真正的数据源
         * @return
         */
        public Builder setGroupRealDataSources(String groupName, List<DataSource> realDataSources) {
            DataSourceGroup group = getOrGenerateGroup(groupName);
            group.setDataSources(realDataSources);
            return this;
        }
        /**
         * 设置每一个组的默认数据源
         * @param groupName     组名
         * @param dataSource    真实的数据源
         * @return
         */
        public Builder setGroupDefaultRealDataSource(String groupName, DataSource dataSource) {
            DataSourceGroup group = getOrGenerateGroup(groupName);
            group.setDefaultDataSource(dataSource);
            return this;
        }

        public Builder setDefaultGroupDataSources(Map<String, DataSource> dataSources) {
            return setGroupRealDataSources(DataSourceGroup.DEFAULT_GROUP_KEY, dataSources);
        }

        /**
         * 设置默认组中的，默认数据源
         * （一个组中可能有多个数据库）
         */
        public Builder setDefaultGroupDefaultRealDataSource(DataSource dataSource) {
            return setGroupDefaultRealDataSource(DataSourceGroup.DEFAULT_GROUP_KEY, dataSource);
        }

        public GroupDataSource build() {
            return groupDataSource;
        }

    }

    /**
     * 获取groupdatasour构建器
     *
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    private DataSourceGroup getOrGenerateGroup(String groupName) {
        return groups.computeIfAbsent(groupName, groupName1 -> new DataSourceGroup(groupName));
    }

    /**
     * 设置每组的默认连接数据源
     * @param dataSourceMap     数据源(key=groupName, v=dataSource)
     */
    public void setDefaultRealDataSourcePerGroup(Map<String, DataSource> dataSourceMap) {
        dataSourceMap.forEach((key, value) -> getOrGenerateGroup(key).setDefaultDataSource(value));
    }

    /**
     * 设置组的全部真实数据源
     * @param dataSourceMap     数据源(key=groupName, v=Map<String, DataSource>)
     */
    public void setRealDataSourcesPerGroup(Map<String, Map<String, DataSource>> dataSourceMap) {
        for (Map.Entry<String, Map<String, DataSource>> item : dataSourceMap.entrySet()) {
            DataSourceGroup dataSourceGroup = getOrGenerateGroup(item.getKey());
            dataSourceGroup.setDataSources(item.getValue());
        }
    }


    private DataSource getRealDataSource(String groupName, String dataSourceKey) {
        DataSourceGroup dataSourceGroup = groups.get(groupName);
        if (null == dataSourceGroup) {
            throw new RuntimeException("undefined data source group " + groupName);
        }

        DataSource dataSource = (dataSourceKey == null) ? dataSourceGroup.getDefaultDataSource() : dataSourceGroup.getDataSource(dataSourceKey);
        if (dataSource == null) {
            throw new RuntimeException("could NOT find data source at " + dataSourceKey + " from group " + groupName);
        }
        return dataSource;
    }

    /**
     * 获取当前数据源
     * @return 数据源datasource
     */
    public DataSource getCurrentDataSource() {
        DataSourceHolder.DataSourceInfo dataSourceInfo = dataSourceHolder.get();
        if (null == dataSourceInfo) {
            //若holder里没有存储任何数据源，说明没有设置任何路由规则，这时要取默认的数据源
            return getRealDataSource(DataSourceGroup.DEFAULT_GROUP_KEY, null);
        }
        return dataSourceInfo.getDataSource();
    }

    /**
     * 设置当前使用的数据源
     * <p></p>
     * 必须和clearCurrentDataSource一起使用{@link #clearCurrentDataSource()}
     * @param groupName     组名
     * @param dataSourceKey 数据源key
     */
    public void setCurrentDataSource(String groupName, String dataSourceKey) {
        DataSource dataSource = getRealDataSource(groupName, dataSourceKey);
        dataSourceHolder.set(groupName, dataSourceKey, dataSource);
    }

    /**
     * 清除当前正在使用的数据源
     * <p></p>
     * 必须和{@link #setCurrentDataSource(String, String)}成对使用
     */
    public void clearCurrentDataSource() {
        dataSourceHolder.clear();
    }

    /**
     * 获取指定分组下的原始数据源列表
     *
     * @param groupName 分组名称
     * @return 数据源标识符对应数据源的哈希表
     */
    public Map<String, DataSource> getDataSourcesOfGroup(String groupName) {
        DataSourceGroup group = groups.get(groupName);
        if (null == group) {
            return Collections.emptyMap();
        }
        return group.getDataSources();
    }




    @Override
    public Connection getConnection() throws SQLException {
        return new TransactionalConnection(this);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new TransactionalConnection(this, username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getCurrentDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getCurrentDataSource().isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return getCurrentDataSource().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        getCurrentDataSource().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        getCurrentDataSource().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return getCurrentDataSource().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return getCurrentDataSource().getParentLogger();
    }
}
