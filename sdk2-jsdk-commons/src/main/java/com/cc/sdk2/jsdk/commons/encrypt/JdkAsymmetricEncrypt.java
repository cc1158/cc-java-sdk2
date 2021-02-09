package com.cc.sdk2.jsdk.commons.encrypt;

import com.cc.sdk2.jsdk.commons.EncryptConst;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * jdk实现的非对称加密
 *
 * @author sen.hu
 */
abstract class JdkAsymmetricEncrypt {


    /**
     * 默认使用RSA算法生成非对称加密
     *
     * @param keySize
     * @return
     * @throws NoSuchAlgorithmException
     */
    public KeyPair genAsymmetricKey(int keySize) throws NoSuchAlgorithmException {
        return genAsymmetricKey(keySize, EncryptConst.RSA);
    }

    /**
     * 使用指定算法生成非对称加密密钥
     *
     * @param keySize
     * @param keyAlgorithm
     * @return
     * @throws NoSuchAlgorithmException
     */
    public KeyPair genAsymmetricKey(int keySize, String keyAlgorithm) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyAlgorithm);
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    /**
     * 私钥生成key对象
     *
     * @param privateKeyBytes 私钥字节数组
     * @param keyAlgorithm    私钥生成算法
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PrivateKey genPrivateKey(byte[] privateKeyBytes, String keyAlgorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        return privateKey;
    }

    /**
     * 公钥生成key对象
     *
     * @param publicKeyBytes 公钥字节数组
     * @param keyAlgorithm   公钥算法
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PublicKey genPublicKey(byte[] publicKeyBytes, String keyAlgorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        return publicKey;
    }


}
