package com.example.blog.util;

import com.example.blog.common.ResultEnum;

public class Result<T> {
    private boolean success = true;
    private int code = ResultEnum.OK.getCode();
    private String msg = "";
    private T data;

    public static <T> Result<T> newInstance() {
        return new Result<T>();
    }

    public Result() {

    }

    public Result(T data) {
        this.data = data;
    }

    public Result(ResultEnum status) {
        this.msg = status.getMsg();
        this.code = status.getCode();
    }

    public Result(String msg) {
        this.msg = msg;
    }


    public Result(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public Result(int code, String msg, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }


    public Result<T> status(ResultEnum status) {
        this.msg = msg;
        this.code = code;
        return this;
    }

    public Result<T> ok() {
        success = true;
        return this;
    }

    public Result<T> fail() {
        success = false;
        return this;
    }

    public Result<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }


    public Result<T> code(int code) {
        this.code = code;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
