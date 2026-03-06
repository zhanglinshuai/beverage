package com.cqz.beverage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.model.Role;
import com.cqz.beverage.service.RoleService;
import com.cqz.beverage.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author zhanglinshuai
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2026-03-06 14:40:26
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




