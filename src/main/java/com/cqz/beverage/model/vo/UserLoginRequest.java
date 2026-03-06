package com.cqz.beverage.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求参数
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;

}
