package com.cc.sdk2.jsdk.datasource.spring.boot;

import com.cc.sdk2.jsdk.datasource.GroupDataSource;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/1/3 10:50
 **/
public class GroupDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    //因为在自动配置中禁止了spring的DataSource数据源配置，所以这里要注册数据源，这样才能使用spring数据源的管理
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        GroupDataSourceProperties dataSourceProperties = new GroupDataSourceProperties();
        Binder.get(this.environment).bind(GroupDataSourceProperties.prefix, Bindable.ofInstance(dataSourceProperties));

        Map<String, DataSource> allDataSources = new HashMap<>();

        Map<String, DataSource> defaultDataSourcePerGroup = new HashMap<>();

        Map<String, Map<String, DataSource>> allDataSourcesPerGroup = new HashMap<>();

        //遍历配置
        dataSourceProperties.getGroups().forEach(group -> {
            //初始化该组默认数据源
            if (group.getDefaultDataSource() != null && group.getDefaultDataSource().trim().length() > 0) {
                DataSource dataSource = getRealDataSource(allDataSources, group.getDefaultDataSource());
                defaultDataSourcePerGroup.put(group.getName(), dataSource);
            }
            //初始化该组下全部数据源
            if (group.getDataSources() != null && group.getDataSources().size() > 0) {
                Map<String, DataSource> dataSources = new HashMap<>();
                group.getDataSources().forEach(dataSourceKey -> {
                    DataSource dataSource = getRealDataSource(allDataSources, dataSourceKey);
                    dataSources.put(dataSourceKey, dataSource);
                });
                allDataSourcesPerGroup.put(group.getName(), dataSources);
            }
        });

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(GroupDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultRealDataSourcePerGroup", defaultDataSourcePerGroup);
        mpv.addPropertyValue("realDataSourcesPerGroup", allDataSourcesPerGroup);
        //TODO 需要测试是否注册默认数据源
//        Map<String, DataSource> dataSourceMap = targetDataSourcesPerGroup.get(RoutingDataSource.DEFAULT_GROUP_NAME);
//        if (dataSourceMap != null && dataSourceMap.size() == 1) {
//            mpv.addPropertyValue("defaultTargetDataSource", dataSourceMap.values().toArray()[0]);
//        }
        registry.registerBeanDefinition("dataSource", beanDefinition);
    }


    private DataSource getRealDataSource(Map<String, DataSource> allDataSources, String prefix) {
        return allDataSources.computeIfAbsent(prefix, key -> {
            DataSourceProperties dataSourceProperties = getDataSourceProperties(key);
            DataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().build();
            if (dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
                org.apache.tomcat.jdbc.pool.DataSource tomcatDataSource = (org.apache.tomcat.jdbc.pool.DataSource) dataSource;
                PoolConfiguration poolProperties = tomcatDataSource.getPoolProperties();
                Binder.get(this.environment).bind("spring.datasource.tomcat", Bindable.ofInstance(poolProperties));
                Binder.get(environment).bind(GroupDataSourceProperties.prefix + "." + prefix, Bindable.ofInstance(poolProperties));
            }
            return dataSource;
        });
    }

    /**
     * 绑定spring的DataSource
     */
    private DataSourceProperties getDataSourceProperties(String key) {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        Binder.get(this.environment).bind("spring.datasource", Bindable.ofInstance(dataSourceProperties));
        Binder.get(this.environment).bind(GroupDataSourceProperties.prefix + "." + key, Bindable.ofInstance(dataSourceProperties));
        if (dataSourceProperties.getType() == null) {
            dataSourceProperties.setType(org.apache.tomcat.jdbc.pool.DataSource.class);

        }
        return dataSourceProperties;
    }
}
