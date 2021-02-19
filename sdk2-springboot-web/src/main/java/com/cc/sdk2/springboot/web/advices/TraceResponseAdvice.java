package com.cc.sdk2.springboot.web.advices;

import com.alibaba.fastjson.JSON;
import com.cc.sdk2.jsdk.commons.result.*;
import com.cc.sdk2.springboot.web.RequestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Date;

/**
 * 拦截controller设置返回traceId
 *
 * @author sen.hu
 * @date 2018/11/29 18:33
 **/
@RestControllerAdvice
public class TraceResponseAdvice implements ResponseBodyAdvice<Object> {
    private final static Logger logger = LogManager.getLogger(TraceResponseAdvice.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            if (selectedContentType.isCompatibleWith(MediaType.APPLICATION_JSON) || selectedContentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
                if (body instanceof ApiResult) {
                    ApiResult<?> result = (ApiResult<?>) body;
                    result.setTraceId(RequestContext.traceId());
                    return result;
                }
            }
            logger.debug("resp====>{}", JSON.toJSONString(body));
            return body;
        } finally {
            logger.info("request cost:======>{}", (System.currentTimeMillis() - RequestContext.requestTime()));
        }
    }
}
