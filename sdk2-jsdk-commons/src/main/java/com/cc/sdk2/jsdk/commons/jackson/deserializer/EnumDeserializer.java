package com.cc.sdk2.jsdk.commons.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2019/11/10 15:14
 **/
public class EnumDeserializer<T extends Enum<T>> extends JsonDeserializer<T> {

    private final Class<T> clazz;

    public EnumDeserializer(Class<T> tClass) {
        this.clazz = tClass;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Method[] methods = clazz.getDeclaredMethods();
        int value = p.getIntValue();
        for (Method method : methods) {
            if (method.getName().equals("ofValue")) {
                try {
                    return (T) method.invoke(null, value);
                } catch (Exception e) {
                }
            }
        }
        return null;
    }
}
