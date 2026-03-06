package com.cqz.beverage.model.dto;

import com.cqz.beverage.model.User;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录相应DTO
 */
@Data
public class LoginResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private long expire;
    private User userInfo;
}
