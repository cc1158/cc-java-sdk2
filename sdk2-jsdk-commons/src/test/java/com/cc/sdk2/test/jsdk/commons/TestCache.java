package com.cc.sdk2.test.jsdk.commons;

import com.cc.sdk2.jsdk.commons.localcache.Cache;
import com.cc.sdk2.jsdk.commons.localcache.caches.CacheBuilder;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2019/11/14 21:49
 **/
public class TestCache {


    public static void main(String[] args) {

        CacheBuilder cacheBuilder = new CacheBuilder();
        Cache<String, String> cache = cacheBuilder.cacheName("test-cache")
                .expiredTime(10)
                .buildGuava()
                ;
        cache.put("test123", "hhhhhhhhhhhhhhhhhhhhhhhh");

        System.out.println(cache.get("test123"));


        try {
            Thread.sleep(10 * 1000);

            System.out.println( cache.get("test123"));
            System.in.read();
            System.out.println(cache.get("test123"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
