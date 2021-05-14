package com.cc.sdk2.jsdk.commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * All rights reserved, copyright@cc.hu
 * 常用的正则表达式
 * @author cc
 * @version 1.0
 * @date 2021/2/8 20:28
 **/
public enum RegexPatterns {
    /**
     * 手机号码验证
     */
    Mobile("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$", "手机号码"),
    /**
     * 固话验证
     */
    Telephone("^(\\(\\d{3,4}-)|\\d{3.4}-)?\\d{7,8}$", "固话号码"),
    /**
     * 邮件地址校验
     */
    Email("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", "电子邮件校验"),
    /**
     * 邮政编码
     */
    ZipCode_CN("[1-9]\\d{5}(?!\\d)", "中国邮政编码"),
    /**
     * 日期格式
     */
    Date_Format("^\\d{4}-0?[1-9]|1[0-2]-(0?[1-9])|((1|2)[0-9])|30|31$", "yyyy-MM-dd日期格式"),
    /**
     * 月份格式校验
     */
    Month("^(0?[1-9]|1[0-2])$", "月份校验，格式：01，1"),
    /**
     * 天验证
     */
    Day("^((0?[1-9])|((1|2)[0-9])|30|31)$", "天验证，格式：01-09,31"),
    /**
     * url pattern
     */
    Url("[a-zA-z]+://[^\\s]*", "Internet url"),
    /**
     * 互联网url  http  https
     */
    HttpUrl("^(http|https)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$", "互联网url"),
    /**
     * 中文字符
     */
    ChineseCharacter("[\\u4e00-\\u9fa5]", "中文字符"),
    /**
     * 双字节，通常匹配字数
     */
    DoubleWord("[^\\x00-\\xff]", "双字节字符"),
    /**
     * html标签
     */
    HtmlTag("<(\\S*?)[^>]*>.*?</>|<.*?/>", "html标签匹配"),
    /**
     * ip地址
     */
    Ipv4("", "ipv4地址匹配"),
    /**
     * 空白字符 行
     */
    WhiteSpace("\\n\\s*\\r", "空白字符，行"),



    ;
    public final String pattern;
    public final String desc;

    RegexPatterns(String pattern, String desc) {
        this.pattern = pattern;
        this.desc = desc;
    }


}
