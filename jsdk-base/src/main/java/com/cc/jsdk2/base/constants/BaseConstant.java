package com.cc.jsdk2.base.constants;

/**
 * All rights reserved, copyright@cc.hu
 * 基础的constant
 * @author cc
 * @version 1.0
 **/
public final class BaseConstant {

    private BaseConstant() {
    }

    /**
     * log traceId 常量
     */
    public static final String TRACE_ID_LOGFILE_NAME = "traceId";
    /**
     * web传递header名称
     */
    public static final String TRACE_ID_LOGWEB_HEADER = "X-TraceId";
    /**
     * 十六进制字符数组
     */
    public static final char[] HEX_DIGITAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * 十六进制字节
     */
    public static final byte[] HEX_DIGITAL_BYTE = { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66};


    /**
     * 回车换行
     */
    public static final String CRLF = "\r\n";
    /**
     * 十进制字符数组
     */
    public static final char[] DECIMAL_DIGITAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 小写字母数组
     */
    public static final char[] LOWER_CASE_LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    /**
     * 大写字母数组
     */
    public static final char[] UPPER_CASE_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    /**
     * 数字
     * 小写
     * 大写
     */
    public static final char[] DECIAL_AND_LETTERS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };


    /**
     * UTF-8
     */
    public static final String CHARSET_NAME_UTF8 = "UTF-8";
    /**
     * GB2312
     */
    public static final String CHARSET_NAME_GB2312 = "GB2312";
    /**
     * GBK
     */
    public static final String CHARSET_NAME_GBK = "GBK";
    /**
     * iso8859-1
     */
    public static final String CHARSET_NAME_ISO8859_1 = "iso8859-1";
    /**
     * 中国大陆地区
     */
    public static final String[] CHINA_MAIN_LAND_PROVINCES = {
            "北京", "天津", "上海", "重庆", "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏",
            "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "海南",
            "四川", "贵州", "云南", "陕西", "甘肃", "青海", "内蒙古", "广西", "西藏", "宁夏",
            "新疆"
    };



}
