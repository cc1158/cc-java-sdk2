package com.cc.sdk2.springboot.web.configurations.mvc.version;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * TODO 没有注解的，默认选择1.0版本
 * @Description 自定义版本url请求
 * @Author sen.hu
 * @Date 2018/11/29 18:13
 **/
public class ApiVersionRequestMapping extends RequestMappingHandlerMapping {

    @Nullable
    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        ApiVersion apiVersion = handlerType.getAnnotation(ApiVersion.class);
        return apiVersion == null ? null : new ApiVersionCondition(apiVersion.vNo());
    }

    @Nullable
    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        ApiVersion apiVersion = method.getDeclaredAnnotation(ApiVersion.class);
        return apiVersion == null ? null : new ApiVersionCondition(apiVersion.vNo());
    }
}
