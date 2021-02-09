package com.cc.sdk2.jsdk.commons.encrypt;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Description 基础加密类
 * @Author sen.hu
 * @Date 2018/10/22 13:56
 **/
public class EncryptBase {
    public enum EncryptMethod {
        MD5, DES, DESede, AES, SHA1, SHA2
    }

    public enum  EncryptMode {
        ECB, CBC, CFB,
    }

    public enum EncryptPaddingWay {
        NoPadding, PKCS5Padding,
    }

    /**
     * java 基础加密、解密方法
     * @param encryptMethod   加/解密算法名称
     * @param encryptMode      加/解密模式
     * @param encryptPaddingWay 加/解密填充方式
     * @param cipherMode    加密/解密
     * @param secretKey     密钥
     * @param ivParameterSpec   偏移量
     * @param dataSource    数据
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] doCipher(EncryptMethod encryptMethod, EncryptMode encryptMode, EncryptPaddingWay encryptPaddingWay, int cipherMode, SecretKey secretKey, IvParameterSpec ivParameterSpec, byte[] dataSource) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        String algorithm = encryptMethod.name() + "/" + encryptMode.name() + "/" + encryptPaddingWay.name();
        Cipher cipher = Cipher.getInstance(algorithm);
        if (ivParameterSpec == null) {
            cipher.init(cipherMode, secretKey, new SecureRandom());
        } else {
            cipher.init(cipherMode, secretKey, ivParameterSpec, new SecureRandom());
        }
        return cipher.doFinal(dataSource);
    }
}
