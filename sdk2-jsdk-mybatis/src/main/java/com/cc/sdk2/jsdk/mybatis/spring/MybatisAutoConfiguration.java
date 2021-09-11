package com.cc.sdk2.jsdk.mybatis.spring;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2019/11/26 22:26
 **/
@Configuration
@EnableConfigurationProperties({MybatisConfigurationProperties.class})
public class MybatisAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "cc-mybatis.mapper-scanner.base-package")
    @ConditionalOnClass(value = MapperScannerConfigurer.class)
    public MapperScannerConfigurer mapperScannerConfigurer(Environment environment) {
        MybatisConfigurationProperties mybatisProperties = new MybatisConfigurationProperties();
        Binder.get(environment).bind("cc-mybatis", Bindable.ofInstance(mybatisProperties));
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(mybatisProperties.getMapperScanner().getBasePackage());
        mapperScannerConfigurer.setAnnotationClass(mybatisProperties.getMapperScanner().getAnnotationClass());
        mapperScannerConfigurer.setProperties(mybatisProperties.getMapperScanner().getProperties());
        if (mybatisProperties.getMapperScanner().getMarkerInterface() != null) {
            mapperScannerConfigurer.setMarkerInterface(mybatisProperties.getMapperScanner().getMarkerInterface());
        }
        if (mybatisProperties.getMapperScanner().getNameGeneratorClass() != null) {
            try {
                BeanNameGenerator nameGenerator = mybatisProperties.getMapperScanner().getNameGeneratorClass().newInstance();
                mapperScannerConfigurer.setNameGenerator(nameGenerator);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("beanGenerator实例化失败", e);
            }
        }
        return mapperScannerConfigurer;
    }

}
