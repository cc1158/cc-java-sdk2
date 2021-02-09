package com.cc.sdk2.jsdk.commons.http.entities;

import java.io.UnsupportedEncodingException;

public class BasicDataRequestEntity extends BaseRequestEntity<BasicDataRequestEntity> {

    @Override
    public byte[] genSendData(String charset) throws UnsupportedEncodingException {
        return new byte[0];
    }
}
