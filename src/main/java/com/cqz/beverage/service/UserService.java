package com.cqz.beverage.service;

import com.cqz.beverage.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.beverage.model.dto.LoginResponseDTO;
import com.cqz.beverage.model.dto.RegisterResponseDTO;

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

}
