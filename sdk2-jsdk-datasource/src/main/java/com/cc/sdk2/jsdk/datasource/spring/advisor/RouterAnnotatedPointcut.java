package com.cc.sdk2.jsdk.datasource.spring.advisor;

import com.cc.sdk2.jsdk.datasource.annotation.Router;
import com.cc.sdk2.jsdk.datasource.spring.AnnotationFinder;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * All rights reserved, copyright@cc.hu
 * 拦截点，拦截有{@link Router}注解的接口类和方法
 * @author cc
 * @version 1.0
 * @date 2021/1/3 19:02
 **/
public class RouterAnnotatedPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (Object.class.equals(method.getDeclaringClass())) {
            return false;
        }
        return null != AnnotationFinder.find(Router.class, method, targetClass);
    }
}
