package com.cc.sdk2.jsdk.commons.date;

import com.cc.sdk2.jsdk.commons.AppRunEnvs;
import com.cc.sdk2.jsdk.commons.utils.StringUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * All rights reserved, copyright@cc.hu
 * 日期工具类
 *
 * @author cc
 * @version 1.0
 * @date 2020/4/27 23:21
 **/
public class DateUtil {

    public static Date INITIAL_DATE = new Date(0L);
    public static Date FAR_FAR_AWAY = new Date(System.currentTimeMillis() + 31536000000000L);

    public static long EPOCH_OFFSET_MILLIS;

    static {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        // Java使用的参照标准是1970年，而时间服务器返回的秒是相当1900年的，算一下偏移
        calendar.set(1900, Calendar.JANUARY, 1, 0, 0, 0);
        EPOCH_OFFSET_MILLIS = Math.abs(calendar.getTime().getTime());
    }

    /**
     * 获取ntp server时间用于校准
     *
     * @return 当前时间
     */
    public static Date getNtpServerTime() {
        Socket socket = null;
        try {
            socket = new Socket("time.nist.gov", 37);
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            int t1 = in.read();
            int t2 = in.read();
            int t3 = in.read();
            int t4 = in.read();
            if ((t1 | t2 | t3 | t4) < 0) {
                return null;
            }
            long time = ((long) t1 << 24) + (t2 << 16) + (t3 << 8) + (t4);
            return new Date(time * 1000 - EPOCH_OFFSET_MILLIS);
        } catch (IOException e) {
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }


    /**
     * 获取UTC时区
     *
     * @return
     */
    public static TimeZone getUTCTimeZone() {
        return TimeZone.getTimeZone("UTC");
    }

    /**
     * 获取GMT+hours时区
     *
     * @param rawHoursOffset 小时偏移量
     * @return 目标时区
     */
    public static TimeZone getGMTOffsetTimeZone(int rawHoursOffset) {
        if (rawHoursOffset < 0 || rawHoursOffset > 23) {
            throw new IllegalArgumentException("rawHoursOffset must between 0, 23");
        }
        return TimeZone.getTimeZone(String.format("GMT+%d", rawHoursOffset));
    }

    /**
     * 获取毫秒数偏移时区
     *
     * @param rawOffsetMilliSecond 偏移的毫秒数
     * @param id                   时区id
     * @return 目标时区
     */
    public static TimeZone getFixedTimeZone(int rawOffsetMilliSecond, String id) {
        return new SimpleTimeZone(rawOffsetMilliSecond, id);
    }


    /**
     * 获取默认时区时间
     *
     * @return
     */
    public static Date getNow() {
        return new Date();
    }

    /**
     * 获取当前utc时间
     *
     * @return
     */
    public static Date getUtcNow() {
        return getUtcDate(new Date());
    }

    /**
     * 获取utc日期
     *
     * @param date
     * @return
     */
    public static Date getUtcDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, -(calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)));
        return calendar.getTime();
    }


    /**
     * 获取指定域的日期
     *
     * @param hour
     * @param minutes
     * @param seconds
     * @return
     */
    public static Date getFixedDate(int hour, int minutes, int seconds) {
        return getFixedDate(new Date(), hour, minutes, seconds);
    }

    /**
     * 获取指定域的日期
     *
     * @param date    指定日期
     * @param hour    小时
     * @param minutes 钟
     * @param seconds 秒数
     * @return 指定后的时间
     */
    public static Date getFixedDate(Date date, int hour, int minutes, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 返回当前天时间（0 点 0 分 0 秒）
     *
     * @return 午夜
     */
    public static Date getMidnight() {
        return getMidnight(new Date());
    }

    /**
     * 某一天0点0分0秒的时间
     *
     * @param date 某一天
     * @return 午夜时间
     */
    public static Date getMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当天最后的时间 23：59：59
     *
     * @param date 某一天
     * @return
     */
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 格式时间
     *
     * @param date 日期
     * @return yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 格式化日期
     *
     * @param date    日期
     * @param pattern 样式
     * @return 格式化后字符串
     */
    public static String formatDate(Date date, String pattern) {
        ThreadSafeFormatter sdf = new ThreadSafeFormatter(pattern);
        return sdf.format(date);
    }

    /**
     * format date by fixed pattern and timezone
     *
     * @param date
     * @param pattern
     * @param timeZone
     * @return
     */
    public static String formatDate(Date date, String pattern, TimeZone timeZone) {
        ThreadSafeFormatter tsdf = new ThreadSafeFormatter(pattern, timeZone);
        return tsdf.format(date);
    }

    /**
     * 格式化日期字符串为日期对象
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 日期对象
     * @throws Exception 解析异常
     */
    public static Date parseDateStr(String dateStr, String pattern) throws Exception {
        return parseDateStr(dateStr, pattern, AppRunEnvs.DefaultTimeZone);
    }

    /**
     * 解析日期字符串为日期格式
     *
     * @param dateStr  日期字符串
     * @param pattern  格式
     * @param timeZone 时区
     * @return 日期对象
     * @throws Exception 异常
     */
    public static Date parseDateStr(String dateStr, String pattern, TimeZone timeZone) throws Exception {
        if (StringUtil.isNullOrEmpty(dateStr) || timeZone == null) {
            throw new IllegalArgumentException("dateStr or timezone is null");
        }
        ThreadSafeFormatter sdf = new ThreadSafeFormatter(pattern, timeZone);
        return sdf.parse(dateStr);
    }

    /**
     * 获取当前年份
     *
     * @return 当前年份
     */
    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return 月份
     */
    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取day of the month
     *
     * @return day    eg the first day of month is 1
     */
    public static int getDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 某天后的日期
     *
     * @param dur 天数
     * @return 日期
     */
    public static Date add(int dur) {
        return add(new Date(), dur);
    }

    /**
     * 具体日期某一天后的日期
     *
     * @param startDate 指定日期
     * @param dur       某天
     * @return 日期对象
     */
    public static Date add(Date startDate, int dur) {
        return add(startDate, Calendar.DATE, dur);
    }

    /**
     * 具体日期以后的日期
     *
     * @param startDate 起始日期
     * @param filed     域
     * @param dur       间隔
     * @return 日期对象
     */
    public static Date add(Date startDate, int filed, int dur) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(filed, dur);
        return calendar.getTime();
    }

    /**
     * 增加分钟
     *
     * @param minutes 分钟间隔
     * @return 日期
     */
    public static Date addMinutes(int minutes) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    /**
     * 计算两个日期相差天数
     * date1 - date2
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return date2-date1天数
     */
    public static int between(Date date1, Date date2) {
        Date date1Temp = getMidnight(date1);
        Date date2Temp = getMidnight(date2);
        return (int) ((date2Temp.getTime() - date1Temp.getTime()) / TimeConstants.DAY_IN_MILLISECONDS);
    }

    /**
     * 获取星期几
     *
     * @return 返回周1-7
     */
    public static int getDayOfWeek() {
        Calendar calendar = GregorianCalendar.getInstance();
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        if (day_of_week == Calendar.SUNDAY) {
            return 7;
        } else {
            return day_of_week - 1;
        }
    }

}
