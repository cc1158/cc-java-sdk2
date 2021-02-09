package com.cc.sdk2.jsdk.commons.date;

import com.cc.sdk2.jsdk.commons.AppRunEnvs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * All rights reserved, copyright@cc.hu
 * 线程安全格式化
 *
 * @author cc
 * @version 1.0
 * @date 2020/4/27 23:15
 **/
public class ThreadSafeFormatter {

    private final ThreadLocal<SimpleDateFormat> THREAD_LOCAL;

    public ThreadSafeFormatter(final String pattern) {
        this(pattern, AppRunEnvs.DefaultTimeZone);
    }

    public ThreadSafeFormatter(final String pattern, final TimeZone timeZone) {
        this.THREAD_LOCAL = ThreadLocal.withInitial(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setTimeZone(timeZone);
            return sdf;
        });
    }

    public String format(Date date) {
        return this.THREAD_LOCAL.get().format(date);
    }

    public Date parse(String date) throws ParseException {
        return this.THREAD_LOCAL.get().parse(date);
    }
}
