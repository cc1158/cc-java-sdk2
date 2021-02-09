package com.cc.sdk2.jsdk.commons.encrypt;

import com.cc.sdk2.jsdk.commons.EncryptConst;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * DES 加密工具类
 * 密钥长度必须是 64 bit 8字节
 * 3DES  密钥长度：192 bit  24字节
 *
 * http://aes.online-domain-tools.com/   在线工具类
 * http://tool.chacuo.net/crypt3des
 * http://tool.oschina.net/encrypt   crypto-js  padding方式未确定  加密解密不一样
 *
 * @author sen.hu
 */
public class DESEncrypt extends JdkSymmetricEncrypt {
    @Override
    public byte[] encrypt(byte[] password, byte[] clearData) {
        return ecbEncrypt(password, null, clearData);
    }

    @Override
    public byte[] decrypt(byte[] password, byte[] cipherData) {
        return ecbDecrypt(password, null, cipherData);
    }

    @Override
    protected void checkPassword(byte[] password) throws RuntimeException {
        if (password.length < 8) {
            throw new RuntimeException("密码长度不正确");
        }
    }

    protected byte[] desCipher(String instanceName, String algorithm, int encryptMode, byte[] password, byte[] iv, byte[] sourceData) {
        checkPassword(password);
        try {
            DESKeySpec desKeySpec = new DESKeySpec(password);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(instanceName);
            SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
            IvParameterSpec ivParameterSpec = (iv == null) ? null : new IvParameterSpec(iv);
            return doCipher(algorithm,encryptMode, secretKey, ivParameterSpec, sourceData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] ecbEncrypt(byte[] password, byte[] iv, byte[] clearData) {
        return desCipher(EncryptConst.DES, EncryptConst.DES + "/ECB/PKCS5Padding", Cipher.ENCRYPT_MODE, password, iv, clearData);
    }

    public byte[] ecbDecrypt(byte[] password, byte[] iv, byte[] cipherData) {
        return desCipher(EncryptConst.DES, EncryptConst.DES + "/ECB/PKCS5Padding", Cipher.DECRYPT_MODE, password, iv, cipherData);
    }

    public byte[] cbcEncrypt(byte[] password, byte[] iv, byte[] clearData) {
        return desCipher(EncryptConst.DES, EncryptConst.DES + "/CBC/PKCS5Padding", Cipher.ENCRYPT_MODE, password, iv, clearData);
    }

    public byte[] cbcDecrypt(byte[] password, byte[] iv, byte[] cipherData) {
        return desCipher(EncryptConst.DES, EncryptConst.DES + "/CBC/PKCS5Padding", Cipher.DECRYPT_MODE, password, iv, cipherData);
    }

    public byte[] cfbEncrypt(byte[] password, byte[] iv, byte[] clearData) {
        return desCipher(EncryptConst.DES, EncryptConst.DES + "/CFB/PKCS5Padding", Cipher.ENCRYPT_MODE, password, iv, clearData);
    }

    public byte[] cfbDecrypt(byte[] password, byte[] iv, byte[] cipherData) {
        return desCipher(EncryptConst.DES, EncryptConst.DES + "/CFB/PKCS5Padding", Cipher.DECRYPT_MODE, password, iv, cipherData);
    }

    /**
     * 3des 加密
     * @param password
     * @param clearData
     * @return
     */
    public byte[] tripleDesEncrypt(byte[] password, byte[] clearData) {
        return tripleDesEcbEncrypt(password, null, clearData);
    }

    /**
     * 3des 解密
     * @param password
     * @param cipherData
     * @return
     */
    public byte[] tripleDesDecrypt(byte[] password, byte[] cipherData) {
        return tripleDesEcbDecrypt(password, null, cipherData);
    }

    public byte[] tripleDesEcbEncrypt(byte[] password, byte[] iv, byte[] clearData) {
        return desCipher(EncryptConst.TRIPLE_DES, EncryptConst.TRIPLE_DES + "/ECB/PKCS5Padding", Cipher.ENCRYPT_MODE, password, iv, clearData);
    }

    public byte[] tripleDesEcbDecrypt(byte[] password, byte[] iv, byte[] cipherData) {
        return desCipher(EncryptConst.TRIPLE_DES, EncryptConst.TRIPLE_DES + "/ECB/PKCS5Padding", Cipher.DECRYPT_MODE, password, iv, cipherData);
    }

    public byte[] tripleDesCbcEncrypt(byte[] password, byte[] iv, byte[] clearData) {
        return desCipher(EncryptConst.TRIPLE_DES, EncryptConst.TRIPLE_DES + "/CBC/PKCS5Padding", Cipher.ENCRYPT_MODE, password, iv, clearData);
    }

    public byte[] tripleDesCbcDecrypt(byte[] password, byte[] iv, byte[] cipherData) {
        return desCipher(EncryptConst.TRIPLE_DES, EncryptConst.TRIPLE_DES + "/CBC/PKCS5Padding", Cipher.DECRYPT_MODE, password, iv, cipherData);
    }

    public byte[] tripleDesCfbEncrypt(byte[] password, byte[] iv, byte[] clearData) {
        return desCipher(EncryptConst.TRIPLE_DES, EncryptConst.TRIPLE_DES + "/CFB/PKCS5Padding", Cipher.ENCRYPT_MODE, password, iv, clearData);
    }

    public byte[] tripleDesCfbDecrypt(byte[] password, byte[] iv, byte[] cipherData) {
        return desCipher(EncryptConst.TRIPLE_DES, EncryptConst.TRIPLE_DES + "/CFB/PKCS5Padding", Cipher.DECRYPT_MODE, password, iv, cipherData);
    }


}
