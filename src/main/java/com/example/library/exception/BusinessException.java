package com.example.library.exception;

import com.example.library.domain.ApiCode;
import com.example.library.domain.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局自定义业务异常
 *
 * @author 冯名豪
 * @date 2022-09-21
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException{

    private final int code;
    private final String msg;
    private final String subCode;
    private final String subMsg;
    private final BaseVO data;

    public BusinessException() {
        super(ApiCode.ERROR.getMsg());
        ApiCode apiCode = ApiCode.ERROR;
        this.code = apiCode.getCode();
        this.msg = apiCode.getMsg();
        this.subCode = "";
        this.subMsg = "";
        this.data = null;
    }

    public BusinessException(ApiCode apiCode) {
        super(apiCode.getMsg());
        this.msg = apiCode.getMsg();
        this.code = apiCode.getCode();
        this.subCode = "";
        this.subMsg = "";
        this.data = null;
    }

    public BusinessException(ApiCode apiCode, String msg) {
        super(msg);
        this.code = apiCode.getCode();
        this.msg = msg;
        this.subCode = "";
        this.subMsg = "";
        this.data = null;
    }

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.subCode = "";
        this.subMsg = "";
        this.data = null;
    }

    public BusinessException(int code, String msg, String subCode, String subMsg) {
        super(subMsg);
        this.code = code;
        this.msg = msg;
        this.subCode = subCode;
        this.subMsg = subMsg;
        this.data = null;
    }

    public BusinessException(ApiCode apiCode, BaseVO data) {
        super(apiCode.getMsg());
        this.code = apiCode.getCode();
        this.msg = apiCode.getMsg();
        this.subCode = "";
        this.subMsg = "";
        this.data = data;
    }

}
