package com.cqz.beverage.model.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改用户信息的请求参数
 */
@Data
public class MotifyUserRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private String avatar;
}
