package com.cqz.beverage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.model.User;
import com.cqz.beverage.service.UserService;
import com.cqz.beverage.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author zhanglinshuai
* @description 针对表【user(所有角色共用一张表，通过角色区分身份)】的数据库操作Service实现
* @createDate 2026-03-06 14:16:00
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




