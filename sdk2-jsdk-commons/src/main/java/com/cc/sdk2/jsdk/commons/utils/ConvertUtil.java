package com.cc.sdk2.jsdk.commons.utils;

import com.cc.sdk2.jsdk.base.BaseConstant;

import java.math.BigDecimal;
import java.util.Base64;

/**
 * All rights reserved, copyright@cc.hu
 * 转换工具类
 * @author cc
 * @version 1.0
 * @date 2021/2/8 20:34
 **/
public class ConvertUtil {

    /**
     * java原生base64编码
     */
    private static final Base64.Decoder decoder;
    private static final Base64.Encoder encoder;

    static {
        decoder = Base64.getDecoder();
        encoder = Base64.getEncoder();
    }


    /**
     * base64编码
     *
     * @param data 原文字节数组
     * @return 编码后的
     */
    public static String base64Encode(byte[] data) {
        return encoder.encodeToString(data);
    }

    /**
     * base64解码
     *
     * @param data 编码字节数组
     * @return 解码后的字节数组
     */
    public static byte[] base64Decode(byte[] data) {
        return decoder.decode(data);
    }

    /**
     * 将long转为无符号数
     *
     * @param value
     * @return
     */
    public static BigDecimal long2Unsigned(long value) {
        if (value >= 0) {
            return new BigDecimal(value);
        }
        long lowValue = value & 0x7fffffffffffffffL;
        return BigDecimal.valueOf(lowValue).add(BigDecimal.valueOf(Long.MAX_VALUE)).add(BigDecimal.valueOf(1));
    }

    /**
     * 字节数组转为16进制显示
     *
     * @param bytes
     * @return
     */
    public static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder();

        for (byte b : bytes) {
            int bit = (b & 0xF0) >> 4;
            //大端（高位）
            ret.append(BaseConstant.HEX_DIGITAL[bit]);
            bit = (b & 0x0F);
            //小端（低位）
            ret.append(BaseConstant.HEX_DIGITAL[bit]);
        }
        return ret.toString();
    }

    /**
     * 解析16进制字节数组
     *
     * @param hexBytes 16进制字节数组
     * @return 原始编码字节
     */
    public static byte[] parseHex(byte[] hexBytes) {
        byte[] source = new byte[hexBytes.length / 2];
        for (int i = 0; i < hexBytes.length; ) {
            String item = new String(hexBytes, i, 2);
            source[i / 2] = (byte) Integer.parseInt(item, 16);
            i += 2;
        }
        return source;
    }

}
