package com.cqz.beverage.service;

import com.cqz.beverage.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.beverage.model.dto.LoginResponseDTO;
import com.cqz.beverage.model.dto.MotifyPasswordDTO;
import com.cqz.beverage.model.dto.RegisterResponseDTO;
import com.cqz.beverage.model.vo.MotifyPasswordRequest;
import com.cqz.beverage.model.vo.MotifyUserRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author zhanglinshuai
* @description 针对表【user(所有角色共用一张表，通过角色区分身份)】的数据库操作Service
* @createDate 2026-03-06 14:16:00
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param username
     * @param password
     * @param checkPassword
     * @param phone
     * @param email
     * @return
     */
    RegisterResponseDTO userRegister(String username, String password, String checkPassword, String phone, String email);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    LoginResponseDTO userLogin(String username, String password);


    /**
     * 获取当前登录用户信息
     * @param token
     * @return
     */
    User getCurrentUser(String token);


    /**
     * 修改用户信息
     * @param motifyUserRequest
     * @return
     */
    RegisterResponseDTO motifyUserInfo(MotifyUserRequest motifyUserRequest,String token);

    /**
     * 修改用户密码
     * @param motifyPasswordRequest
     * @return
     */
    MotifyPasswordDTO motifyPassword(MotifyPasswordRequest motifyPasswordRequest);
}
