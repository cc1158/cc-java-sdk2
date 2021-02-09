package com.cc.sdk2.jsdk.commons;

import com.cc.sdk2.jsdk.base.SystemProperties;
import com.cc.sdk2.jsdk.commons.utils.NetworkUtil;

import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * All rights reserved, copyright@cc.hu
 * 系统运行环境
 *
 * @author cc
 * @version 1.0
 * @date 2021/2/8 19:08
 **/
public class AppRunEnvs {
    /**
     * 当前环境cpu核心数
     */
    public static int ProcessorNumber = Runtime.getRuntime().availableProcessors();
    /**
     * 当前环境地域
     */
    public static Locale DefaultLocale = Locale.getDefault();
    /**
     * 当前默认时区
     */
    public static TimeZone DefaultTimeZone = TimeZone.getDefault();

    /**
     * 根据key获取系统环境
     *
     * @param key key
     * @return 属性值
     */
    public static String getSystemProperty(String key) {
        return System.getProperty(key);
    }

    /**
     * 获取系统的属性
     *
     * @param systemProperties 系统环境枚举
     * @return 当前属性值
     */
    public static String getSystemProperty(SystemProperties systemProperties) {
        return System.getProperty(systemProperties.key);
    }

    /**
     * 类加载器是否加载该类
     *
     * @param classPackagePath
     * @return true   加载    false  否
     */
    public static boolean classIsLoaded(String classPackagePath) {
        ClassLoader currLoader = Thread.currentThread().getContextClassLoader();
        try {
            currLoader.loadClass(classPackagePath);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 生成一个traceId
     *
     * @return traceId
     */
    public static String genTraceId() {
        String rndId = UUID.randomUUID().toString().replace("-", "");
        String ip = NetworkUtil.getLocalIpV4();
        return rndId + "-" + ip;
    }


}
