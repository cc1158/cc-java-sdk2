package com.cc.sdk2.springboot.web;

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
 *
 * @author sen.hu
 * @date 2019/9/30 16:50
 **/
public class SdkSpringbootWebRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

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
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableSdkSpringbootWeb.class.getName()));
        if (annoAttrs != null) {
            Set<String> scanPackages = new LinkedHashSet<>();
            boolean enableMvcConfig = annoAttrs.getBoolean("enableMvcConfiguration");
            if (enableMvcConfig) {
                scanPackages.add("com.cc.sdk2.springboot.web.configurations.mvc");
            }
            boolean enableValidateConfig = annoAttrs.getBoolean("enableValidateConfiguration");
            if (enableValidateConfig) {
                scanPackages.add("com.cc.sdk2.springboot.web.configurations.validate");
            }
            boolean enableAdvice = annoAttrs.getBoolean("enableGlobalAdvice");
            if (enableAdvice) {
                scanPackages.add("com.cc.sdk2.springboot.web.advices");
            }
            String[] packages = annoAttrs.getStringArray("customizeScanPackages");
            if (packages.length > 0) {
                scanPackages.addAll(Arrays.asList(packages));
            }
            ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
            scanner.setResourceLoader(this.resourceLoader);
            scanner.scan(scanPackages.stream().toArray(String[]::new));
        }
    }
}
