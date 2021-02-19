package com.cc.sdk2.springboot.console;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 激活控制台程序注解
 * @author sen.hu
 * @date 2019/10/8 11:15
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SdkSpringbootConsoleRegistrar.class)
public @interface EnableSdkSpringbootConsole {

    String[] customizeScanPackages() default {};
}
