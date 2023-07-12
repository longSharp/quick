package com.quick.member.common.config.exception;

import com.quick.member.common.enums.ResultCode;
import com.quick.member.domain.dto.resp.R;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @Author: Longcm
 * @Description: 统一异常处理
 */
@RestControllerAdvice
public class ExceptionMapper {

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R handleMethodArgumentNotValidException(MissingServletRequestParameterException ex){
        String parameterName = ex.getParameterName();
        return R.error(ResultCode.PARAMS_VALID_FAIL.getCode(),"`"+parameterName+"`"+ResultCode.PARAMS_VALID_FAIL.getMsg());
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
   public R handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
       BindingResult bindingResult = ex.getBindingResult();
       StringBuilder sb = new StringBuilder("参数校验失败！");
       for (FieldError fieldError : bindingResult.getFieldErrors()) {
           sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(",");
       }
       return R.error(ResultCode.PARAMS_VALID_FAIL.getCode(),sb.toString());
   }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
   public R handleConstraintViolationException(ConstraintViolationException ex){
        String message = ex.getMessage();
        String[] split = message.split("\\.");
        return R.error(ResultCode.PARAMS_VALID_FAIL.getCode(),ResultCode.PARAMS_VALID_FAIL.getMsg()+(split.length>1?"|"+split[1]:""));
   }

    @ExceptionHandler(BusinessException.class)
    public R businessExceptionHandler(BusinessException e) {
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R handleRuntimeException(RuntimeException ex){
        ex.printStackTrace();
        return R.error();
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R handleException(Exception ex){
        ex.printStackTrace();
        return R.error();
    }
}