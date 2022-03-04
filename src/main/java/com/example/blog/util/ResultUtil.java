package com.example.blog.util;

import com.example.blog.common.ResultEnum;

public class ResultUtil {
    public static boolean isOk(Result<?> result){
        return null != result && result.getCode() == ResultEnum.OK.getCode();
    }

    public static <T> Result<T> ok(){
        return new Result<T>(ResultEnum.OK);
    }

    public static <T> Result<T> ok(T data){
        return new Result<T>(ResultEnum.OK.getCode(), ResultEnum.OK.getMsg(), data);
    }

    public static <T> Result<T> fail(){
        return new Result<T>(ResultEnum.BAD_REQUEST).fail();
    }

    public static <T> Result<T> status(ResultEnum status){
        return new Result<T>(status.getCode(), status.getMsg()).fail();
    }

    public static <T> Result<T> fail(ResultEnum status){
        return new Result<T>(status.getCode(), status.getMsg()).fail();
    }

    public static <T> Result<T> fail(String message){
        return fail(ResultEnum.BAD_REQUEST.getCode(), message, (T)null).fail();
    }

    public static <T> Result<T> fail(int code, String message){
        return new Result<T>(code, message).fail();
    }

    public static <T> Result<T> fail(int code ,String message, T data){
        return new Result<T>(code, message, data).fail();
    }

    public static <T> Result<T> notfound(){
        return new Result<T>(ResultEnum.NOT_FOUND).fail();
    }
}
