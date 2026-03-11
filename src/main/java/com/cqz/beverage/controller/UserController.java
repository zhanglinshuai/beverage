package com.cqz.beverage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.mapper.UserRoleMapper;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.UserRole;
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
    private UserRoleMapper userRoleMapper;

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

    @GetMapping("/getUserInfo")
    public Result<UserDTO> getUserInfo(HttpServletRequest request) {
        String header = request.getHeader(jwtTokenUtil.getHeader());
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        if(currentUser == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        UserDTO userDTO = UserDTO.userDTO(currentUser);
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<UserRole>().eq("user_id", currentUser.getId());
        UserRole userRole = userRoleMapper.selectOne(queryWrapper);
        if(userRole == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        userDTO.setRoleCode(userRole.getRoleCode());
        return Result.success(userDTO);
    }


    @PutMapping("/motifyUserInfo")
    public Result<User> motifyUserInfo(@RequestBody MotifyUserRequest motifyUserRequest, HttpServletRequest request) {
        if (motifyUserRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        String header = request.getHeader(jwtTokenUtil.getHeader());
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User user = userService.motifyUserInfo(motifyUserRequest,token);
        return Result.success(user);
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
        IPage<AdminUserInfo> resultPage = userService.AdminGetUserInfo(request, pageRequest);
        PageResponseDTO<AdminUserInfo> result = new PageResponseDTO<>();
        result.setRecords(resultPage.getRecords());
        result.setTotal(resultPage.getTotal());
        result.setPageNum((int) resultPage.getSize());
        result.setPageNum((int) resultPage.getCurrent());

        return Result.success(result);
    }

    @PutMapping("/admin/motifyUserInfo")
    public Result<AdminUserInfo> AdminMotifyUserInfo(@RequestBody AdminMotifyRequest adminMotifyRequest, HttpServletRequest request) {
        if (adminMotifyRequest==null || request==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        AdminUserInfo adminUserInfo = userService.AdminMotifyUserInfo(request, adminMotifyRequest);
        if(adminUserInfo == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        return Result.success(adminUserInfo);
    }

    @GetMapping("/admin/userDetail")
    public Result<AdminUserInfo> getAdminUserDetail(HttpServletRequest request,Long userId) {
        if (userId==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"参数为空");
        }
        if(request==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"参数为空");
        }
        AdminUserInfo result = userService.getUserDetail(request, userId);
        if (result == null) {
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(),"查询失败");
        }

        return Result.success(result);
    }

    @GetMapping("/admin/search")
    public Result<PageResponseDTO<AdminUserInfo>> AdminSearchUserInfo(SearchUserRequest searchUserRequest,HttpServletRequest request){
        if (searchUserRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (request==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        IPage<AdminUserInfo> adminUserInfoIPage = userService.searchUser(searchUserRequest,request);
        PageResponseDTO<AdminUserInfo> result = new PageResponseDTO<>();
        result.setRecords(adminUserInfoIPage.getRecords());
        result.setTotal(adminUserInfoIPage.getTotal());
        result.setPageNum((int) adminUserInfoIPage.getSize());
        result.setPageNum((int) adminUserInfoIPage.getCurrent());

        return Result.success(result);
    }
}
