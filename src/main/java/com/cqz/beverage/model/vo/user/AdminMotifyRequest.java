package com.cqz.beverage.model.vo.user;

import com.cqz.beverage.model.User;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员修改用户信息请求参数
 */
@Data
public class AdminMotifyRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private User user;

    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private String avatar;
    private int status;
    private int isDelete;
    private String role;
}
