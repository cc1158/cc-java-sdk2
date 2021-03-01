package com.cc.sdk2.jsdk.commons.result;

import java.util.HashMap;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/2/8 18:42
 **/
public final class ResultBuilder {
    /**
     * 获取成功返回结果
     *
     * @return 成功结果
     */
    public static ApiResult<?> success() {
        return new ApiResult<>(BaseErrorCode.Success.code, BaseErrorCode.Success.msg, new HashMap<String, Object>(1));
    }

    /**
     * 获取成功返回
     *
     * @param data 返回数据
     * @param <T>  类型
     * @return api result对象
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(BaseErrorCode.Success.code, BaseErrorCode.Success.msg, data);
    }

    /**
     * 获取自定义code和msg的结果
     *
     * @param code 自定义code
     * @param msg  说明
     * @return api result对象
     */
    public static ApiResult<?> getApiResult(int code, String msg) {
        return new ApiResult<>(code, msg, new HashMap<String, Object>(1));
    }

    /**
     * 根据error code 获取api result
     *
     * @param errorCode errorcode 实现对象
     * @return api result 对象
     */
    public static ApiResult<?> failure(ErrorCode errorCode) {
        return new ApiResult<>(errorCode.getCode(), errorCode.getMsg(), null);
    }

    /**
     * 返回基础业务异常
     *
     * @param msg 提示
     * @return api result 对象
     */
    public static ApiResult<?> failure(String msg) {
        return new ApiResult<>(BaseErrorCode.Business_Error.code, msg, null);
    }


}
