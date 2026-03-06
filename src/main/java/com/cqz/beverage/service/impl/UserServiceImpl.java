package com.cqz.beverage.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.constant.JwtConstant;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.mapper.UserMapper;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.dto.LoginResponseDTO;
import com.cqz.beverage.model.dto.RegisterResponseDTO;
import com.cqz.beverage.model.vo.MotifyUserRequest;
import com.cqz.beverage.service.UserService;
import com.cqz.beverage.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
* @author zhanglinshuai
* @description 针对表【user(所有角色共用一张表，通过角色区分身份)】的数据库操作Service实现
* @createDate 2026-03-06 14:16:00
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;


    @Override
    public RegisterResponseDTO userRegister(String username, String password, String checkPassword, String phone, String email) {
        //任意一个参数为空则会抛出该异常
        if(StrUtil.hasEmpty(username, password, checkPassword, phone, email)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if(username.isEmpty() || username.length()>20){
            throw new BusinessException(BusinessExceptionEnum.PARAM_FORMAT_ERROR.getCode(),"用户名的长度不得为空或者不能超过20个字符");
        }
        if(password.length() < 6){
            throw new BusinessException(BusinessExceptionEnum.PARAM_FORMAT_ERROR.getCode(),"密码的长度不得小于6个字符");
        }
        //密码和二次密码是否相同
        if(!StrUtil.equals(password,checkPassword)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR.getCode(),"两次输入密码错误");
        }
        //校验用户名是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",username);
        User user = userMapper.selectOne(userQueryWrapper);
        if(user!=null){
            throw new BusinessException(BusinessExceptionEnum.DATA_DUPLICATE.getCode(),username+"该用户名已存在");
        }
        //如果用户名不存在，对密码进行加密
        User newUser = new User();
        String encryptedPassword = BCrypt.hashpw(password);
        newUser.setUsername(username);
        newUser.setPassword(encryptedPassword);
        newUser.setPhone(phone);
        newUser.setEmail(email);
        newUser.setStatus(0);
        if(save(newUser)){
            return RegisterResponseDTO.fromEntity(newUser);
        }else {
            return null;
        }
    }

    @Override
    public LoginResponseDTO userLogin(String username, String password) {
        if(StrUtil.hasEmpty(username, password)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if(username.isEmpty() || username.length()>20){
            throw new BusinessException(BusinessExceptionEnum.PARAM_FORMAT_ERROR.getCode(),"用户名的长度不得为空或者不能超过20个字符");
        }
        if(password.length() < 6){
            throw new BusinessException(BusinessExceptionEnum.PARAM_FORMAT_ERROR.getCode(),"密码的长度不得小于6个字符");
        }
        //检验用户是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",username);
        User user = userMapper.selectOne(userQueryWrapper);
        //用户不存在
        if(user==null){
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        //获取密码，将数据库中存储的密文密码和明文密码进行比较
        String encryptedPassword = user.getPassword();
        if(!BCrypt.checkpw(password,encryptedPassword)){
            throw new BusinessException(BusinessExceptionEnum.USER_PASSWORD_ERROR);
        }
        //账号被禁用
        if(user.getStatus()==1){
            throw new BusinessException(BusinessExceptionEnum.USER_ACCOUNT_DISABLED);
        }
        //如果用户存在生成token
        String token = jwtTokenUtil.generateToken(user.getId(), user.getUsername());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(token);
        loginResponseDTO.setExpire(JwtConstant.EXPIRATION);
        loginResponseDTO.setUserInfo(user);
        return loginResponseDTO;
    }

    @Override
    public User getCurrentUser(String token) {
        Object userId = jwtTokenUtil.getUserIdFromToken(token);
        Long id = (Long) userId;
        return userMapper.selectById(id);
    }

    @Override
    public RegisterResponseDTO motifyUserInfo(MotifyUserRequest motifyUserRequest,String token) {

        User currentUser = getCurrentUser(token);
        if(currentUser==null){
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        if(motifyUserRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        String username = motifyUserRequest.getUsername();
        String realName = motifyUserRequest.getRealName();
        String email = motifyUserRequest.getEmail();
        String avatar = motifyUserRequest.getAvatar();
        String phone = motifyUserRequest.getPhone();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",username);
        User user = userMapper.selectOne(userQueryWrapper);
        if(user!=null){
            throw new BusinessException(BusinessExceptionEnum.USER_NAME_DUPLICATE);
        }
        if(!currentUser.getUsername().equals(username)){
            currentUser.setUsername(username);
        }
        if(currentUser.getRealName().equals(realName)){
            currentUser.setRealName(realName);
        }
        if(!currentUser.getEmail().equals(email)){
            currentUser.setEmail(email);
        }
        if(!currentUser.getAvatar().equals(avatar)){
            currentUser.setAvatar(avatar);
        }
        if(!currentUser.getPhone().equals(phone)){
            currentUser.setPhone(phone);
        }
        userMapper.updateById(currentUser);
        return RegisterResponseDTO.fromEntity(currentUser);
    }


}




