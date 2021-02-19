package com.cc.sdk2.springboot.web;

/**
 * web请求上下文
 *
 * @author cc.sen.hu
 */
public class RequestContext {

    private static ThreadLocal<String> TRACE_ID = new ThreadLocal<>();
    private static ThreadLocal<Long> REQUEST_TIME = new ThreadLocal<>();


    public static String traceId() {
         return TRACE_ID.get();
    }

    public static void setTraceId(String traceId) {
         TRACE_ID.set(traceId);
         REQUEST_TIME.set(System.currentTimeMillis());
    }

    public static Long requestTime() {
        return REQUEST_TIME.get();
    }

}
