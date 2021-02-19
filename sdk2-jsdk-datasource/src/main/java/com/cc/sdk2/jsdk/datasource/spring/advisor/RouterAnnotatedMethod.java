package com.cc.sdk2.jsdk.datasource.spring.advisor;

import com.cc.sdk2.jsdk.datasource.annotation.Router;
import com.cc.sdk2.jsdk.datasource.spring.AnnotationFinder;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/1/3 21:58
 **/
public class RouterAnnotatedMethod {

    private Router methodAnnotation;

    private Method method;

    private Object[] args;

    private Class<?> targetClass;

    private Processor processor;


    public static RouterAnnotatedMethod of(MethodInvocation invocation) {
        Router methodAnnotaion = AnnotationFinder.find(Router.class, invocation.getMethod(), invocation.getThis().getClass());
        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();
        Class<?> targetClass = invocation.getThis().getClass();
        Processor processor = invocation::proceed;

        return new RouterAnnotatedMethod(methodAnnotaion, method, args, targetClass, processor);
    }

    private RouterAnnotatedMethod(Router methodAnnotation, Method method, Object[] args, Class<?> targetClass, Processor processor) {
        this.methodAnnotation = methodAnnotation;
        this.method = method;
        this.args = args;
        this.targetClass = targetClass;
        this.processor = processor;
    }

    public Router getMethodAnnotation() {
        return methodAnnotation;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Processor getProcessor() {
        return processor;
    }

    @FunctionalInterface
    public interface Processor {
        Object proceed() throws Throwable;
    }
}
