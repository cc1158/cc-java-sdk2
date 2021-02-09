package com.cc.sdk2.jsdk.commons.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 加密工具类
 * 密钥长度：128bit  16字节    192 bit  24 字节   256 bit 32字节
 *
 * @author sen.hu
 */
public class AESEncrypt extends JdkSymmetricEncrypt {


    @Override
    public byte[] encrypt(byte[] password, byte[] clearData) {
        return ecbEncrypt(password, null, clearData);
    }

    @Override
    public byte[] decrypt(byte[] password, byte[] cipherData) {
        return ecbDecrypt(password, null, cipherData);
    }

    protected byte[] aesCipher(String algorithm, int encryptMode, byte[] password, byte[] iv, byte[] sourceData) {
        checkPassword(password);
        SecretKeySpec secretKeySpec = new SecretKeySpec(password, "AES");
        try {
            IvParameterSpec ivParameterSpec = (iv == null) ? null : new IvParameterSpec(iv);
            return doCipher(algorithm, encryptMode, secretKeySpec, ivParameterSpec, sourceData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
     protected void checkPassword(byte[] password) throws RuntimeException {
        if (password.length != 16 && password.length != 24 && password.length != 32) {
            throw new RuntimeException("密码长度不合要求");
            //如果密钥长度不是128bit 192bit 256bit则使用随机数生成, 128位秘钥

//            KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptBase.EncryptMethod.AES.name());
//            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//            secureRandom.setSeed(password);
//            keyGenerator.init(DEFAULT_KEY_LENGTH, secureRandom);
//            byte[] genRawPasswords = keyGenerator.generateKey().getEncoded();
//            return new SecretKeySpec(genRawPasswords, EncryptBase.EncryptMethod.AES.name());
        }
    }

    public byte[] ecbEncrypt(byte[] password, byte[] iv, byte[] clearData) {
        return aesCipher("AES/ECB/PKCS5Padding", Cipher.ENCRYPT_MODE, password, iv, clearData);
    }

    public byte[] ecbDecrypt(byte[] password, byte[] iv, byte[] cipherData) {
        return aesCipher("AES/ECB/PKCS5Padding", Cipher.DECRYPT_MODE, password, iv, cipherData);
    }

    public byte[] cbcEncrypt(byte[] password, byte[] iv, byte[] clearData) {
        return aesCipher("AES/CBC/PKCS5Padding", Cipher.ENCRYPT_MODE, password, iv, clearData);
    }

    public byte[] cbcDecrypt(byte[] password, byte[] iv, byte[] cipherData) {
        return aesCipher("AES/CBC/PKCS5Padding", Cipher.DECRYPT_MODE, password, iv, cipherData);
    }

    public byte[] cfbEncrypt(byte[] password, byte[] iv, byte[] clearData) {
        return aesCipher("AES/CFB/PKCS5Padding", Cipher.ENCRYPT_MODE, password, iv, clearData);
    }

    public byte[] cfbDecrypt(byte[] password, byte[] iv, byte[] cipherData) {
        return aesCipher("AES/CFB/PKCS5Padding", Cipher.DECRYPT_MODE, password, iv, cipherData);
    }


}
