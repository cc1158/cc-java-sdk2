package com.cc.sdk2.jsdk.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 以日期为前缀产生20位UUID
 *
 * @author ccsen.hu@gmail.com
 */
public final class UUID {

    private UUID(){}


    private static AtomicLong serial = new AtomicLong(0);

    private static class LazyHolder {
        private static final UUID uuid = new UUID();
    }

    private String genUUId(String prefix, String pattern, int genLength) {
        StringBuilder buf = new StringBuilder(prefix);
        //日期str
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateStr = sdf.format(new Date());
        buf.append(dateStr);
        if (serial.get() > 9999) {
            serial .set(0);
        }
        //序列号
        String serialStr = String.valueOf(fill(String.valueOf(serial.incrementAndGet()), '0', 4, true));
        buf.append(serialStr);
        //
        int rndLen = genLength - dateStr.length() - serialStr.length();
        if (rndLen > 0) {
            //计算随机串长度
            int pow ;
            while (true) {
                pow = new Random().nextInt(rndLen);
                if (pow > 0) {
                    break;
                }
            }
            int randNum = (int) (Math.random() * Math.pow(10, pow));
            //随机串
            String randStr = String.valueOf(randomFill(String.valueOf(randNum), rndLen, false));
            buf.append(randStr);
        }
        return buf.toString();
    }

    private char[] fill(String original, char filledChar, int length, boolean prefixFilled) {
        char[] originalChars = original.toCharArray();
        if (originalChars.length >= length) {
            return originalChars;
        }
        char[] ret = new char[length];
        int minus = length - originalChars.length;
        if (prefixFilled) {
            //前缀填充
            System.arraycopy(originalChars, 0, ret, minus, originalChars.length);
            for (int i = 0; i < minus; i++) {
                ret[i] = filledChar;
            }
        } else {
            //作为后缀填充
            System.arraycopy(originalChars, 0, ret, 0, originalChars.length);
            for (int i = originalChars.length; i < length; i++) {
                ret[i] = filledChar;
            }
        }
        return ret;
    }

    private char[] randomFill(String original, int length, boolean prefixFilled) {
        char[] originalChars = original.toCharArray();
        if (originalChars.length >= length) {
            return originalChars;
        }
        char[] ret = new char[length];
        int minus = length - originalChars.length;
        Random random = new Random();
        if (prefixFilled) {
            //前缀填充
            System.arraycopy(originalChars, 0, ret, minus, originalChars.length);
            for (int i = 0; i < minus; i++) {
                ret[i] = BaseConstant.DECIMAL_DIGITAL[random.nextInt(BaseConstant.DECIMAL_DIGITAL.length)];
            }
        } else {
            //作为后缀填充
            System.arraycopy(originalChars, 0, ret, 0, originalChars.length);
            for (int i = originalChars.length; i < length; i++) {
                ret[i] = BaseConstant.DECIMAL_DIGITAL[random.nextInt(BaseConstant.DECIMAL_DIGITAL.length)];
            }
        }
        return ret;
    }

    /**
     * 根据时间戳（MMddHHmmssS）生成UUID 20 bytes
     * @return
     */
    public static synchronized String newUUId() {
        return LazyHolder.uuid.genUUId("", "MMddHHmmssS", 20);
    }

    /**
     * 根据前缀 日渐戳生成UUID
     * @param prefix    时间戳
     * @param pattern   格式化日期
     * @param genLen     生成长度
     * @return
     */
    public static synchronized String newUUId(String prefix, String pattern, int genLen) {
        return LazyHolder.uuid.genUUId(prefix, pattern, genLen > 0 ? genLen : 20);
    }



}
