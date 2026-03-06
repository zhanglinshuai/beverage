package com.cqz.beverage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.model.UserRole;
import com.cqz.beverage.service.UserRoleService;
import com.cqz.beverage.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author zhanglinshuai
* @description 针对表【user_role(用户角色表)】的数据库操作Service实现
* @createDate 2026-03-06 14:45:41
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




