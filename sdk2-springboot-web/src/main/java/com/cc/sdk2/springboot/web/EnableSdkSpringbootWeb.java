package com.cc.sdk2.springboot.web;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自定义框架扫描注解
 * @author cc.hu
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SdkSpringbootWebRegistrar.class)
public @interface EnableSdkSpringbootWeb {
    /**
     * 自定义扫描
     * @return
     */
    String[] customizeScanPackages() default {};

    /**
     * 启用全局advice
     * @return
     */
    boolean enableGlobalAdvice() default true;

    /**
     * 启用默认mvc配置
     * @return
     */
    boolean enableMvcConfiguration() default true;

    /**
     * 启用验证配置
     * @return
     */
    boolean enableValidateConfiguration() default true;

}