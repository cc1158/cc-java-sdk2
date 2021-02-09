package com.cc.sdk2.jsdk.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * All rights reserved, copyright@cc.hu
 * 输入输出工具类
 * @author cc
 * @version 1.0
 * @date 2021/2/8 20:13
 **/
public class IoUtil {
    /**
     * 基本输出去操作
     *
     * @param out   输出流
     * @param bytes 内容字节
     * @throws IOException io异常
     */
    public static void write(OutputStream out, byte[] bytes) throws IOException {
        if (out == null || bytes == null || bytes.length == 0) {
            return;
        }
        if (bytes.length <= 1024) {
            out.write(bytes);
            return;
        }
        int remainder = bytes.length % 1024;
        int times = remainder == 0 ? bytes.length / 1024 : bytes.length / 1024 + 1;
        for (int i = 0, off = 0; i < times; i++) {
            if (remainder != 0 && i == times - 1) {
                out.write(bytes, off, remainder);
            } else {
                out.write(bytes, off, 1024);
            }
            off += 1024;
        }
    }

    /**
     * 基本输入操作
     *
     * @param in 输入流
     * @return 读取内容
     * @throws IOException io异常
     */
    public static byte[] read(InputStream in) throws IOException {
        if (in == null) {
            return null;
        }
        byte[] ret = new byte[0];
        int readNum;
        int destP;
        byte[] buff = new byte[1024];
        while ((readNum = in.read(buff)) != -1) {
            destP = ret.length;
            ret = Arrays.copyOf(ret, destP + readNum);
            System.arraycopy(buff, 0, ret, destP, readNum);
        }
        return ret;
    }
}
