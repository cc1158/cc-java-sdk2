package com.cc.sdk2.jsdk.commons.fastjson;

import com.alibaba.fastjson.serializer.EnumSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class EnumValueSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        Class<?> clazz = object.getClass();
        if (clazz.isEnum()) {
            Field[] fields = clazz.getFields();
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getName().equals("value")) {
                    try {
                        serializer.write(f.get(object));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return;
                }

            }
            EnumSerializer fstSer = new EnumSerializer();
            fstSer.write(serializer, object, fieldName, fieldType, features);
        }
    }
}
