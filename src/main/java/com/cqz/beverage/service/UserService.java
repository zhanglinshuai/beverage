package com.cqz.beverage.service;

import com.cqz.beverage.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhanglinshuai
* @description 针对表【user(所有角色共用一张表，通过角色区分身份)】的数据库操作Service
* @createDate 2026-03-06 14:16:00
*/
public interface UserService extends IService<User> {

}
