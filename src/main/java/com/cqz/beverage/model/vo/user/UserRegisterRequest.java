package com.cqz.beverage.model.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求参数
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID =  1L;

    private String username;
    private String password;
    private String checkPassword;
    private String phone;
    private String email;
}
