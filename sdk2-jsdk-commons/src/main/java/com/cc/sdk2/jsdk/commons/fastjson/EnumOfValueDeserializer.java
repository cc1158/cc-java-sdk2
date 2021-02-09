package com.cc.sdk2.jsdk.commons.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 枚举根据ofValue和默认的枚举反序列化
 * @author sen.hu
 */
public class EnumOfValueDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        try {
            Class<?> clazz = Class.forName(type.getTypeName());
            if (clazz.isEnum()) {
                //判断是否为int值
                if (parser.lexer.token() == JSONToken.LITERAL_INT) {
                    Object ov = parser.parse();
                    int value = TypeUtils.castToInt(ov);
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.getName().equals("ofValue")) {
                            return (T) method.invoke(null, value);
                        }
                    }
                } else {
                    EnumDeserializer  fstDes = new EnumDeserializer(clazz);
                    return fstDes.deserialze(parser, type, fieldName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

}
