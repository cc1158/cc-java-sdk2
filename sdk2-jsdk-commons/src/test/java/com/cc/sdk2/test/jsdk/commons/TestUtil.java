package com.cc.sdk2.test.jsdk.commons;

import com.cc.sdk2.jsdk.commons.utils.FileUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/2/9 10:16
 **/
public class TestUtil {
    @Test
    public void testFileCopy() {
        try {
            FileUtil.copy("D:\\Documents\\person\\sen.hu\\桌面\\test.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileSplit() {
        try {
            FileUtil.split("D:\\Documents\\person\\sen.hu\\桌面\\test.txt", 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
