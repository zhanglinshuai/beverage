package com.cqz.beverage.model.dto.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.cqz.beverage.model.User;
import lombok.Data;

import java.util.Date;
@Data
public class UserDTO {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */

    private String username;
    /**
     * 真实姓名
     */

    private String realName;

    /**
     * 手机号
     */

    private String phone;

    /**
     * 邮箱
     */

    private String email;

    /**
     * 头像
     */

    private String avatar;

    /**
     * 账号状态 0-正常  1-禁用
     */

    private Integer status;

    /**
     * 最后登录时间
     */

    private Date lastLoginTime;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 更新时间
     */

    private Date updateTime;

    private Integer isDelete;

    private String roleCode;

    public static UserDTO userDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRealName(user.getRealName());
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getEmail());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setStatus(user.getStatus());
        userDTO.setLastLoginTime(user.getLastLoginTime());
        userDTO.setCreateTime(user.getCreateTime());
        userDTO.setUpdateTime(user.getUpdateTime());
        userDTO.setIsDelete(user.getIsDelete());
        return userDTO;
    }
}
