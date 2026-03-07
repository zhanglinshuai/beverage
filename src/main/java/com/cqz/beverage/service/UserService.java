package com.cqz.beverage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.beverage.model.dto.user.AdminUserInfo;
import com.cqz.beverage.model.dto.user.LoginResponseDTO;
import com.cqz.beverage.model.dto.user.MotifyPasswordDTO;
import com.cqz.beverage.model.dto.user.RegisterResponseDTO;
import com.cqz.beverage.model.vo.user.AdminMotifyRequest;
import com.cqz.beverage.model.vo.user.MotifyPasswordRequest;
import com.cqz.beverage.model.vo.user.MotifyUserRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

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
    User motifyUserInfo(MotifyUserRequest motifyUserRequest,String token);

    /**
     * 修改用户密码
     * @param motifyPasswordRequest
     * @return
     */
    MotifyPasswordDTO motifyPassword(MotifyPasswordRequest motifyPasswordRequest);

    /**
     * 管理员查看所有用户信息(分页查询）
     * @param request
     * @param pageRequest
     * @return
     */
    IPage<AdminUserInfo> AdminGetUserInfo(HttpServletRequest request, PageRequest  pageRequest);

    /**
     * 管理员查看用户详情信息
     * @param request
     * @param userId
     * @return
     */
    AdminUserInfo getUserDetail(HttpServletRequest request,Long userId);

    /**
     * 管理员修改用户信息
     * @param request
     * @param adminMotifyRequest 修改后的用户信息
     * @return
     */
    AdminUserInfo AdminMotifyUserInfo(HttpServletRequest request, AdminMotifyRequest  adminMotifyRequest);
}
