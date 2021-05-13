package com.cc.sdk2.jsdk.commons.utils;

import com.cc.sdk2.jsdk.base.BaseConstant;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * All rights reserved, copyright@cc.hu
 * 字符串工具类
 *
 * @author cc
 * @version 1.0
 * @date 2020/4/27 23:21
 **/
public final class StringUtil {
    /**
     * a str is null or empty
     *
     * @param str
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || "".equals(str) || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        int slen = str.length();
        for (int i = 0; i < slen; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

//    private static final Pattern HTTP_PROTOCOL_PATTERN = Pattern.compile("^(http:|https:|//).*");

    /**
     * 拼接host，提取baseUri中的host拼接到url上
     *
     * @param baseUri 带协议的前缀
     * @param url     相对
     * @return
     */
    public static String appendHost(String baseUri, String url) {
        if (isNullOrEmpty(baseUri)) {
            return null;
        }
        if (url.charAt(0) == '/') {
            return getHost(url) + url;
        }
        return getHost(baseUri) + "/" + url;
    }

    /**
     * 获取host的正则
     */
    private static Pattern HOST_PATTERN = Pattern.compile("((.*//)?[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)");

    /**
     * 获取url中的host
     *
     * @param url url
     * @return host地址
     */
    public static String getHost(String url) {
        String tUrl = url.indexOf("?") > 0 ? url.substring(0, url.indexOf("?")) : url;
        Matcher m = HOST_PATTERN.matcher(tUrl);
        if (m.find()) {
            return m.group(0);
        }
        return "";
    }

    /**
     * 去除sql中的'
     *
     * @param str
     * @return
     */
    public static String escapeSQL(String str) {
        if (isNullOrEmpty(str)) {
            return null;
        }
        return str.replaceAll("'", "''");
    }

    /**
     * 去除xml中特殊字符
     *
     * @param str
     * @return
     */
    public static String escapeXMLTags(String str) {
        return str.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;")
                .replaceAll("'", "&apos;")
                .replaceAll("\"", "&quot;")
                ;
    }

    /**
     * url参数对正则
     */
    private static final Pattern URL_PARAMS_PATTERN = Pattern.compile("(([\\w_$]+)=([\\w_$]+))");

    /**
     * 获取url query params
     *
     * @param queryStr query 字符串
     * @return 参数的键值对
     */
    public static Map<String, String> getUrlParams(String queryStr) {
        Map<String, String> kvMap = new HashMap<String, String>(10);
        if (!isNullOrEmpty(queryStr)) {
            String params = queryStr.contains("?") ? queryStr.substring(queryStr.indexOf("?")) : queryStr;
            Matcher m = URL_PARAMS_PATTERN.matcher(params);
            while (m.find()) {
                String k = m.group(2);
                String v = m.group(3);
                if (!isNullOrEmpty(k)) {
                    kvMap.put(k, isNullOrEmpty(v) ? "" : v);
                }
            }
        }
        return kvMap;
    }

    /**
     * 随机产生一个字母
     *
     * @param type 0 随机  1  大写   2  消息
     * @return 随机产生一个字母
     */
    public static char generateRandomLetter(int type) {
        int charCode = 0;
        Random random = new Random();
        if (type == 0) {
            int rndInt = random.nextInt(52);
            if (rndInt >= 26) {
                //大写字母
                charCode = rndInt - 26 + 97;
            } else {
                charCode = rndInt + 65;
            }
        }
        if (type == 1) {
            charCode = random.nextInt(26) + 65;
        }
        if (type == 2) {
            charCode = random.nextInt(26) + 97;
        }
        return (char) charCode;
    }

    public static Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");
    public static String getChineseCharacters(String str) {
        Matcher matcher = CHINESE_PATTERN.matcher(str);
        StringBuilder buf = new StringBuilder();
        while (matcher.find()) {
            buf.append(matcher.group());
        }
        return buf.toString();
    }



    /**
     * @return 随机产生中文字符
     */
    public static String generateRandomChineseCharacter() throws UnsupportedEncodingException {
        /**
         * 汉字是有一个全国统一的代码的，标准代号为GB2312-80，又称国标码。它规定了所有的国标汉字和符号分配在一个94行和94列的方阵里面，方阵的每一行称为一个区，01区到94区；每一列称为一个位，01位到94位。每一个汉字的区码和位码组合的4个阿拉伯数字就是它们的“区位码”（可以用他们唯一确定一个汉字）。
         * 与汉字区位码类似的还有汉字机内码，汉字的机内码是在汉字的区位码的区码和位码上分别加上A0H（这里的H表示前两位数字为16进制数）得到的。使用机内码表示汉字占用2个字节，分别称为高位字节和地位字节，
         * 高位字节=区码+A0H;
         * 低位字节=位码+A0H;
         * 如区位码为1601， 转换为16进制为：1001；
         * 机内码的高位字节：10 + A0 = B0H，低位字节为: 01+ A0=A1H，机内码为：B0A1H。
         */
        Random random = new Random();
        //--------------生成第一位区码
        StringBuilder buf = new StringBuilder();
        //生成11-14随机数
        int r1 = random.nextInt(3) + 11;
        buf.append(BaseConstant.HEX_DIGITAL[r1]);
        //生成第二位区码
        int r2;
        if (r1 == 13) {
            r2 = random.nextInt(10);
        } else {
            r2 = random.nextInt(16);
        }
        buf.append(BaseConstant.HEX_DIGITAL[r2]);
        byte firstByte = (byte) Integer.parseInt(buf.toString(), 16);

        buf = new StringBuilder();
        //生成第一位位码
        int r3 = random.nextInt(6) + 10;
        buf.append(BaseConstant.HEX_DIGITAL[r3]);
        //生成第二位位码
        int r4;
        if (r3 == 10) {
            r4 = random.nextInt(14) + 2;
        } else {
            r4 = random.nextInt(16);
        }
        buf.append(BaseConstant.HEX_DIGITAL[r4]);
        byte secondByte = (byte) Integer.parseInt(buf.toString(), 16);
        //将生成的区码放入第一个元素，位码放入第二个元素
        //根据字节生成汉
        return new String(new byte[]{firstByte, secondByte}, "GB2312");
    }

    /**
     * 把一个字符串转换成list
     *
     * @param str
     * @param separator
     * @return
     */
    public static List<String> str2List(String str, String separator) {
        if (isNullOrEmpty(str)) {
            return new ArrayList<>();
        }
        return Arrays.asList(str.split(separator));
    }

    /**
     * 字符串格式化输出
     *
     * @param str
     * @param params
     * @return
     */
    public static String strFormat(String str, Object... params) {
        if (str == null || "".equals(str)) {
            return str;
        }
        return String.format(str, params);
    }

    private static final Pattern ASCII_CHARACTER_PATTERN = Pattern.compile("[\\x00-\\ff]");

    private static final Pattern NOT_ASCII_CHARACTER_PATTERN = Pattern.compile("[^\\x00-\\xff\\w]");
    /**
     * 一条短消息最多1120位，否则会被拆分两条短信
     */
    private static final int ONE_SMS_BIT_TOTAL = 1120;

    /**
     * 一条短息字数计算
     *
     * @param str
     * @return
     */
    public static int countSmsSplitLength(String str) {
        int bitCouter = 0;
        Matcher matcher = ASCII_CHARACTER_PATTERN.matcher(str);
        while (matcher.find()) {
            bitCouter += 7;
        }
        matcher = NOT_ASCII_CHARACTER_PATTERN.matcher(str);
        while (matcher.find()) {
            bitCouter += 2 * 8;
        }
        int len = bitCouter / ONE_SMS_BIT_TOTAL;
        if (bitCouter % ONE_SMS_BIT_TOTAL == 0) {
            return len;
        } else {
            return len + 1;
        }

    }

    public static String left(String str, int len) {
        if (str == null) {
            return null;
        } else if (len < 0) {
            return "";
        } else {
            return str.length() < len ? str : str.substring(0, len);
        }
    }

    public static String right(String str, int len) {
        if (str == null) {
            return null;
        } else if (len < 0) {
            return "";
        } else {
            return str.length() < len ? str : str.substring(str.length() - len);
        }
    }

    public static String replace(String str, String searchStr, String replacement, int max) {
        if (isNullOrEmpty(str) || isNullOrEmpty(searchStr) || replacement == null || max == 0) {
            return str;
        }
        int end = str.indexOf(searchStr);
        if (end == -1) {
            return str;
        }
        int replLen = searchStr.length();
        int start = 0;
        StringBuffer buffer;
        for (buffer = new StringBuffer(); end != -1; end = str.indexOf(searchStr, start)) {
            buffer.append(str, start, end).append(replacement);
            start = end + replLen;
            --max;
            if (max == 0) {
                break;
            }
        }
        //拼接剩余字符串
        buffer.append(str.substring(start));
        return buffer.toString();
    }

    public static String replaceOne(String str, String searchStr, String replacement) {
        return replace(str, searchStr, replacement, 1);
    }

    public static String replaceAll(String str, String searchStr, String replacement) {
        return replace(str, searchStr, replacement, -1);
    }

    public static String remove(String str, String remove) {
        if (isNullOrEmpty(str) || isNullOrEmpty(remove)) {
            return str;
        }
        return replaceAll(str, remove, "");
    }

    public static String remove(String str, char remove) {
        if (isNullOrEmpty(str) || str.indexOf(remove) == -1) {
            return str;
        }
        char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);

    }

}
