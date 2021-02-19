package com.cc.sdk2.jsdk.datasource.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/1/3 11:16
 **/
@ConfigurationProperties(prefix = "cc-datasource")
public class GroupDataSourceProperties {

    public static final String prefix = "cc-datasource";

    /**数据源列表*/
    private final List<Group> groups = new ArrayList<>();

    public List<Group> getGroups() {
        return groups;
    }

    public static class Group {
        /**分组名称*/
        private String name;
        /**该组中数据源列表*/
        private List<String >dataSources;
        /**该组下默认数据源*/
        private String defaultDataSource;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getDataSources() {
            return dataSources;
        }

        public void setDataSources(List<String> dataSources) {
            this.dataSources = dataSources;
        }

        public String getDefaultDataSource() {
            return defaultDataSource;
        }

        public void setDefaultDataSource(String defaultDataSource) {
            this.defaultDataSource = defaultDataSource;
        }
    }



}
