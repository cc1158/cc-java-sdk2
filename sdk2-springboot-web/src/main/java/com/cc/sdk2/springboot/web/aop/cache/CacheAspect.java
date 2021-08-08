package com.cc.sdk2.springboot.web.aop.cache;

import com.cc.sdk2.jsdk.commons.utils.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/8/4 21:29
 **/
@Aspect
@Component
public class CacheAspect implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private ThreadLocal<Boolean> NEED_PUT_CACHE = ThreadLocal.withInitial(()-> false);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Before(value = "@annotation(cache) && target(bean) ", argNames = "joinPoint, cache, bean")
    public void before(JoinPoint joinPoint, Cache cache, Object bean) {

    }

    @Around(value = "@annotation(cache)", argNames = "joinPoint, cache")
    public Object around(ProceedingJoinPoint joinPoint, Cache cache) throws Throwable {
        String cacheKey = null;
        if (StringUtil.isNullOrEmpty(cache.key())) {
            cacheKey = joinPoint.getSignature().getDeclaringTypeName() + "_" + joinPoint.getSignature().getName() + "_" + "args" + joinPoint.getArgs().length;
        }
        Class retClass =  getReturnType(joinPoint);
        if (retClass == null || retClass.getSimpleName().equals("void") ) {
            return joinPoint.proceed(joinPoint.getArgs());
        }
        if (cache.isRemoveOpt()) {
            return joinPoint.proceed(joinPoint.getArgs());
        }
        CacheOperation cacheOperation = (CacheOperation) applicationContext.getBean(cache.cacheOptClass());
        Object obj = cacheOperation.get(cacheKey, retClass);
        if (null == obj) {
            NEED_PUT_CACHE.set(true);
            return joinPoint.proceed(joinPoint.getArgs());
        }
        return obj;
    }

    private Class<?> getReturnType(JoinPoint joinPoint) {
        Class<?> [] classes = new Class[joinPoint.getArgs().length];
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            classes[i] = joinPoint.getArgs()[i].getClass();
        }
        try {
            Method method = joinPoint.getTarget().getClass().getMethod(joinPoint.getSignature().getName(), classes);
            return method.getReturnType();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @AfterReturning(returning = "retObj", value = "@annotation(cache)", argNames = "joinPoint, cache, retObj")
    public Object afterReturning(JoinPoint joinPoint, Cache cache, Object retObj) {
        String cacheKey = null;
        if (StringUtil.isNullOrEmpty(cache.key())) {
            cacheKey = joinPoint.getSignature().getDeclaringTypeName() + "_" + joinPoint.getSignature().getName() + "_" + "args" + joinPoint.getArgs().length;
        }
        CacheOperation cacheOperation = (CacheOperation) applicationContext.getBean(cache.cacheOptClass());
        if (cache.isRemoveOpt()) {
            cacheOperation.remove(cacheKey);
        } else {
            if (cache.validTime() > 0) {
                cacheOperation.put(cacheKey, retObj, cache.validTime());
            } else {
                cacheOperation.put(cacheKey, retObj);
            }
        }
        return retObj;
    }

    @After(value = "@annotation(cache)) && target(bean)", argNames = "joinPoint, cache")
    public void after(JoinPoint joinPoint, Cache cache) {
        System.out.println("in after");
        NEED_PUT_CACHE.remove();
    }


}
