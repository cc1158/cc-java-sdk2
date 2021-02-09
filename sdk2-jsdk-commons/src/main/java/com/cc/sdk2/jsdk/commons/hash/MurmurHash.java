package com.cc.sdk2.jsdk.commons.hash;

import com.cc.sdk2.jsdk.commons.utils.ConvertUtil;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * MurmurHash一致性Hash算法JAVA版
 * 返回无符号long，string表示或字节数组
 *
 * @author sen.hu
 */
public class MurmurHash implements Hash {

    @Override
    public String hash(String content) {
        return new String(this.hash(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    @Override
    public byte[] hash(byte[] content) {
        return doUnsignedHash(content).toString().getBytes(StandardCharsets.UTF_8);
    }

    private BigDecimal doUnsignedHash(byte[] content) {
        return ConvertUtil.long2Unsigned(this.doHash(content));
    }

    private long doHash(byte[] content) {
        ByteBuffer buf = ByteBuffer.wrap(content);
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(
                    ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }

}
