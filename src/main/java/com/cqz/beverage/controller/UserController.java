package com.cqz.beverage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.dto.user.*;
import com.cqz.beverage.model.vo.user.*;
import com.cqz.beverage.service.UserService;
import com.cqz.beverage.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制层
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @PostMapping("/register")
    public Result<RegisterResponseDTO> userRegister(@Validated @RequestBody UserRegisterRequest userRegisterRequest) {
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String email = userRegisterRequest.getEmail();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String phone = userRegisterRequest.getPhone();
        RegisterResponseDTO registerResponseDTO = userService.userRegister(username, password, checkPassword, phone, email);
        if(registerResponseDTO == null) {
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(),"用户创建失败");
        }
        return Result.success(registerResponseDTO);
    }

    @PostMapping("/login")
    public Result<LoginResponseDTO> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        LoginResponseDTO loginResponseDTO = userService.userLogin(username, password);
        return Result.success(loginResponseDTO);

    }

    @GetMapping("getUserInfo")
    public Result<RegisterResponseDTO> getUserInfo(HttpServletRequest request) {
        String header = request.getHeader(jwtTokenUtil.getHeader());
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        if(currentUser == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        RegisterResponseDTO registerResponseDTO = RegisterResponseDTO.fromEntity(currentUser);

        return Result.success(registerResponseDTO);
    }


    @PutMapping("motifyUserInfo")
    public Result<RegisterResponseDTO> motifyUserInfo(@RequestBody MotifyUserRequest motifyUserRequest, HttpServletRequest request) {
        if (motifyUserRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        String header = request.getHeader(jwtTokenUtil.getHeader());
        String token = jwtTokenUtil.getTokenFromHeader(header);
        RegisterResponseDTO registerResponseDTO = userService.motifyUserInfo(motifyUserRequest,token);
        return Result.success(registerResponseDTO);
    }

    @PutMapping("motifyPassword")
    public Result<MotifyPasswordDTO> motifyPassword(@RequestBody MotifyPasswordRequest motifyPasswordRequest) {
        if(motifyPasswordRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        MotifyPasswordDTO motifyPasswordDTO = userService.motifyPassword(motifyPasswordRequest);
        if(motifyPasswordDTO == null) {
            throw new BusinessException(BusinessExceptionEnum.MOTIFY_PASSWORD_ERROR);
        }
        return Result.success(motifyPasswordDTO);
    }

    @GetMapping("/admin/getUserInfo")
    public Result<PageResponseDTO<AdminUserInfo>> AdminGetUserInfo(HttpServletRequest request, PageRequest pageRequest){
        if (pageRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (request==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        List<AdminUserInfo> adminUserInfos = userService.AdminGetUserInfo(request, pageRequest);
        Page<AdminUserInfo> adminUserInfoPage = new Page<>();
        //配置总条数
        adminUserInfoPage.setTotal((long) pageNum *pageSize);
        //配置每页条数
        adminUserInfoPage.setSize(pageSize);
        //配置页码
        adminUserInfoPage.setCurrent(pageNum);
        //配置记录
        adminUserInfoPage.setRecords(adminUserInfos);
        //将Page<AdminUserInfo>封装为PageResponseDTO<AdminUserInfo>类型
        PageResponseDTO<AdminUserInfo> result = PageResponseDTO.buildPageResponseDTO(adminUserInfoPage);
        return Result.success(result);
    }

    @PutMapping("/admin/motifyUserInfo")
    public Result<AdminUserInfo> AdminMotifyUserInfo(@RequestBody AdminMotifyRequest adminMotifyRequest, HttpServletRequest request, String userName) {
        if (adminMotifyRequest==null || request==null ||  userName==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        AdminUserInfo adminUserInfo = userService.AdminMotifyUserInfo(request, adminMotifyRequest, userName);
        if(adminUserInfo == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        return Result.success(adminUserInfo);
    }
}
