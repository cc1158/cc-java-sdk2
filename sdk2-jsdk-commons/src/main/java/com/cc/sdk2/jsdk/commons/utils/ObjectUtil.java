package com.cc.sdk2.jsdk.commons.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * All rights reserved, copyright@cc.hu
 * 对象工具类
 *
 * @author cc
 * @version 1.0
 * @date 2021/2/8 20:14
 **/
public class ObjectUtil {

    /**
     * 获取方法
     *
     * @param clazz 类型
     * @return
     */
    public static Method[] getMethods(Class clazz) {
        Class searchType = clazz;
        List<Method> ret = new ArrayList<>();
        while (Object.class != searchType && searchType != null) {
            Method[] methods = searchType.getDeclaredMethods();
            if (methods.length > 0) {
                ret.addAll(Arrays.asList(methods));
            }
            searchType = searchType.getSuperclass();
        }
        return ret.toArray(new Method[0]);
    }

    /**
     * 根据方法名和参数类型获取
     *
     * @param clazz
     * @param methodName
     * @param paramTypes
     * @return
     */
    public static Method getMethod(Class clazz, String methodName, Class... paramTypes) {
        Method ret = null;
        Class searchType = clazz;
        while (Object.class != searchType && searchType != null) {
            try {
                ret = searchType.getDeclaredMethod(methodName, paramTypes);
                if (ret != null) {
                    break;
                }
            } catch (NoSuchMethodException e) {
            }
            searchType = searchType.getSuperclass();
        }
        return ret;
    }


    /**
     * 获取所有的fields
     *
     * @param clazz
     * @return
     */
    public static Field[] getFields(Class clazz) {
        Class searchType = clazz;
        List<Field> ret = new ArrayList<>();
        while (searchType != null && Object.class != searchType) {
            Field[] fields = searchType.getDeclaredFields();
            if (fields.length > 0) {
                ret.addAll(Arrays.asList(fields));
            }
            searchType = searchType.getSuperclass();
        }
        return ret.toArray(new Field[0]);
    }

    /**
     * 把一个对象赋值到另一个对象
     *
     * @param srcInstance 源对象
     * @param targetClass 目标对象clazz
     * @param <T>         要赋值的源对象
     * @param <R>         要赋值的目标对象clazz
     * @return 目标对象的一个实例
     */
    public static <T, R> R copyFields(T srcInstance, Class<R> targetClass) throws IllegalAccessException, InstantiationException {
        if (srcInstance == null || targetClass == null) {
            return null;
        }
        R destInstance = targetClass.newInstance();
        copyFields(srcInstance, destInstance);
        return destInstance;
    }

    /**
     * 两个相同的对象实例进行属性值copy（浅拷贝）
     *
     * @param srcInstance  源对象
     * @param destInstance 目标对象
     * @param <T>          对象类型
     */
    public static <T> void copyFields(T srcInstance, T destInstance) throws IllegalAccessException {
        Field[] srcFields = getFields(srcInstance.getClass());
        Field[] destFields = getFields(destInstance.getClass());
        for (Field destF : destFields) {
            for (Field srcF : srcFields) {
                destF.setAccessible(true);
                srcF.setAccessible(true);
                //属性值不为空，名字相同，并且类型相同则进行赋值
                if (srcF.get(srcInstance) != null
                        && srcF.getName().equals(destF.getName())
                        && srcF.getType().equals(destF.getType())) {
                    destF.set(destInstance, srcF.get(srcInstance));
                }
            }
        }
    }

    /**
     * 执行对象的某一个方法
     *
     * @param obj        对象
     * @param methodName 方法名
     * @param params     参数
     * @return 方法返回结果
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object invoke(Object obj, String methodName, Object... params) throws InvocationTargetException, IllegalAccessException {
        if (obj == null || methodName == null || methodName.trim().length() == 0) {
            throw new IllegalArgumentException("params error");
        }
        Method method;
        if (params != null && params.length > 0) {
            Class[] paramTypes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = params[i].getClass();
            }
            method = getMethod(obj.getClass(), methodName, paramTypes);
        } else {
            method = getMethod(obj.getClass(), methodName);
        }
        if (method != null) {
            method.setAccessible(true);
            return method.invoke(obj, params);
        }
        throw new NoSuchMethodError(methodName);

    }

    /**
     * 对象序列化为字节数据组
     * @param obj   对象
     * @return 字节数组
     * @throws IOException
     */
    public static byte[] serializeToBytes(Object obj) throws IOException {
        if (obj instanceof Serializable) {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
            objOut.writeObject(obj);
            return bytesOut.toByteArray();
        }
        throw new IllegalArgumentException("the object not implement Serializable interface");
    }

    /**
     * 对象字节数组反序列化为队形
     * @param objBytes  对象字节数组
     * @return  对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserializeFromBytes(byte[] objBytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bytesIn = new ByteArrayInputStream(objBytes);
        ObjectInputStream objIn = new ObjectInputStream(bytesIn);
        return objIn.readObject();
    }

}
