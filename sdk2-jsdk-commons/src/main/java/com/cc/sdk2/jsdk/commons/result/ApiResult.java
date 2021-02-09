package com.cc.sdk2.jsdk.commons.result;

import com.cc.sdk2.jsdk.commons.date.DateUtil;

import java.util.Date;
import java.util.Map;

/**
 * All rights reserved, copyright@cc.hu
 * 接口返回结果集
 * @author cc
 * @version 1.0
 * @date 2021/2/8 18:42
 **/
public class ApiResult<T> {

    private final int code;
    private final String msg;
    private T data;
    private final String time = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    private String traceId;


    protected ApiResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    protected ApiResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    /**
     * 添加返回数据  data必须是map类型
     *
     * @param key
     * @param value
     * @return
     */
    public ApiResult<T> addData(String key, Object value) {
        if (this.data != null && this.data instanceof Map) {
            Map<String, Object> temp = (Map) this.data;
            temp.put(key, value);
            return this;
        } else {
            throw new RuntimeException("Data Filed Is Not A java.util.Map Instance, Can't Put A Return Data");
        }
    }
}
