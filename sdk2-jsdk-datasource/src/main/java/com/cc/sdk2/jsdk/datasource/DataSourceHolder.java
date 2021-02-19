package com.cc.sdk2.jsdk.datasource;

import javax.sql.DataSource;
import java.util.LinkedList;

public class DataSourceHolder {
    /**
     * 封装数据源信息
     */
    public class DataSourceInfo {
        private String group;
        /**
         * 数据源对应的key
         */
        private String key;
        /**
         * 真正的datasource
         */
        private DataSource dataSource;

        public DataSourceInfo(String group, String key, DataSource dataSource) {
            this.group = group;
            this.key = key;
            this.dataSource = dataSource;
        }

        public String getGroup() {
            return group;
        }

        public String getKey() {
            return key;
        }

        public DataSource getDataSource() {
            return dataSource;
        }

        @Override
        public String toString() {
            return "DataSourceInfo{" +
                    "group='" + group + '\'' +
                    ", key='" + key + '\'' +
                    ", dataSource=" + dataSource +
                    '}';
        }
    }

    /**
     * 存储当前数据源的列表
     */
    private final ThreadLocal<LinkedList<DataSourceInfo>> DATA_SOURCE_KEYS = new ThreadLocal<>();

    /**
     * 获取当前链接个数
     * @return  当前的链接个数
     */
    public int count() {
        LinkedList<DataSourceInfo> keys = DATA_SOURCE_KEYS.get();
        if (null == keys || keys.isEmpty()) {
            return 0;
        }
        return keys.size();
    }

    /**
     * 获取当前datasource
     * @return 当前DataSource
     */
    public DataSourceInfo get() {
        LinkedList<DataSourceInfo> keys = DATA_SOURCE_KEYS.get();
        if (null == keys || keys.isEmpty()) {
            return null;
        }
        return keys.getFirst();
    }

    /**
     * 设置当前数据源
     * @param group     组名
     * @param dataSourceKey     数据源key
     * @param dataSource    数据源
     */
    public void set(String group, String dataSourceKey, DataSource dataSource) {
        LinkedList<DataSourceInfo> keys = DATA_SOURCE_KEYS.get();
        if (null == keys) {
            keys = new LinkedList<>();
            DATA_SOURCE_KEYS.set(keys);
        }
        keys.addFirst(new DataSourceInfo(group, dataSourceKey, dataSource));
    }

    /**
     * 清空当前数据源
     */
    public void clear() {
        LinkedList<DataSourceInfo> keys = DATA_SOURCE_KEYS.get();
        if (null == keys || keys.isEmpty()) {
            DATA_SOURCE_KEYS.remove();
            return;
        }
        keys.removeFirst();
    }



}
