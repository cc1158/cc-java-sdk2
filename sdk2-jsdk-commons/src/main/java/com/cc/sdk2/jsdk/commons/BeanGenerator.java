package com.cc.sdk2.jsdk.commons;

/**
 * All rights reserved, copyright@cc.hu
 * 对象生成器
 * @author cc
 * @version 1.0
 * @date 2021/2/8 20:28
 **/
public class BeanGenerator<T> {

    private final Class<T> clazz;

    public BeanGenerator(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 通过默认构造函数创建对象
     *
     * @return 新对象
     */
    public T generate() {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过构造函数创建对象
     *
     * @param constructTypes  参数类型
     * @param initArgs  初始化参数
     * @return 对象
     */
    public T generate(Class<?>[] constructTypes, Object... initArgs) {
        try {
            return clazz.getDeclaredConstructor(constructTypes).newInstance(initArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
