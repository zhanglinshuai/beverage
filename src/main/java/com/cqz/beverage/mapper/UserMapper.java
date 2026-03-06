package com.cqz.beverage.mapper;

import com.cqz.beverage.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author zhanglinshuai
* @description 针对表【user(所有角色共用一张表，通过角色区分身份)】的数据库操作Mapper
* @createDate 2026-03-06 14:16:00
* @Entity com.cqz.beverage.model.User
*/
public interface UserMapper extends BaseMapper<User> {

}




