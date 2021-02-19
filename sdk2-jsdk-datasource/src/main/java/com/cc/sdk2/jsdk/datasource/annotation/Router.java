package com.cc.sdk2.jsdk.datasource.annotation;


import com.cc.sdk2.jsdk.datasource.DataSourceGroup;
import com.cc.sdk2.jsdk.datasource.router.DataSourceRouter;
import com.cc.sdk2.jsdk.datasource.router.DefaultDataSourceRouter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 组数据源路由
 * @author sen.hu
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Router {

    /**
     * 组名
     * @return
     */
    String group() default DataSourceGroup.DEFAULT_GROUP_KEY;

    /**
     * 路由算法 类
     * @return
     */
    Class<? extends DataSourceRouter> value() default DefaultDataSourceRouter.class;


}
