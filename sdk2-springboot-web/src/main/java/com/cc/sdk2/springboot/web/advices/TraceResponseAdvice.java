package com.cc.sdk2.springboot.web.advices;

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
 * @author  sen.hu
 * @date  2018/11/29 18:33
 **/
@RestControllerAdvice
public class TraceResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (selectedContentType.isCompatibleWith(MediaType.APPLICATION_JSON) || selectedContentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            if (body instanceof ResultBuilder.ApiResult) {
                ResultBuilder.ApiResult result = (ResultBuilder.ApiResult) body;
                result.setTraceId(RequestContext.getRequestId());
                Long reqTimestamp = RequestContext.getRequestTimestamp() == null ? System.currentTimeMillis() : RequestContext.getRequestTimestamp();
                result.setTime(DateUtil.formatDate(new Date(reqTimestamp), "yyyy-MM-dd HH:mm:ss.S"));
                return result;
            }
        }
        return body;
    }
}
