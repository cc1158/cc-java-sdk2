package com.cc.sdk2.test.jsdk.commons;

import com.cc.sdk2.jsdk.commons.encrypt.AESEncrypt;
import com.cc.sdk2.jsdk.commons.encrypt.DESEncrypt;
import com.cc.sdk2.jsdk.commons.encrypt.RSAEncrypt;
import com.cc.sdk2.jsdk.commons.hash.Hash;
import com.cc.sdk2.jsdk.commons.hash.Md5Hash;
import com.cc.sdk2.jsdk.commons.hash.Sha1Hash;
import com.cc.sdk2.jsdk.commons.hash.Sha2Hash;
import com.cc.sdk2.jsdk.commons.utils.ConvertUtil;
import org.junit.Test;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/2/9 10:18
 **/
public class TestEncrypt {
    @Test
    public void testHash() {
        String content = "test";
        Md5Hash md5 = new Md5Hash();
        Hash sha1 = new Sha1Hash();
        Hash sha2 = new Sha2Hash();
        System.out.println("test md5=" + md5.hash(content));
        System.out.println("test md5 16位=" + md5.get16md5(content));
        System.out.println("test sha1=" + sha1.hash(content));
        System.out.println("test sha2=" + sha2.hash(content));
    }

    @Test
    public void testAesEncrypt() {
        String password = "test123111111111";
        String clearWord = "hello world， beautiful";
        AESEncrypt aesEncrypt = new AESEncrypt();
        byte[] enData = aesEncrypt.encrypt(password.getBytes(), clearWord.getBytes());
        System.out.println(ConvertUtil.toHex(enData));
        byte[] deData = aesEncrypt.decrypt(password.getBytes(), enData);
        System.out.println(new String(deData));
    }

    @Test
    public void testDesEncrypt() {
        String password = "test123111111111";
        String clearWord = "hello world， beautiful";
        DESEncrypt desEncrypt = new DESEncrypt();
        byte[] enData = desEncrypt.encrypt(password.getBytes(), clearWord.getBytes());
        System.out.println(ConvertUtil.toHex(enData));
        byte[] deData = desEncrypt.decrypt(password.getBytes(), enData);
        System.out.println(new String(deData));
    }

    @Test
    public void testRsaEncrypt() {
        String clearWord = "hello world， beautiful";
        RSAEncrypt rsaEncrypt = new RSAEncrypt();
        Map<String, Object> keyPair = rsaEncrypt.genRSAKeyPair();
        byte[] enData = rsaEncrypt.encrypt((Key) keyPair.get(RSAEncrypt.PRIVATE_KEY_NAME), clearWord.getBytes());
        byte[] sigData = rsaEncrypt.genSignature((PrivateKey) keyPair.get(RSAEncrypt.PRIVATE_KEY_NAME), clearWord.getBytes());
        boolean checkFlag = rsaEncrypt.verifySignature((PublicKey) keyPair.get(RSAEncrypt.PUBLIC_KEY_NAME), clearWord.getBytes(), sigData);
        System.out.println(checkFlag);
        byte[] deData = rsaEncrypt.decrypt((PublicKey) keyPair.get(RSAEncrypt.PUBLIC_KEY_NAME), enData);
        System.out.println(new String(deData));

    }
}
