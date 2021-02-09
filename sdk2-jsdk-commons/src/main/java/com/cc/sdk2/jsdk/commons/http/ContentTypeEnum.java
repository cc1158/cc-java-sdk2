package com.cc.sdk2.jsdk.commons.http;

public enum ContentTypeEnum {
    /**
     * 网页
     */
    TEXT_HTML("text/html", "默认"),
    /**
     * json数据
     */
    APPLICATION_JSON("application/json", "json数据类型"),
    /**
     * form标段
     */
    APPLICATION_FORM_URL_ENCODE("application/x-www-form-urlencoded", "form表单"),
    /**
     * 多类型表单 可上传文件
     */
    MULTIPART_FORM("multipart/form-data; boundary=", "多类型表单，上传文件")
    ;
    public final String value;
    public final String desc;

    ContentTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
