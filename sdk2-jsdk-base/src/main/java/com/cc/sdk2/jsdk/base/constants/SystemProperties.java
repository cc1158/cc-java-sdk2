package com.cc.sdk2.jsdk.base.constants;

/**
 * All rights reserved, copyright@cc.hu
 *  java运行环境枚举
 * @author cc
 * @version 1.0
 **/
public enum SystemProperties {

    /**
     * the curr os name
     */
    OS_NAME("os.name", "当前系统名"),
    /**
     * line separator
     */
    LINE_SEPARATOR("line.separator", "当前换行分隔符"),
    /**
     * file encoding
     */
    FILE_ENCODING("file.encoding", "文件编码"),
    /**
     * file separator
     */
    FILE_SEPARATOR("file.separator", "文件分隔符"),
    /**
     * the set time zone
     */
    USER_TIMEZONE("user.timezone", "当前time zone"),
    /**
     * path separator
     */
    PATH_SEPARATOR("path.separator", "路径分隔符"),
    ;

    public final String key;
    public final String desc;

    SystemProperties(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }


}
