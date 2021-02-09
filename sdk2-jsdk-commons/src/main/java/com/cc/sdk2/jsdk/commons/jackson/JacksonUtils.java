package com.cc.sdk2.jsdk.commons.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Map;
import java.util.TimeZone;

/**
 * All rights reserved, copyright@cc.hu
 * jackson 序列化
 * @author  cc
 * @version 1.0
 * @date 2019/11/10 9:34
 **/
public class JacksonUtils {

    public interface ObjectMapperInit {
        void initObjectMapper(ObjectMapper objectMapper);
    }

    private static ThreadLocal<ObjectMapper> THREAD_LOCAL_MAPPER = ThreadLocal.withInitial(() -> {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //不注释,会导致swagger报错
        //objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true);
        //关闭日期序列化为时间戳的功能
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //关闭序列化的时候没有为属性找到getter方法,报错
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //关闭反序列化的时候，没有找到属性的setter报错
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //序列化的时候序列对象的所有属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setTimeZone(TimeZone.getDefault());

        //
//        SimpleModule simpleModule = new SimpleModule();
//        //json值序列化
//        simpleModule.addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
//        //json值反序列化
//        simpleModule.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
//        //json键序列化
//        simpleModule.addKeySerializer(LocalDateTime.class,LocalDateTimeSerializer.INSTANCE);
//        //json键反序列化
//        simpleModule.addKeyDeserializer(LocalDateTime.class, LocalDateTimeKeyDeserializer.INSTANCE);
//        objectMapper.registerModule(simpleModule);


        return objectMapper;
    });

    public static void configJacksonMapper(ObjectMapperInit objectMapperInit) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapperInit.initObjectMapper(objectMapper);
        THREAD_LOCAL_MAPPER.set(objectMapper);
    }


    public static <T> String toJson(T obj) {
        try {
            return THREAD_LOCAL_MAPPER.get().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> String toPrettyJson(T obj) {
        try {
            return THREAD_LOCAL_MAPPER.get().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return THREAD_LOCAL_MAPPER.get().readValue(json, clazz);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  eg   Response<List<User>> response = mapper.readValue(json, new TypeReference<Response<List<User>>>(){});
     *  serialize generic class
     * @param json
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return THREAD_LOCAL_MAPPER.get().readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> klass, Map<Class<?>, JsonDeserializer<?>> deserializers) {
        try {
            ObjectMapper mapper = THREAD_LOCAL_MAPPER.get();
            Module module = new SimpleModule("", Version.unknownVersion(), deserializers);
            mapper.registerModules(new Module[]{module});
            return mapper.readValue(json, klass);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
