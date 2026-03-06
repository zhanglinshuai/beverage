package com.cqz.beverage.model.dto;

import com.cqz.beverage.model.User;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员查看用户信息响应参数
 */
@Data
public class AdminUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
    private String role;


    public static AdminUserInfo fromEntity(User user){
        AdminUserInfo userInfo = new AdminUserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getRealName());
        userInfo.setPhone(user.getPhone());
        userInfo.setEmail(user.getEmail());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setStatus(user.getStatus());
        return userInfo;
    }
}
