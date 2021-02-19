package com.cc.sdk2.test.jsdk.commons;

import org.junit.Test;
import com.cc.sdk2.jsdk.commons.date.DateUtil;

import java.util.Date;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/2/9 10:02
 **/
public class TestDateUtil {
    @Test
    public void testDateUtil() {
        System.out.println(DateUtil.formatDate(new Date(), "yyyy-MM-dd'T'HH:mm:ss.SSS", DateUtil.getUTCTimeZone()));
        try {
            System.out.println(DateUtil.parseDateStr("2019-10-30T07:47:05.889", "yyyy-MM-dd'T'HH:mm:ss.SSS", DateUtil.getUTCTimeZone()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFormat() {
        System.out.println(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SZ"));
    }
}
