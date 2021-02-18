package com.cc.sdk2.springboot.web.servlet.listener;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

public class TraceIdGenerateListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();

        if (request.getMethod().toUpperCase().equals(HttpMethod.OPTIONS.name())) {
            return ;
        }
        String traceId = java.util.UUID.randomUUID().toString();
        RequestContext.setRequestId(traceId);
        ThreadContext.put("traceId", traceId);

    }
}
