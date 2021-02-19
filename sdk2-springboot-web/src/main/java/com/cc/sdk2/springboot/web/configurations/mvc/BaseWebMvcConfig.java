package com.cc.sdk2.springboot.web.configurations.mvc;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cc.sdk2.springboot.web.configurations.mvc.version.ApiVersionRequestMapping;
import com.cc.sdk2.springboot.web.servlet.listener.TraceIdGenerateListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.servlet.ServletRequestListener;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class BaseWebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Bean
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping(ContentNegotiationManager contentNegotiationManager, FormattingConversionService conversionService, ResourceUrlProvider resourceUrlProvider) {
        ApiVersionRequestMapping versionMapping = new ApiVersionRequestMapping();
        versionMapping.setOrder(0);
        versionMapping.setInterceptors(getInterceptors(conversionService, resourceUrlProvider));
        return versionMapping;
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //字符串解析器
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        converters.add(stringHttpMessageConverter);
        //json解析器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                // 保留map空的字段
                SerializerFeature.WriteMapNullValue,
                // 将String类型的null转成""
                //SerializerFeature.WriteNullStringAsEmpty,
                // 将Number类型的null转成0
                //SerializerFeature.WriteNullNumberAsZero,
                // 将List类型的null转成[]
                SerializerFeature.WriteNullListAsEmpty,
                // 将Boolean类型的null转成false
                //SerializerFeature.WriteNullBooleanAsFalse,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect
        );
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        List<MediaType> mediaTypeList = new ArrayList<>();
        // 解决中文乱码问题，相当于在Controller上的@RequestMapping中加了个属性produces = "application/json"
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypeList);
        converters.add(converter);
    }

    @Override
    protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        ThreadPoolTaskExecutor mvcThreadPool = new ThreadPoolTaskExecutor();
        //核心线程数
        mvcThreadPool.setCorePoolSize(2000);
        //线程总数
        mvcThreadPool.setMaxPoolSize(3000);
        //线程执行最长时间
        mvcThreadPool.setKeepAliveSeconds(15);
        //队列长度
        mvcThreadPool.setQueueCapacity(1000);
        //设置执行名称
        mvcThreadPool.setThreadNamePrefix("mvcAsync-");
        //设置拒绝策略
        mvcThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        mvcThreadPool.initialize();
        //执行线程池
        configurer.setTaskExecutor(mvcThreadPool);
        //退出时间
        configurer.setDefaultTimeout(15 * 1000);
    }

    @Bean
    public ServletListenerRegistrationBean<ServletRequestListener> addSessionListener() {
        ServletListenerRegistrationBean<ServletRequestListener> listenerRegistrationBean = new ServletListenerRegistrationBean<>();
        listenerRegistrationBean.setListener(new TraceIdGenerateListener());
        return listenerRegistrationBean;
    }
}
