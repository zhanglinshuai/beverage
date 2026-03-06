package com.cqz.beverage.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    //捕获自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }
    //捕获运行时异常
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        //记录异常日志
        e.printStackTrace();
        return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(),  BusinessExceptionEnum.SYSTEM_ERROR.getMsg());
    }
    //捕获所有未定义的异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        e.printStackTrace();
        return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), "未知异常"+e.getMessage());
    }
}
