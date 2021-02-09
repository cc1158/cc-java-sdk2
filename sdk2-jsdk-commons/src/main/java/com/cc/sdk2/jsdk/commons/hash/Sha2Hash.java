package com.cc.sdk2.jsdk.commons.hash;

import com.cc.sdk2.jsdk.commons.EncryptConst;
import com.cc.sdk2.jsdk.commons.utils.ConvertUtil;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * sha2 算法实现
 * @author sen.hu
 */
public class Sha2Hash extends JdkHash {

    @Override
    public String hash(String content) {
        if (content == null || "".equals(content)) {
            return null;
        }
        byte[] hash = hash(content.getBytes(StandardCharsets.UTF_8));
        return null == hash ? "" : ConvertUtil.toHex(hash);
    }

    @Override
    public byte[] hash(byte[] content) {
        if (content == null || content.length == 0) {
            return null;
        }
        try {
            return jdkHash(EncryptConst.SHA256, content);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
