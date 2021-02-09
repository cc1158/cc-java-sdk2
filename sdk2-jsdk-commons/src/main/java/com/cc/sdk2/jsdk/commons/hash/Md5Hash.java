package com.cc.sdk2.jsdk.commons.hash;

import com.cc.sdk2.jsdk.commons.EncryptConst;
import com.cc.sdk2.jsdk.commons.utils.ConvertUtil;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * md5算法实现
 * @author sen.hu
 */
public class Md5Hash extends JdkHash {
    @Override
    public String hash(String content) {
        if (content == null || "".equals(content)) {
            return null;
        }
        byte[] hash = hash(content.getBytes(StandardCharsets.UTF_8));
        return hash == null ? "" : ConvertUtil.toHex(hash);
    }

    public String get16md5(String content) {
        byte[] hash = hash(content.getBytes(StandardCharsets.UTF_8));
        return hash == null ? "" : ConvertUtil.toHex(hash).substring(8, 24);
    }

    @Override
    public byte[] hash(byte[] content) {
        if (content == null || content.length == 0) {
            return null;
        }
        try {
            return jdkHash(EncryptConst.MD5, content);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
