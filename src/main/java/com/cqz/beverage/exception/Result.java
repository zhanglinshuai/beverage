package com.cqz.beverage.exception;

import lombok.Data;

/**
 * 全局统一返回结果
 * @param <T>
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;
    //成功方法
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<T>();
        result.setCode(200);
        result.setMsg("success");
        result.setData(data);
        return  result;
    }
    //失败方法
    public static <T> Result<T> fail(int code,String msg){
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMsg(msg);
        return  result;
    }
}
