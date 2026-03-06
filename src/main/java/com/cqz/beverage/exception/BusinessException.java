package com.cqz.beverage.exception;

/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException {
    private final int code;
    //直接传入异常码和提示
    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
    }
    public BusinessException(BusinessExceptionEnum businessExceptionEnum) {
        this(businessExceptionEnum.getCode(), businessExceptionEnum.getMsg());
    }
    public int getCode() {
        return code;
    }
}
