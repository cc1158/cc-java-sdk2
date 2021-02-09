package com.cc.sdk2.jsdk.commons.encrypt;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * jdk实现对称加密算法
 * @author sen.hu
 */
abstract class JdkSymmetricEncrypt {

    /**
     * 使用jdk封装的加密算法
     *
     * @param algorithm     算法名称
     * @param cipherMode    CipherMode枚举值
     * @param secretKey     密码
     * @param ivParameterSpec       偏移量
     * @param dataSource    数据块
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    protected byte[] doCipher(String algorithm, int cipherMode, SecretKey secretKey, IvParameterSpec ivParameterSpec, byte[] dataSource) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        if (ivParameterSpec == null) {
            cipher.init(cipherMode, secretKey, new SecureRandom());
        } else {
            cipher.init(cipherMode, secretKey, ivParameterSpec, new SecureRandom());
        }
        return cipher.doFinal(dataSource);
    }

    /**
     * 校验密码的长度等
     * @param password
     */
    abstract void checkPassword(byte[] password) throws RuntimeException;

    /**
     * 加密
     * @param password      密钥
     * @param clearData     明文
     * @return
     */
    public abstract byte[] encrypt(byte[] password, byte[] clearData);

    /**
     * 解密
     * @param password      密钥
     * @param cipherData    加密文字
     * @return
     */
    public abstract byte[] decrypt(byte[] password, byte[] cipherData);

}
