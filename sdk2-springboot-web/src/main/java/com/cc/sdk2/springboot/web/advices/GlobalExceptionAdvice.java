package com.cc.sdk2.springboot.web.advices;

import com.cc.sdk2.jsdk.base.exceptions.*;
import com.cc.sdk2.jsdk.commons.result.*;
import com.cc.sdk2.jsdk.commons.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Locale;

/**
 * web全局异常处理
 *
 * //TODO 多语言处理
 *
 * @author sen.hu
 * @date 2018/11/29 13:36
 **/
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> exception500(Exception e) {
        e.printStackTrace();
        return ResultBuilder.getErrorResult(BaseErrorCode.Server_Error);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> exception400(MissingServletRequestParameterException e) {
        return ResultBuilder.getErrorResult(BaseErrorCode.Bad_Request);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<?> exception404(NoHandlerFoundException e) {
        return ResultBuilder.getErrorResult(BaseErrorCode.Not_Found);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResult<?> exception405(MethodArgumentTypeMismatchException e) {
        return ResultBuilder.getErrorResult(BaseErrorCode.Method_Not_Allowed);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> validationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.getFieldError() != null) {
            return ResultBuilder.getApiResult(BaseErrorCode.Bad_Request.code,
                    String.format("%s  %s", bindingResult.getFieldError().getField(), bindingResult.getFieldError().getDefaultMessage()));
        }
        return ResultBuilder.getApiResult(BaseErrorCode.Bad_Request.code, "Parameters Error");
    }

    @ExceptionHandler({ParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> requestParameterException(ParameterException e) {
        if (StringUtil.isNullOrEmpty(e.getMessage())) {
            return ResultBuilder.getApiResult(BaseErrorCode.Bad_Request.code, "Parameters Error");
        }
        return ResultBuilder.getApiResult(BaseErrorCode.Bad_Request.code, e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> businessexception(BusinessException e) {
        Locale locale = e.getLocale() == null ? LocaleContextHolder.getLocale() : e.getLocale();
        String msg = messageSource.getMessage(e.getMsgCode(), e.getArgs(), e.getDefaultMsg(), locale);
        return ResultBuilder.getErrorResult(msg);
    }

    @ExceptionHandler(TokenInvalidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> invalidTokenException(TokenInvalidException e) {
        return ResultBuilder.getErrorResult(BaseErrorCode.Token_Invalid);
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> accessTokenExpiredException(TokenExpiredException e) {
        return ResultBuilder.getErrorResult(BaseErrorCode.Token_Expired);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> refreshTokenExpiredException(RefreshTokenExpiredException e) {
        return ResultBuilder.getErrorResult(BaseErrorCode.Refresh_Token_Expired);
    }

    @ExceptionHandler(SessionExpiredException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> refreshTokenExpiredException(SessionExpiredException e) {
        return ResultBuilder.getErrorResult(BaseErrorCode.Session_Expired);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult<?> unauthorizedException(UnauthorizedException e) {
        return ResultBuilder.getErrorResult(BaseErrorCode.Unauthorized_Error);
    }
}
