package com.cc.sdk2.jsdk.commons.http.entities;


import com.cc.sdk2.jsdk.commons.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StringRequestEntity extends BaseRequestEntity<StringRequestEntity> {

    private String rawBody;


    public StringRequestEntity() {
    }

    public StringRequestEntity(String rawBody) {
        this.rawBody = rawBody;
    }

    public StringRequestEntity addFormData(String key, Object value) {
        formParams.put(key, value);
        rawBody = null;
        return this;
    }

    public StringRequestEntity setRawBody(String body) {
        this.rawBody = body;
        formParams.clear();
        return this;
    }

    public String getRawBody() {
        return rawBody;
    }

    @Override
    public byte[] genSendData(String charset) throws UnsupportedEncodingException {
        if (!StringUtil.isNullOrEmpty(rawBody)) {
            return this.rawBody.getBytes(charset);
        }
        if (formParams.size() > 0) {
            StringBuilder buf = new StringBuilder();
            this.formParams.forEach((k, v) -> {
                try {
                    buf.append(k).append("=").append(URLEncoder.encode(String.valueOf(v), charset)).append("&");
                } catch (UnsupportedEncodingException e) {
                }
            });
            return buf.toString().getBytes(charset);
        }
        return new byte[0];
    }



}
