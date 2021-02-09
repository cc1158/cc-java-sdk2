package com.cc.sdk2.jsdk.commons.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 使用jdk自带的散列算法实现常见散列
 */
abstract class JdkHash implements Hash {


  /**
   * jdk自带的散列算法实现
   * @param hashAlgorithm
   * @param originData
   * @return
   * @throws NoSuchAlgorithmException
   */
  protected byte[] jdkHash(String hashAlgorithm, byte[] originData) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm);
    messageDigest.update(originData);
    byte[] hashes = messageDigest.digest();
    return hashes;
  }

}
