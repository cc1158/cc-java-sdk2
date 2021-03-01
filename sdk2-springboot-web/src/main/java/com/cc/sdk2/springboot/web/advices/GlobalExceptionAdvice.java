package com.cc.sdk2.springboot.web.advices;

import com.cc.sdk2.jsdk.base.exceptions.*;
import com.cc.sdk2.jsdk.commons.result.*;
import com.cc.sdk2.jsdk.commons.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return ResultBuilder.getApiResult(BaseErrorCode.Server_Error.code, e.getMessage());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> exception400(MissingServletRequestParameterException e) {
        return ResultBuilder.failure(BaseErrorCode.Bad_Request);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> exceptionBodyNull(HttpMessageNotReadableException e) {
        return ResultBuilder.getApiResult(BaseErrorCode.Bad_Request.code, "Body Can't Be Readable Or Body Is Null");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<?> exception404(NoHandlerFoundException e) {
        return ResultBuilder.failure(BaseErrorCode.Not_Found);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResult<?> exception405(MethodArgumentTypeMismatchException e) {
        return ResultBuilder.failure(BaseErrorCode.Method_Not_Allowed);
    }
    /**
     * 验证不能绑定国际化
     * 临时方案
      */
    private static final Pattern VALIDATION_PATTERN = Pattern.compile("\\{(.+)\\}");

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> validationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.getFieldError() != null) {
            String msg = bindingResult.getFieldError().getDefaultMessage();
            if ( msg != null) {
                Matcher matcher = VALIDATION_PATTERN.matcher(bindingResult.getFieldError().getDefaultMessage());
                if (matcher.matches()) {
                    msg =  messageSource.getMessage(matcher.group(1), null, LocaleContextHolder.getLocale());
                }
                return ResultBuilder.getApiResult(BaseErrorCode.Bad_Request.code, msg);
//                return ResultBuilder.getApiResult(BaseErrorCode.Bad_Request.code,
//                        String.format("%s  %s", bindingResult.getFieldError().getField(), msg));
            }

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

    private String getI18nMsg(BaseException e) {
        Locale locale = e.getLocale() == null ? LocaleContextHolder.getLocale() : e.getLocale();
        String msg = messageSource.getMessage(e.getMsgCode(), e.getArgs(), e.getDefaultMsg(), locale);
        return msg;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> businessexception(BusinessException e) {
        e.setDefaultMsg(BaseErrorCode.Business_Error.getMsg());
        return ResultBuilder.failure(getI18nMsg(e));
    }

    @ExceptionHandler(TokenInvalidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> invalidTokenException(TokenInvalidException e) {
        e.setDefaultMsg(BaseErrorCode.Token_Invalid.msg);
        return ResultBuilder.getApiResult(BaseErrorCode.Token_Invalid.code, getI18nMsg(e));
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> accessTokenExpiredException(TokenExpiredException e) {
        e.setDefaultMsg(BaseErrorCode.Token_Expired.msg);
        return ResultBuilder.getApiResult(BaseErrorCode.Token_Expired.code, getI18nMsg(e));
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> refreshTokenExpiredException(RefreshTokenExpiredException e) {
        e.setDefaultMsg(BaseErrorCode.Refresh_Token_Expired.msg);
        return ResultBuilder.getApiResult(BaseErrorCode.Refresh_Token_Expired.code, getI18nMsg(e));
    }

    @ExceptionHandler(SessionExpiredException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<?> refreshTokenExpiredException(SessionExpiredException e) {
        e.setDefaultMsg(BaseErrorCode.Session_Expired.msg);
        return ResultBuilder.getApiResult(BaseErrorCode.Session_Expired.code, getI18nMsg(e));
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult<?> unauthorizedException(UnauthorizedException e) {
        e.setDefaultMsg(BaseErrorCode.Unauthorized_Error.msg);
        return ResultBuilder.getApiResult(BaseErrorCode.Unauthorized_Error.code, getI18nMsg(e));
    }
}
