package com.cqz.beverage.controller;

import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.dto.LoginResponseDTO;
import com.cqz.beverage.model.dto.MotifyPasswordDTO;
import com.cqz.beverage.model.dto.RegisterResponseDTO;
import com.cqz.beverage.model.vo.MotifyPasswordRequest;
import com.cqz.beverage.model.vo.MotifyUserRequest;
import com.cqz.beverage.model.vo.UserLoginRequest;
import com.cqz.beverage.model.vo.UserRegisterRequest;
import com.cqz.beverage.service.UserService;
import com.cqz.beverage.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result<RegisterResponseDTO> motifyUserInfo(@RequestBody MotifyUserRequest  motifyUserRequest, HttpServletRequest request) {
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
}
