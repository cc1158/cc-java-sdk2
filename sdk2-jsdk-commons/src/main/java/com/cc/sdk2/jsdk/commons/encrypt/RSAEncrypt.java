package com.cc.sdk2.jsdk.commons.encrypt;

import com.cc.sdk2.jsdk.commons.EncryptConst;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密解密工具类
 * <p>
 * 因为使用128字节 1024位长度的key，所以默认的加密和解密的最大字节数为：
 * 默认的填充占  11 个字节
 * 所以加密块为：117字节
 * 解密块：128字节
 *
 * @author sen.hu
 */
public class RSAEncrypt extends JdkAsymmetricEncrypt {
    /**
     * 公钥 map key
     */
    public static final String PUBLIC_KEY_NAME = "RSAPublicKey";
    /**
     * 私钥  map key
     */
    public static final String PRIVATE_KEY_NAME = "RSAPrivateKey";

    /**
     * rsa 最大加密密文的块 117byte 128-11
     */
    private final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * rsa 最大解密密文大小
     */
    private final int MAX_DECRYPT_BLOCK = 128;
    /**
     * 128字节
     * 默认密钥大小
     */
    private final int DEFAULT_KEY_SIZE = 1024;

    /**
     * 生成签名
     * @param privateKey    私钥
     * @param clearData     明文
     * @return
     */
    public byte[] genSignature(PrivateKey privateKey, byte[] clearData) {
        try {
            Signature signature = Signature.getInstance(EncryptConst.SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(clearData);
            return signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 校验签名
     * @param publicKey     公钥
     * @param clearData   解密后的数据
     * @param signatureData     签名
     * @return
     */
    public boolean verifySignature(PublicKey publicKey, byte[] clearData, byte[] signatureData) {
        try {
            Signature signature = Signature.getInstance(EncryptConst.SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(clearData);
            return signature.verify(signatureData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 生成默认128byte（1024）位的密钥对
     * @return
     */
    public Map<String, Object> genRSAKeyPair() {
        return this.genRSAKeyPair(DEFAULT_KEY_SIZE);
    }

    /**
     * 生成rsa公钥  私钥
     * @param keySize   key大小
     * @return
     */
    public Map<String, Object> genRSAKeyPair(int keySize) {
        try {
            KeyPair keyPair = genAsymmetricKey(keySize, EncryptConst.RSA);
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, Object> keyMap = new HashMap<String, Object>();
            keyMap.put(PUBLIC_KEY_NAME, publicKey);
            keyMap.put(PRIVATE_KEY_NAME, privateKey);
            return keyMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     * @param rsaKey        公钥  或 私钥 rsa
     * @param clearData     明文
     * @return
     */
    public byte[] encrypt(Key rsaKey, byte[] clearData) {
        try {
            return doCipher(rsaKey, Cipher.ENCRYPT_MODE, clearData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param rsaKey    公钥  私钥
     * @param cipherData    密文
     * @return
     */
    public byte[] decrypt(Key rsaKey, byte[] cipherData) {
        try {
            return doCipher(rsaKey, Cipher.DECRYPT_MODE, cipherData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private byte[] doCipher(Key key, int cipherMode, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(EncryptConst.RSA);
        cipher.init(cipherMode, key);
        int dataLength = data.length;
        int maxCipherSize = 0;
        if (cipherMode == Cipher.ENCRYPT_MODE) {
            maxCipherSize = MAX_ENCRYPT_BLOCK;
        }
        if (cipherMode == Cipher.DECRYPT_MODE) {
            maxCipherSize = MAX_DECRYPT_BLOCK;
        }
        int offset = 0;
        byte[] buf;
        ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
        int diff = dataLength - offset;
        while (diff > 0) {
            //分段加密
            if (diff > maxCipherSize) {
                //如果大于rsa最大块
                buf = cipher.doFinal(data, offset, maxCipherSize);
            } else {
                buf = cipher.doFinal(data, offset, diff);
            }
            tempOut.write(buf, 0, buf.length);
            offset += maxCipherSize;
            diff = dataLength - offset;
        }
        byte[] ciphered = tempOut.toByteArray();
        try {
            tempOut.flush();
            tempOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ciphered;
    }




}
