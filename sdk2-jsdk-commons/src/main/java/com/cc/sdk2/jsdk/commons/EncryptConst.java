package com.cc.sdk2.jsdk.commons;

/**
 * 加密常量类
 * @author sen.hu
 */
public final class EncryptConst {
    /**
     * md5
     */
    public static final String MD5 = "MD5";
    /**
     * sha1
     */
    public static final String SHA1 = "SHA1";
    /**
     * sha2
     */
    public static final String SHA256 = "SHA-256";
    /**
     * aes 方法名
     */
    public static final String AES = "AES";
    /**
     * des方法名
     */
    public static final String DES = "DES";
    /**
     * 3des方法名
     */
    public static final String TRIPLE_DES = "DESede";
    /**
     * rsa 方法名
     */
    public static final String RSA = "RSA";
    /**
     * rsa 默认签名方法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    //==============================加密模式==============================
    /**
     * 加密模式 ecb
     */
    public static final String ECB_MODE = "ECB";
    /**
     * 机密模式CBC
     */
    public static final String CBC_MODE = "CBC";
    /**
     * 加密模式CFB
     */
    public static final String CFB_MODE = "CFB";


    //=========================填充方式===============================

    /**
     *
     * 无填充方式
     */
    public static final String PADDING_WAY_NO_PADDING = "NoPadding";
    /**
     * PKCS5填充方式
     */
    public static final String PADDING_WAY_PKCS5_PADDING = "PKCS5Padding";




}
