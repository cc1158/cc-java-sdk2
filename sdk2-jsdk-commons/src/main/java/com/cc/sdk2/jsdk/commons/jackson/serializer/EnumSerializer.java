package com.cc.sdk2.jsdk.commons.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * All rights reserved, copyright@cc.hu
 * 自定义枚举序列化
 * @author cc
 * @version 1.0
 * @date 2019/11/10 12:49
 **/
public class EnumSerializer extends JsonSerializer<Enum<?>> {

    @Override
    public void serialize(Enum<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value != null) {
            Field[] fields = value.getClass().getFields();
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getName().equals("value")) {
                    try {
                        gen.writeNumber((Integer) f.get(value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return;
                }

            }
        }
    }
}
