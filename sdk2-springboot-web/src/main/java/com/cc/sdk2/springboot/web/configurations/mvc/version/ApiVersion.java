package com.cc.sdk2.springboot.web.configurations.mvc.version;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping
public @interface ApiVersion {
    float vNo() default 1.0F;
}
