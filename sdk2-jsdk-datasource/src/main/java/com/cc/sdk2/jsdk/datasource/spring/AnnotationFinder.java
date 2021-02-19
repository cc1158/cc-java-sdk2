package com.cc.sdk2.jsdk.datasource.spring;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/1/3 19:05
 **/
public class AnnotationFinder {

    public static class ArgumentInfo {

        private int index;

        private List<Annotation> annotations = new ArrayList<>();

        public ArgumentInfo(int index) {
            this.index = index;
        }

        public void addAnnotation(Annotation annotation) {
            annotations.add(annotation);
        }

        public boolean hasAnnotation() {
            return !annotations.isEmpty();
        }

        public int getIndex() {
            return index;
        }

        public List<Annotation> getAnnotations() {
            return annotations;
        }

        public Annotation getFirstAnnotation() {
            return annotations.get(0);
        }
    }

    private static Map<AnnotationCacheKey, List<ArgumentInfo>> annotatedArgInfoCache = new ConcurrentHashMap<>();

    /**
     * 获取方法中包含指定注解的参数
     *
     * <p>该方法会首先使用method参数查找，若没有找到包含指定注解的参数，则查找targetClass中对应的method</p>
     *
     * @param annotationClass 注解类类型
     * @param method          方法
     * @param targetClass     真实执行类
     * @param <T>             注解类型
     * @return 包含指定注解的参数列表
     */
    public static <T extends Annotation> List<ArgumentInfo> findAnnotatedArguments(Class<T> annotationClass, Method method, Class<?> targetClass) {
        Method targetMethod = getTargetOrDefaultMethod(method, targetClass);
        return annotatedArgInfoCache.computeIfAbsent(new AnnotationCacheKey(targetMethod, annotationClass), key -> {
            List<ArgumentInfo> result = findAnnotatedArguments(annotationClass, method);
            if (!result.isEmpty() || method.equals(targetMethod)) {
                return result;
            }
            return findAnnotatedArguments(annotationClass, targetMethod);
        });
    }

    private static <T extends Annotation> List<ArgumentInfo> findAnnotatedArguments(Class<T> annotationClass, Method method) {
        List<ArgumentInfo> result = new ArrayList<>();
        Annotation[][] annotationPerArg = method.getParameterAnnotations();
        for (int i = 0; i < annotationPerArg.length; i++) {
            ArgumentInfo argumentInfo = new ArgumentInfo(i);
            Annotation[] annotations = annotationPerArg[i];
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(annotationClass)) {
                    argumentInfo.addAnnotation(annotation);
                }
            }
            if (argumentInfo.hasAnnotation()) {
                result.add(argumentInfo);
            }
        }
        return result;
    }

    /**
     * 获取方法或者类上的指定注解
     *
     * <p>该方法可以查找到以下位置的注解：
     * <ul>
     *     <li>接口方法上的</li>
     *     <li>实现方法上的</li>
     *     <li>接口类上的</li>
     *     <li>实现类上的</li>
     * </ul>
     * </p>
     *
     * @param annotationClass 注解类类型
     * @param method          方法
     * @param targetClass     真实执行类
     * @param <T>             注解类型
     * @return 注解
     */
    public static <T extends Annotation> T find(Class<T> annotationClass, Method method, Class<?> targetClass) {
        Method targetMethod = getTargetOrDefaultMethod(method, targetClass);
        return findFromMethodAndClass(annotationClass, targetMethod, targetClass);
    }

    private static Method getTargetOrDefaultMethod(Method method, Class<?> targetClass) {
        try {
            return targetClass.getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return method;
        }
    }

    private static <T extends Annotation> T findFromMethodAndClass(Class<T> annotationClass, Method method,
                                                                   Class<?> targetClass) {
        T annotation = AnnotationUtils.findAnnotation(targetClass, annotationClass);
        if (annotation != null) {
            return annotation;
        }
        annotation = AnnotationUtils.findAnnotation(method, annotationClass);
        if (annotation != null) {
            return annotation;
        }
        annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), annotationClass);
        if (annotation != null) {
            return annotation;
        }
        return null;
    }

    private static final class AnnotationCacheKey implements Comparable<AnnotationCacheKey> {

        private final AnnotatedElement element;

        private final Class<? extends Annotation> annotationType;

        public AnnotationCacheKey(AnnotatedElement element, Class<? extends Annotation> annotationType) {
            this.element = element;
            this.annotationType = annotationType;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AnnotationCacheKey)) {
                return false;
            }
            AnnotationCacheKey otherKey = (AnnotationCacheKey) other;
            return (this.element.equals(otherKey.element) && this.annotationType.equals(otherKey.annotationType));
        }

        @Override
        public int hashCode() {
            return (this.element.hashCode() * 29 + this.annotationType.hashCode());
        }

        @Override
        public String toString() {
            return "@" + this.annotationType + " on " + this.element;
        }

        @Override
        public int compareTo(AnnotationCacheKey other) {
            int result = this.element.toString().compareTo(other.element.toString());
            if (result == 0) {
                result = this.annotationType.getName().compareTo(other.annotationType.getName());
            }
            return result;
        }
    }


}
