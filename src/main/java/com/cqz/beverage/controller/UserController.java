package com.cqz.beverage.controller;

import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.dto.LoginResponseDTO;
import com.cqz.beverage.model.dto.RegisterResponseDTO;
import com.cqz.beverage.model.vo.UserLoginRequest;
import com.cqz.beverage.model.vo.UserRegisterRequest;
import com.cqz.beverage.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制层
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
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
}
