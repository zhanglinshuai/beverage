package com.cqz.beverage.model.dto.user;

import com.cqz.beverage.model.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户注册响应参数
 */
@Data
public class RegisterResponseDTO implements Serializable {
    private long userId;
    private String username;
    private String phone;
    private String email;
    private Integer status;
    private Date createTime;
    public static String desensitizePhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    // 从Entity转换为DTO（避免手动set）
    public static RegisterResponseDTO fromEntity(User entity) {
        RegisterResponseDTO dto = new RegisterResponseDTO();
        dto.setUserId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setPhone(desensitizePhone(entity.getPhone()));
        dto.setEmail(entity.getEmail());// 脱敏处理
        dto.setStatus(entity.getStatus());
        dto.setCreateTime(entity.getCreateTime());
        return dto;
    }
}
