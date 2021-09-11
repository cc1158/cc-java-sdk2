package com.cc.sdk2.jsdk.mybatis.spring;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.annotation.Annotation;
import java.util.Properties;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2019/11/26 22:26
 **/
@ConfigurationProperties(prefix = "cc-mybatis")
public class MybatisConfigurationProperties {

    public static class MapperScanner {

        /**
         * 包扫描路径 可用分隔符指定多个 ,
         */
        private String basePackage;
        /** 标记了注解类的接口才代理 */
        private Class<? extends Annotation> annotationClass;

        /**
         * 属性列表
         */
        private final Properties properties = new Properties();

        /**
         * 扩展接口类
         */
        private Class<?> markerInterface;

        /**
         * 动态代理bean生成规则
         */
        private Class<BeanNameGenerator> nameGeneratorClass;

        public String getBasePackage() {
            return basePackage;
        }

        public void setBasePackage(String basePackage) {
            this.basePackage = basePackage;
        }

        public Class<? extends Annotation> getAnnotationClass() {
            return annotationClass;
        }

        public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
            this.annotationClass = annotationClass;
        }

        public Properties getProperties() {
            return properties;
        }

        public Class<?> getMarkerInterface() {
            return markerInterface;
        }

        public void setMarkerInterface(Class<?> markerInterface) {
            this.markerInterface = markerInterface;
        }

        public Class<BeanNameGenerator> getNameGeneratorClass() {
            return nameGeneratorClass;
        }

        public void setNameGeneratorClass(Class<BeanNameGenerator> nameGeneratorClass) {
            this.nameGeneratorClass = nameGeneratorClass;
        }
    }

    /**
     * mybatis mapperscanner 配置
     */
    private final MapperScanner mapperScanner = new MapperScanner();

    public MapperScanner getMapperScanner() {
        return mapperScanner;
    }
}
