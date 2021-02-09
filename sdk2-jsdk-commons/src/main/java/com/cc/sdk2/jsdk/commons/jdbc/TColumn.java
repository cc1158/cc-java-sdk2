package com.cc.sdk2.jsdk.commons.jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TColumn {
    /**
     * table column name
     *
     * @return
     */
    String value() default "";
}
