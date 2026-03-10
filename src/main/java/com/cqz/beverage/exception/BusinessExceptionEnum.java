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
    USER_LOGIN_INVALID(20007,"用户登录已过期"),
    USER_PASSWORD_SAME(20008,"密码修改前后相同"),
    MOTIFY_PASSWORD_ERROR(20009,"修改密码失败"),
    USER_ROLE_NO_PERMISSION(20010,"用户非管理员无权限"),
    //设备异常
    DEVICE_ALREADY_EXISTS(30001,"设备已经存在"),
    DEVICE_NOT_EXISTS(30002,"设备不存在"),
    OPERATOR_NOT_EXISTS(30003,"运营商不存在"),
    USER_NOT_HAVE_DEVICE(30004,"用户名下没有设备"),
    DEVICE_UNAVAILABLE(30005,"设备目前不可用"),
    //维修记录异常
    DEVICE_ARCHIVE_NOT_EXIES(40001,"该设备的维修记录不存在"),
    //商品异常
    PRODUCT_HAS_EXISTS(50001,"商品已存在"),
    PRODUCT_NOT_EXISTS(50002,"商品不存在"),
    //库存异常
    CAPACITY_HAS_EXISTS(60001,"库存配置已存在"),
    BRAND_AND_PRODUCT_MUST_EXIST(60002,"品牌和商品名称必须都存在"),
    BRAND_MUST_EXIST(60003,"品牌必须存在"),
    STOCK_MUST_LESS_MAX_CAPACITY(60004,"当前库存必须小于最大容量"),
    //货道异常
    DEVICE_CHANNEL_NOT_EXISTS(70001,"当前设备的货道还未配置"),
    PRODUCT_NOT_EXIST_IN_CHANNEL(70002,"商品不存在于任一货道中"),
    CHANNEL_NOT_EXISTS(70003,"货道编号不存在"),
    //库存记录异常
    RECORD_HAS_EXISTS(80001,"当前设备的货道库存记录已经存在"),
    OUT_OF_STOCK(80002,"当前设备货道的库存小于变更数量，无法操作"),
    EXCEEDING_MAX_RANGE(80003,"库存加变更数量超出当前设备货道的最大容量，无法操作"),
    RECORD_NOT_EXISTS(80004,"当前设备的货道库存记录不存在"),
    UPDATED_STOCK_LESS_THAN_ZERO(80005,"更新后的库存小于0"),
    UPDATED_STOCK_BETTER_THAN_MAX(80006,"更新后的库存大于最大容量")
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
