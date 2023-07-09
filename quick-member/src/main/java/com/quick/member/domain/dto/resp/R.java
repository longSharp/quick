package com.quick.member.domain.dto.resp;

import com.quick.member.common.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回对象R
 *
 * @author longcm
 * @since 2023-06-16 16:25:59
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code; //编码

    private String msg; //信息

    private T data; //数据

    public static <T> R<T> ok() {
        R<T> r = new R<>();
        r.code = ResultCode.REQUEST_SUCCESS.getCode();
        r.msg = ResultCode.REQUEST_SUCCESS.getMsg();
        return r;
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.code = ResultCode.REQUEST_SUCCESS.getCode();
        r.msg = ResultCode.REQUEST_SUCCESS.getMsg();
        r.data = data;
        return r;
    }

    public static <T> R<T> ok(ResultCode result) {
        R<T> r = new R<>();
        r.code = result.getCode();
        r.msg = result.getMsg();
        return r;
    }

    public static <T> R<T> ok(String code,String msg) {
        R<T> r = new R<>();
        r.code = code;
        r.msg = msg;
        return r;
    }

    public static <T> R<T> ok(String code,String msg,T data) {
        R<T> r = new R<>();
        r.code = code;
        r.msg = msg;
        r.data = data;
        return r;
    }

    public static <T> R<T> error() {
        R<T> r = new R<>();
        r.msg = ResultCode.REQUEST_FAIL.getMsg();
        r.code = ResultCode.REQUEST_FAIL.getCode();
        return r;
    }

    public static <T> R<T> error(String code,String msg) {
        R<T> r = new R<>();
        r.msg = msg;
        r.code = code;
        return r;
    }

}

