package com.quick.member.common.config.exception;

import com.quick.member.common.enums.ResultCode;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException{
    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode error) {
        super(error.getMsg());
        this.code = error.getCode();
    }

    public BusinessException(ResultCode error, String message) {
        super(message);
        this.code = error.getCode();
    }

    public String getCode() {
        return code;
    }
}
