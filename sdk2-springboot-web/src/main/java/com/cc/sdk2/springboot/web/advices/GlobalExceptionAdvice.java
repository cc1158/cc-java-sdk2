package com.cc.sdk2.springboot.web.advices;

import com.cc.sdk2.jsdk.base.exceptions.*;
import com.cc.sdk2.jsdk.commons.result.*;
import com.cc.sdk2.jsdk.commons.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private Logger logger = LogManager.getLogger(GlobalExceptionAdvice.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> exception500(Exception e) {
        e.printStackTrace();
        logger.error(e);
        return ResultBuilder.failure(new UnknownServerException());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> exception400(MissingServletRequestParameterException e) {
        return ResultBuilder.failure(new ParameterException(e.getMessage()));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> exceptionBodyNull(HttpMessageNotReadableException e) {
        return ResultBuilder.getApiResult(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<?> exception404(NoHandlerFoundException e) {
        return ResultBuilder.getApiResult(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResult<?> exception405(MethodArgumentTypeMismatchException e) {
        return ResultBuilder.getApiResult(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
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
                return ResultBuilder.getApiResult(HttpStatus.BAD_REQUEST.value(),  msg);
//                return ResultBuilder.getApiResult(BaseErrorCode.Bad_Request.code,
//                        String.format("%s  %s", bindingResult.getFieldError().getField(), msg));
            }

        }
        return ResultBuilder.getApiResult(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ExceptionHandler({ParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> requestParameterException(ParameterException e) {
        return ResultBuilder.getApiResult(HttpStatus.BAD_REQUEST.value(), getI18nMsg(e));
    }

    private String getI18nMsg(BaseCheckedException e) {
        if (StringUtil.isNotNullOrEmpty(e.getI18nCode())) {
            return messageSource.getMessage(e.getI18nCode(), e.getArgs(), e.getDefaultMessage(), LocaleContextHolder.getLocale());
        }
        return null;
    }

    @ExceptionHandler({BusinessException.class, TokenInvalidException.class, TokenExpiredException.class,
            RefreshTokenExpiredException.class, SessionExpiredException.class, UnauthorizedException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> businessexception(BaseCheckedException e) {
        e.setI18nMessage(getI18nMsg(e));
        return ResultBuilder.failure(e);
    }

}
