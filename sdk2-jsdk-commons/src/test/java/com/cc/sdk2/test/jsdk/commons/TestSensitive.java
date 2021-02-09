package com.cc.sdk2.test.jsdk.commons;

import com.cc.sdk2.jsdk.commons.sensitive.SensitivewordFilter;
import org.junit.Test;

import java.util.Set;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2020/4/30 15:27
 **/
public class TestSensitive {

    @Test
    public void testBanWords(String[] args) {
        SensitivewordFilter filter = new SensitivewordFilter();
        String string = "太多的伤感粪青也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
                + "然后法轮功 我们的扮演的角色罢工门青主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
                + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。";
        System.out.println("待检测语句字数：" + string.length());
        long beginTime = System.currentTimeMillis();
        int s = filter.getCheckSensitiveWordResult(string, 1);
        Set<String> set = filter.getSensitiveWord(string, 0);
        long endTime = System.currentTimeMillis();
        System.out.println("ss=" + s);
        System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
        System.out.println("总共消耗时间为：" + (endTime - beginTime));
    }
}
