package com.example.library.exception;

import com.example.library.domain.ApiCode;
import com.example.library.domain.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;

/**
 * 全局统一异常处理
 *
 * @author 冯名豪
 * @date 2022-09-21
 * @since 1.0
 */
@Slf4j
@ControllerAdvice
public class WebGlobalExceptionHandler {

    /**
     * 自定义异常
     */
    @ExceptionHandler(com.example.library.exception.BusinessException.class)
    @ResponseBody
    public Response businessException(com.example.library.exception.BusinessException e) {
        return Response.failure(e);
    }

    /**
     * 连接被中断异常
     */
    @ExceptionHandler(ClientAbortException.class)
    public void clientAbortException(ClientAbortException e, HttpServletRequest request) {
        log.warn("连接被中断异常: {},  {}", request.getRequestURI(), e.getMessage());
    }

    /**
     * 缺少请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Response missingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String parameterName = e.getParameterName();
        String msg = String.format("缺少%s参数", parameterName);
        log.warn("{},  {}", request.getRequestURI(), msg);
        return Response.failure(ApiCode.MISSING_PARAMETER, msg);
    }

    /**
     * 参数类型解析失败
     *
     * @param request 请求
     * @param e       异常
     * @return 响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Response httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        String requestURI = request.getRequestURI();
        Throwable cause = e.getCause();
        if (cause == null) {
            log.warn("uri: {},  参数格式有误: {}", requestURI, e.getMessage());
            return Response.failure(ApiCode.INVALID_PARAMETERS, "参数格式有误");
        }

        String message = e.getMessage();
        Throwable businessCause = cause.getCause();
        if (businessCause instanceof com.example.library.exception.BusinessException) {
            com.example.library.exception.BusinessException business = (com.example.library.exception.BusinessException) businessCause;
            return Response.failure(business.getCode(), business.getMsg());
        }

        if (!StringUtils.hasText(request.getContentType())) {
            log.warn("{},  请求头Content-Type为空", requestURI);
            return Response.failure(ApiCode.CONTENT_TYPE_INVALID, "请求头Content-Type为空");
        }

        final String reference = "through reference chain:";
        if (reference.equals(message)) {
            int begin = message.lastIndexOf('[');
            int end = message.lastIndexOf(']');
            if (begin != -1) {
                log.warn("{},  {}", requestURI, message);
                String fieldName = message.substring(begin + 2, end - 1);
                String msg = String.format("%s参数类型错误", fieldName);
                return Response.failure(ApiCode.INVALID_PARAMETERS, msg);
            }
        }
        if (cause instanceof JsonProcessingException) {
            log.warn("JSON格式有误, {},  {}", requestURI, message);
            return Response.failure(ApiCode.INVALID_PARAMETERS, "JSON格式有误");
        }

        log.warn("{},  {}", requestURI, message);
        return Response.failure(ApiCode.INVALID_PARAMETERS);
    }

    /**
     * 参数验证失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("{},  {}", request.getRequestURI(), e.getMessage());
        return fieldErrors(e.getBindingResult().getFieldErrors());
    }

    /**
     * 参数验证失败
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Response bindException(BindException e, HttpServletRequest request) {
        log.warn("{},  {}", request.getRequestURI(), e.getMessage());
        return fieldErrors(e.getBindingResult().getFieldErrors());
    }

    /**
     * 参数验证失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Response constraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        log.warn("{},  {}", request.getRequestURI(), violation.getMessage());
        String field = violation.getPropertyPath().toString();
        return Response.failure(ApiCode.INVALID_PARAMETERS, field);
    }

    /**
     * 参数验证失败
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public Response validationException(ValidationException e, HttpServletRequest request) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        if (cause instanceof com.example.library.exception.BusinessException) {
            com.example.library.exception.BusinessException be = (com.example.library.exception.BusinessException) cause;
            return Response.failure(be.getCode(), be.getMsg());
        }
        log.warn("{},  {}", request.getRequestURI(), message);

        String temp = "Check configuration for";
        int index = message.indexOf(temp);
        if (index > 0) {
            int lastIndex = message.lastIndexOf('\'');
            message = message.substring(index + temp.length() + 2, lastIndex);
            return Response.failure(ApiCode.INVALID_PARAMETERS, message + "参数无效");
        }
        return Response.failure(ApiCode.INVALID_PARAMETERS);
    }

    /**
     * 参数验证失败
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Response methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String msg = String.format("%s参数类型异常", e.getName());
        log.warn("{},  {}", request.getRequestURI(), msg);
        return Response.failure(ApiCode.INVALID_PARAMETERS, msg);
    }

    /**
     * 不支持请求方式
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Response httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String msg = String.format("不支持%s请求", e.getMethod());
        log.warn("{},  {}", request.getRequestURI(), msg);
        return Response.failure(ApiCode.REQUEST_REQUIRED);
    }

    /**
     * 不支持请求类型
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public Response httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String message = e.getMessage();
        String contentType = request.getContentType();
        if (!StringUtils.hasText(contentType)) {
            log.warn("{},  请求头Content-Type为空", requestURI);
            return Response.failure(ApiCode.CONTENT_TYPE_INVALID, "请求头Content-Type为空");
        }

        String notSupported = "Content type 'application/json' not supported";
        if (notSupported.equals(message)) {
            log.warn("{},  不支持JSON数据格式, {}", requestURI, message);
            return Response.failure(ApiCode.INVALID_PARAMETERS, "不支持JSON数据格式");
        }

        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException) {
            String error = cause.getMessage();
            int begin = error.lastIndexOf('[');
            int end = error.lastIndexOf(']');
            if (begin > 0) {
                log.warn("{},  {}", requestURI, message);
                String filed = error.substring(begin + 2, end - 1);
                String msg = filed + ":参数类型错误";
                return Response.failure(ApiCode.INVALID_PARAMETERS, msg);
            }
        }

        if (MediaType.APPLICATION_JSON_VALUE.contains(contentType)) {
            log.warn("{},  {}", requestURI, message);
            return Response.failure(ApiCode.INVALID_PARAMETERS, "JSON数据错误");
        }

        log.warn("{},  {}", requestURI, message);
        return Response.failure(ApiCode.INVALID_PARAMETERS);
    }

    /**
     * 文件上传异常
     */
    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public Response multipartException(MultipartException e, HttpServletRequest request) {
        log.warn("{},  {}", request.getRequestURI(), e.getMessage());
        String msg = "Current request is not a multipart request";
        if (msg.equals(e.getMessage())) {
            return Response.failure(ApiCode.INVALID_PARAMETERS, "请上传文件类型");
        }
        return Response.failure(ApiCode.ERROR, "文件上传失败");
    }

    /**
     * 文件上传异常
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseBody
    public Response missingServletRequestPartException(MissingServletRequestPartException e, HttpServletRequest request) {
        String message = e.getMessage();
        log.warn("{},  {}", request.getRequestURI(), message);
        int first = message.indexOf('\'');
        if (first > 0) {
            int last = message.lastIndexOf('\'');
            String field = message.substring(first + 1, last);
            return Response.failure(ApiCode.MISSING_PARAMETER, "缺少" + field + "文件参数");
        }
        return Response.failure(ApiCode.MISSING_PARAMETER, "缺少文件参数");
    }

    /**
     * 通用异常(未知异常)
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Response exception(Throwable e, HttpServletRequest request, HttpServletResponse response) {
        log.error("状态码:{},  requestURI:{},  未知异常", response.getStatus(), request.getRequestURI(), e);
        return Response.failure(ApiCode.ERROR);
    }

    /**
     * 返回响应
     *
     * @param fieldErrors 字段异常集合
     * @return 响应对象
     */
    private Response fieldErrors(List<FieldError> fieldErrors) {
        StringBuilder msgList = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            String defaultMessage = fieldError.getDefaultMessage();
            String content = "Failed to convert property paramValue of type";
            String msg;
            if (content.equals(defaultMessage)) {
                msg = String.format("%s参数类型错误", fieldError);
            } else {
                msg = String.format("%s%s", fieldError, defaultMessage);
            }
            msgList.append(msg).append("，");
        }
        // 删除最后一个逗号
        msgList.deleteCharAt(msgList.length() - 1);
        return Response.failure(ApiCode.INVALID_PARAMETERS, msgList.toString());
    }

}

