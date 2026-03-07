package com.cqz.beverage.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改密码的响应参数
 */
@Data
public class MotifyPasswordDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean isMotified;
    private String oldPassword;
    private String motifiedPassword;


    public static MotifyPasswordDTO fromEntity(String oldPassword,String motifiedPassword) {
        MotifyPasswordDTO dto = new MotifyPasswordDTO();
        dto.setMotified(true);
        dto.setOldPassword(oldPassword);
        dto.setMotifiedPassword(motifiedPassword);
        return dto;
    }
}
