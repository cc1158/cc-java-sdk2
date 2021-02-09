package com.cc.sdk2.jsdk.commons.utils;

import com.cc.sdk2.jsdk.base.SystemProperties;
import com.cc.sdk2.jsdk.commons.AppRunEnvs;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/2/8 20:31
 **/
public class NetworkUtil {

    /**
     * 获取第一个网关的ipv4
     * @return
     */
    public static String getLocalIpV4() {
        if (AppRunEnvs.getSystemProperty(SystemProperties.OS_NAME).equalsIgnoreCase("Linux")) {
            List<String> localIps = getLocalIpV4s();
            if (localIps == null || localIps.size() == 0) {
                return null;
            }
            return localIps.get(0);
        } else {
            try {
                return Inet4Address.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取全部的本机ipv4地址
     * @return
     */
    public static List<String> getLocalIpV4s() {
        List<String> ret = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface item = networkInterfaces.nextElement();
                if (item.isLoopback() || item.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = item.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address != null && address instanceof Inet4Address) {
                        ret.add(address.getHostAddress());
                    }
                }
            }
            return ret;
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断一个ip是否是内网地址
     *
     * @param ip ip地址
     * @return true   内网地址   false 外网地址
     */
    public static boolean isIntranet(String ip) {
        if (ip.startsWith("10.") || ip.startsWith("172.16.") || ip.startsWith("192.168.")) {
            return true;
        }
        return false;
    }

}
