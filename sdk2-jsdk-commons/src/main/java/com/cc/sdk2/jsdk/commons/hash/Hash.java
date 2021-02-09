package com.cc.sdk2.jsdk.commons.hash;

/**
 * 散列算法接口
 */
public interface Hash {
    /**
     * 对字符串进行散列
     * @param content
     * @return  散列接口转为16进制表示
     */
    String hash(String content);

    /**
     * 对字节数组进行散列
     * @param content
     * @return  散列后的字节数组
     */
    byte[] hash(byte[] content);

}
