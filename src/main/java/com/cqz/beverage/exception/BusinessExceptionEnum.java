package com.cqz.beverage.exception;

/**
 * 自定义异常枚举
 */
public enum BusinessExceptionEnum {
    //通用异常
    PARAM_ERROR(10001,"参数输入错误"),
    PARAM_EMPTY(10002,"必填参数为空"),
    PARAM_RANGE_ERROR(10004,"参数超出合法范围"),
    PARAM_FORMAT_ERROR(10003,"参数格式错误"),
    SYSTEM_ERROR(10005,"系统内部错误"),
    SYSTEM_BUSY(10006,"系统繁忙，请稍后重试"),
    DATA_DUPLICATE(10007,"数据重复已存在"),
    REQUEST_TIMEOUT(10008,"请求超时"),
    //用户异常
    USER_NOT_FOUND(20001,"用户不存在"),
    USER_NAME_DUPLICATE(20002,"用户名已被占用"),
    USER_PASSWORD_ERROR(20003,"密码错误"),
    USER_PASSWORD_EMPTY(20004,"密码不能为空"),
    USER_ACCOUNT_DISABLED(20005,"用户账号被禁用"),
    USER_NOT_LOGIN(20006,"用户未登录"),
    USER_LOGIN_INVALID(20007,"用户登录已过期")
    ;

    private int code;
    private String msg;
    BusinessExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
