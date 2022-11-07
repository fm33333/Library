package com.example.library.domain;

import lombok.Getter;

/**
 * 返回的业务异常状态码枚举
 *
 * @author 冯名豪
 * @date 2022-09-21
 * @since 1.0
 */
@Getter
public enum ApiCode {

    SUCCESS(0, "成功"),
    ERROR(1, "系统繁忙, 请稍后再试"),

    INVALID_PARAMETERS(400, "无效的参数"),
    MISSING_PARAMETER(402, "缺少参数"),
    NOT_FOUND(404, "请求的URL地址不存在"),
    REQUEST_REQUIRED(405, "网络请求错误, 请求方法未允许"),
    DATA_NULL(407, "数据不存在, 请核实"),
    CONTENT_TYPE_INVALID(415, "不支持的媒体类型"),

    GATEWAY_REQUEST_TIMEOUT(504, "网关网络超时"),
    SYSTEM_ERROR(500, "服务器错误, 请联系管理员"),

    BOOK_EXIST(1000, "图书已存在"),

    ;


    private final int code;
    private final String msg;

    ApiCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
