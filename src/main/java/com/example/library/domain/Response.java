package com.example.library.domain;

import com.example.library.constant.FieldLength;
import com.example.library.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回Response对象
 *
 * @author 冯名豪
 * @date 2022-09-21
 * @since 1.0
 */
@Data
public class Response implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = FieldLength.L1;

    /**
     * 是否成功
     */
    @JsonIgnore
    private boolean success;

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public Response() {
        this.success = true;
        this.code = ApiCode.SUCCESS.getCode();
        this.msg = ApiCode.SUCCESS.getMsg();
    }

    private Response(Object data) {
        this.success = true;
        this.code = ApiCode.SUCCESS.getCode();
        this.msg = ApiCode.SUCCESS.getMsg();
        this.data = data;
    }

    public static Response success() {
        return new Response();
    }

    public static Response success(Object data) {
        return new Response(data);
    }

    public static Response failure(ApiCode code) {
        return createResponse(code.getCode(), code.getMsg(), null);
    }

    public static Response failure(ApiCode code, Object data) {
        return createResponse(code.getCode(), code.getMsg(), data);
    }

    public static Response failure(ApiCode code, String msg) {
        return createResponse(code.getCode(), msg, null);
    }

    public static Response failure(int code, String msg) {
        Response response = new Response();
        response.success = false;
        response.code = code;
        response.msg = msg;
        response.data = null;
        return response;
    }

    public static Response failure(BusinessException e) {
        Response response = new Response();
        response.success = false;
        response.code = e.getCode();
        response.msg = e.getMsg();
        response.data = e.getData();
        return response;
    }

    private static Response createResponse(int code, String msg, Object data) {
        Response response = new Response();
        response.success = ApiCode.SUCCESS.getCode() == code;
        response.code = code;
        response.msg = msg;
        response.data = data;
        return response;
    }

}