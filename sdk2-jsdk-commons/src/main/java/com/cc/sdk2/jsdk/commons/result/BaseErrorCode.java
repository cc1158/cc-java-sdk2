package com.cc.sdk2.jsdk.commons.result;

/**
 * 基础结果集
 * 定义基础业务错误类型
 * 异常码（1） + 业务（2位 00 代表框架） + 错误类型（三位）
 *
 * 例如：100900   业务异常错误 100001 token非法
 *
 * @author sen.hu
 * @date 2019/10/9 13:54
 **/
public enum BaseErrorCode implements ErrorCode {
    /**
     * 成功返回
     */
    Success(0, "success"),
    /**
     * 资源未找到
     */
    Not_Found(404, "Not Found"),
    /**
     * 参数异常
     */
    Bad_Request(400, "Bad Request"),
    /**
     * 请求方式错误
     */
    Method_Not_Allowed(405, "Method Not Allowed"),
    /**
     * 服务器未知异常
     */
    Server_Error(500, "Server Error/Unknown Error"),
    /**
     * 业务异常
     */
    Business_Error(100900, ""),
    /**
     * 非法token
     */
    Token_Invalid(100001, "Illegal Token"),
    /**
     * Access Token过期
     */
    Token_Expired(100002, "Access Token Expired"),
    /**
     * Refresh Token 过期
     */
    Refresh_Token_Expired(100003, "Refresh Token Expired"),
    /**
     * Session 过期
     */
    Session_Expired(100004, "Session Expired"),

    /**
     * 未授权请求
     */
    Unauthorized_Error(100009, "Unauthorized Request"),;

    public final int code;
    public final String msg;

    BaseErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
