package com.cc.sdk2.jsdk.commons.http.entities;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseRequestEntity<T extends BaseRequestEntity> {

    protected final String reqUid = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 16);

    protected String contentType;

    private Map<String, Object> queryParams = new HashMap<>();

    private Map<String, Object> headers = new HashMap<>();

    protected Map<String, Object> formParams = new HashMap<>(2);

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public T addHeader(String key, Object value) {
        headers.put(key, value);
        return (T) this;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public T addUrlParam(String name, Object value) {
        queryParams.put(name, value);
        return (T) this;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public Map<String, Object> getFormParams() {
        return formParams;
    }

    public abstract byte[] genSendData(String charset) throws UnsupportedEncodingException;

}
