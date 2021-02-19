package com.cc.sdk2.jsdk.commons.localcache.caches;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2019/11/14 20:54
 **/
class CacheMonitor implements Runnable {



    @Override
    public void run() {
        if (CacheBuilder.CACHE_MAP.isEmpty()) {
            return ;
        }
        System.out.println("********************* CacheMonitor [" + CacheBuilder.CACHE_MAP .size() + "] *********************");
        CacheBuilder.CACHE_MAP.forEach((ke, v) -> {
            System.out.println("[" + v.getCacheName() + v.getClass().getSimpleName() + "] " + v.getCacheInfo());
        });
        System.out.println("******************* CacheMonitor end ********************************");

    }
}
