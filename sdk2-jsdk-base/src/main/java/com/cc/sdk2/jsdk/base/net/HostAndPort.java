package com.cc.sdk2.jsdk.base.net;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * All rights reserved, copyright@cc.hu
 * 地址端口类
 * @author cc
 * @version 1.0
 * @date 2019/10/31 21:16
 **/
public class HostAndPort {

    public static final Pattern HOST_PORT_FIND_PATTERN = Pattern.compile("([a-zA-Z0-9\\.]+):(\\d+)(,?)");

//    public static final Pattern HOST_PORT_MATCH_PATTERN = Pattern.compile("((\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+)(,?))+|((([a-zA-Z0-9]+\\.)?[a-zA-Z0-9]+\\.[a-zA-Z0-9]+:\\d+)(,)?)+");

    public static final Pattern HOST_PORT_MATCH_PATTERN = Pattern.compile("(([a-zA-Z0-9\\.]+):(\\d+)(,?))+");


    private final String host;
    private final int port;

    public HostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    /**
     * process hosts to HostAndPort
     * @param addresses
     * @return
     */
    public static Set<HostAndPort> processAddressStr(String addresses) {
        Matcher matcher = HOST_PORT_MATCH_PATTERN.matcher(addresses);
        if (matcher.matches()) {
            Set<HostAndPort> hostAndPortSet = new HashSet<>();
            Matcher findMather = HOST_PORT_FIND_PATTERN.matcher(addresses);
            while (findMather.find()) {
                hostAndPortSet.add(new HostAndPort(findMather.group(1), Integer.parseInt(findMather.group(2))));
            }
            return hostAndPortSet;
        }
        return null;
    }

}
