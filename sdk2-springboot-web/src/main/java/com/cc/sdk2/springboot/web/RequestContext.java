package com.cc.sdk2.springboot.web;

import com.cc.sdk2.jsdk.base.UUID;
import com.cc.sdk2.jsdk.commons.utils.StringUtil;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * web请求上下文
 *
 * @author cc.sen.hu
 */
public class RequestContext {

    public static final String ATTACHMENT_KEY_CLIENT_IP = "client-ip";


    /**
     * 请求唯一id
     */
    private static ThreadLocal<String> TRACE_ID = new InheritableThreadLocal<>();

    /**
     * 请求时间
     */
    private static ThreadLocal<Long> REQUEST_TIME = new ThreadLocal<>();

    /**
     * 传递变量
     */
    private static ThreadLocal<Map<String, String>> attachments = InheritableThreadLocal.withInitial(ConcurrentHashMap::new);


    public static String traceId() {
         return TRACE_ID.get();
    }

    public static void setTraceId() {
        setTraceId(UUID.newRandomUUId());
    }

    public static void setTraceId(String traceId) {
         TRACE_ID.set(traceId);
         REQUEST_TIME.set(System.currentTimeMillis());
    }

    public static Long requestTime() {
        return REQUEST_TIME.get();
    }

    public static void addAttachment(String key, String value) {
        if (!StringUtil.isNullOrEmpty(key) && !StringUtil.isNullOrEmpty(value)) {
            attachments.get().put(key, value);
        }
    }

    public static void deleteAttachment(String key) {
        if (!StringUtil.isNullOrEmpty(key)) {
            attachments.get().remove(key);
        }
    }

    public static String getAttachment(String key) {
        if (!StringUtil.isNullOrEmpty(key)) {
            return attachments.get().get(key);
        }
        return null;
    }

    public Map<String, String> getAttachments() {
        return Collections.unmodifiableMap(attachments.get());
    }

}
