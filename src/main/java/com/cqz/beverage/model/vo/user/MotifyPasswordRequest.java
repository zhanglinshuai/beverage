package com.cqz.beverage.model.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改密码的请求参数
 */
@Data
public class MotifyPasswordRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private String password;
    private String motifiedPassword;

}
