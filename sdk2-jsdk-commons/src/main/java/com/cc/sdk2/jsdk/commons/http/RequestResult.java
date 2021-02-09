package com.cc.sdk2.jsdk.commons.http;


import com.cc.sdk2.jsdk.commons.utils.StringUtil;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * 请求返回
 */
public class RequestResult {
    private String reqUrl;
    private final int statusCode;
    private final long costTime;
    private String redirectUrl;
    private byte[] data;
    private String respCharset;
    private Map<String, List<String>> respHeaders;

    RequestResult(String reqUrl, int statusCode, long costTime) {
        this.reqUrl = reqUrl;
        this.statusCode = statusCode;
        this.costTime = costTime;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getCostTime() {
        return costTime;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Map<String, List<String>> getRespHeaders() {
        return respHeaders;
    }

    public void setRespHeaders(Map<String, List<String>> respHeaders) {
        this.respHeaders = respHeaders;
    }

    public String getRespCharset() {
        return respCharset;
    }

    public void setRespCharset(String respCharset) {
        this.respCharset = respCharset;
    }

    @Override
    public String toString() {
        return "{" +
                "reqUrl=\"" + reqUrl + '\"' +
                ", statusCode=" + statusCode +
                ", costTime=" + costTime +
                ", redirectUrl=\"" + redirectUrl + '\"' +
                ", data=\"" + new String(data, Charset.forName(StringUtil.isNullOrEmpty(respCharset) ? "UTF-8" : this.respCharset)) +
                ", respCharset=\"" + respCharset + '\"' +
//                ", respHeaders=" + respHeaders +
                '}';
    }
}