package com.cc.sdk2.jsdk.commons.result;

import com.cc.sdk2.jsdk.base.exceptions.BaseCheckedException;
import com.cc.sdk2.jsdk.base.exceptions.BaseCode;
import com.cc.sdk2.jsdk.base.exceptions.BusinessException;
import com.cc.sdk2.jsdk.commons.utils.StringUtil;

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
        return new ApiResult<>(BaseCode.SUCCESS_CODE, BaseCode.SUCCESS_CODE_MESSAGE, new HashMap<String, Object>(1));
    }

    /**
     * 获取成功返回
     *
     * @param data 返回数据
     * @param <T>  类型
     * @return api result对象
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(BaseCode.SUCCESS_CODE, BaseCode.SUCCESS_CODE_MESSAGE, data);
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
     * @param checkedException errorcode 实现对象
     * @return api result 对象
     */
    public static ApiResult<?> failure(BaseCheckedException checkedException) {
        return new ApiResult<>(checkedException.getCode(), StringUtil.isNotNullOrEmpty(checkedException.getI18nMessage()) ? checkedException.getI18nMessage() : checkedException.getDefaultMessage(), null);
    }

    /**
     * 返回基础业务异常
     *
     * @param msg 提示
     * @return api result 对象
     */
    public static ApiResult<?> failure(int groupId, int serviceId, String msg) {
        return new ApiResult<>(new BusinessException(groupId, serviceId).getCode(), msg, null);
    }


}
