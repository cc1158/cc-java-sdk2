package com.cc.sdk2.springboot.console;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 控制台注册器
 * @author sen.hu
 * @date 2019/10/8 11:34
 **/
public class SdkSpringbootConsoleRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        //bean注册器
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableSdkSpringbootConsole.class.getName()));
        if (annoAttrs != null) {
            Set<String> scanPackages = new LinkedHashSet<>();
            scanPackages.add("com.cc.framework.springboot.console");

            String basePackage[] = annoAttrs.getStringArray("customizeScanPackages");
            if (basePackage.length > 0) {
                scanPackages.addAll(Arrays.asList(basePackage));
            }

            ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
            scanner.setResourceLoader(this.resourceLoader);
            scanner.scan(scanPackages.stream().toArray(String[]::new));
        }
    }
}
