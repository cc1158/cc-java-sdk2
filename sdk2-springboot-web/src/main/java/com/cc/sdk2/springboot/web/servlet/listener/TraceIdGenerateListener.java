package com.cc.sdk2.springboot.web.servlet.listener;

import com.cc.sdk2.jsdk.base.UUID;
import com.cc.sdk2.jsdk.commons.utils.StringUtil;
import com.cc.sdk2.springboot.web.RequestContext;
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
        String traceId = UUID.newRandomUUId();
        RequestContext.setTraceId(traceId);
        RequestContext.addAttachment(RequestContext.ATTACHMENT_KEY_CLIENT_IP, getRequestIp(request));
        //log4j2 上线文
        ThreadContext.put("traceId", traceId);

    }

    /**
     * 获取请求ip
     * 首先通过Nginx代理http header获取
     * 然后通过getRemoteAddr()获取
     * 最后判断是否是多个代理
     * 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
     * @param request  请求参数
     * @return 客户端ip
     */
    private String getRequestIp(HttpServletRequest request) {
        String requestIp = request.getHeader("x-forwarded-for");
        if (StringUtil.isNullOrEmpty(requestIp)) {
            requestIp = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isNullOrEmpty(requestIp)) {
              requestIp = request.getRemoteAddr();
        }
        if (StringUtil.isNotNullOrEmpty(requestIp) && requestIp.length() > 15) {
            if (requestIp.indexOf(",") > 0) {
                requestIp = requestIp.substring(0, requestIp.indexOf(","));
            }
        }
        return requestIp;
    }
}
