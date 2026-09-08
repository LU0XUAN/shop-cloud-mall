package com.shop.core.result;

import com.shop.core.constant.HttpStatus;

import java.io.Serializable;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/10 21:57
 * @Version 1.0
 */
public enum ResultCode implements CustomizeResultCode, Serializable {


    SUCCESS(HttpStatus.SUCCESS, "操作成功"),
    SYSTEM_EXECUTION_ERROR(HttpStatus.ERROR, "系统内部错误"),
    ;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    private Integer code;

    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":\"" + code + '\"' +
                ", \"msg\":\"" + msg + '\"' +
                '}';
    }
}
