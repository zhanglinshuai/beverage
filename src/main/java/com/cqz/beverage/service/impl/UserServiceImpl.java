package com.cqz.beverage.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.constant.JwtConstant;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.mapper.UserMapper;
import com.cqz.beverage.mapper.UserRoleMapper;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.UserRole;
import com.cqz.beverage.model.dto.user.AdminUserInfo;
import com.cqz.beverage.model.dto.user.LoginResponseDTO;
import com.cqz.beverage.model.dto.user.MotifyPasswordDTO;
import com.cqz.beverage.model.dto.user.RegisterResponseDTO;
import com.cqz.beverage.model.vo.user.*;
import com.cqz.beverage.service.UserService;
import com.cqz.beverage.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhanglinshuai
 * @description 针对表【user(所有角色共用一张表，通过角色区分身份)】的数据库操作Service实现
 * @createDate 2026-03-06 14:16:00
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public RegisterResponseDTO userRegister(String username, String password, String checkPassword, String phone, String email) {
        //任意一个参数为空则会抛出该异常
        if (StrUtil.hasEmpty(username, password, checkPassword, phone, email)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (username.isEmpty() || username.length() > 20) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_FORMAT_ERROR.getCode(), "用户名的长度不得为空或者不能超过20个字符");
        }
        if (password.length() < 6) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_FORMAT_ERROR.getCode(), "密码的长度不得小于6个字符");
        }
        //密码和二次密码是否相同
        if (!StrUtil.equals(password, checkPassword)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR.getCode(), "两次输入密码错误");
        }
        //校验用户名是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user != null) {
            throw new BusinessException(BusinessExceptionEnum.DATA_DUPLICATE.getCode(), username + "该用户名已存在");
        }
        //如果用户名不存在，对密码进行加密
        User newUser = new User();
        String encryptedPassword = BCrypt.hashpw(password);
        newUser.setUsername(username);
        newUser.setPassword(encryptedPassword);
        newUser.setPhone(phone);
        newUser.setEmail(email);
        newUser.setStatus(0);
        newUser.setIsDelete(0);
        if (save(newUser)) {
            return RegisterResponseDTO.fromEntity(newUser);
        } else {
            return null;
        }
    }

    @Override
    public LoginResponseDTO userLogin(String username, String password) {
        if (StrUtil.hasEmpty(username, password)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (username.isEmpty() || username.length() > 20) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_FORMAT_ERROR.getCode(), "用户名的长度不得为空或者不能超过20个字符");
        }
        if (password.length() < 6) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_FORMAT_ERROR.getCode(), "密码的长度不得小于6个字符");
        }
        //检验用户是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userMapper.selectOne(userQueryWrapper);
        //用户不存在
        if (user == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        //获取密码，将数据库中存储的密文密码和明文密码进行比较
        String encryptedPassword = user.getPassword();
        if (!BCrypt.checkpw(password, encryptedPassword)) {
            throw new BusinessException(BusinessExceptionEnum.USER_PASSWORD_ERROR);
        }
        //账号被禁用
        if (user.getStatus() == 1) {
            throw new BusinessException(BusinessExceptionEnum.USER_ACCOUNT_DISABLED);
        }
        if (user.getIsDelete() == 1) {
            throw new BusinessException(BusinessExceptionEnum.USER_ACCOUNT_DISABLED.getCode(), "该用户已被删除");
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
        Long id = (userId instanceof Number) ? ((Number) userId).longValue() : null;
        return userMapper.selectById(id);
    }

    @Override
    public User motifyUserInfo(MotifyUserRequest motifyUserRequest, String token) {

        User currentUser = getCurrentUser(token);
        if (currentUser == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        if (motifyUserRequest == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        String username = motifyUserRequest.getUsername();
        String realName = motifyUserRequest.getRealName();
        String email = motifyUserRequest.getEmail();
        String avatar = motifyUserRequest.getAvatar();
        String phone = motifyUserRequest.getPhone();
        //如果当前登录用户名称和修改后的用户名不相同，判断修改后的用户名是否已存在
        if (!currentUser.getUsername().equals(username)) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("username", username);
            User user = userMapper.selectOne(userQueryWrapper);
            if (user != null) {
                throw new BusinessException(BusinessExceptionEnum.USER_NAME_DUPLICATE);
            }
            currentUser.setUsername(username);
        }
        currentUser.setRealName(realName);
        currentUser.setEmail(email);
        currentUser.setAvatar(avatar);
        currentUser.setPhone(phone);
        userMapper.updateById(currentUser);
        return currentUser;
    }

    @Override
    public MotifyPasswordDTO motifyPassword(MotifyPasswordRequest motifyPasswordRequest) {
        if (motifyPasswordRequest == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        Long userId = motifyPasswordRequest.getUserId();
        String password = motifyPasswordRequest.getPassword();
        String motifiedPassword = motifyPasswordRequest.getMotifiedPassword();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        String oldPassword = user.getPassword();
        //校验原密码输入是否正确
        if (!BCrypt.checkpw(password, oldPassword)) {
            throw new BusinessException(BusinessExceptionEnum.USER_PASSWORD_ERROR);
        }
        //校验密码修改前后是否相同
        if (password.equals(motifiedPassword)) {
            throw new BusinessException(BusinessExceptionEnum.USER_PASSWORD_SAME);
        }
        //修改后的加密密码
        String motifiedSafetiedPassword = BCrypt.hashpw(motifiedPassword);
        user.setPassword(motifiedSafetiedPassword);
        userMapper.updateById(user);
        return MotifyPasswordDTO.fromEntity(password, motifiedPassword);
    }

    @Override
    public IPage<AdminUserInfo> AdminGetUserInfo(HttpServletRequest request, PageRequest pageRequest) {
        String header = request.getHeader(jwtTokenUtil.getHeader());
        String token = jwtTokenUtil.getTokenFromHeader(header);
        //判断是否为管理员
        if (!isAdmin(token)) {
            return null;
        }
        //如果是管理员，查询所有的用户信息
        //构建分页对象
        Page<User> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        //执行分页查询
        userMapper.selectPage(page, null);
        //转化为AdminUserInfo
        Page<AdminUserInfo> resultPage = new Page<>();
        resultPage.setTotal(page.getTotal());
        resultPage.setSize(page.getSize());
        resultPage.setCurrent(page.getCurrent());
        List<AdminUserInfo> dtoList = page.getRecords().stream().map(AdminUserInfo::fromEntity).collect(Collectors.toList());
        for (AdminUserInfo dto : dtoList) {
            QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
            userRoleQueryWrapper.eq("user_id", dto.getId());
            UserRole userRole = userRoleMapper.selectOne(userRoleQueryWrapper);
            if (userRole == null) {
                continue;
            }
            dto.setRole(userRole.getRoleCode());
        }
        resultPage.setRecords(dtoList);
        return resultPage;
    }

    @Override
    public AdminUserInfo getUserDetail(HttpServletRequest request, Long userId) {
        if (request == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (userId == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        //校验是否为管理员
        String header = request.getHeader(jwtTokenUtil.getHeader());
        String token = jwtTokenUtil.getTokenFromHeader(header);
        if (!isAdmin(token)) {
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        //查询用户信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", user.getId());
        UserRole userRole = userRoleMapper.selectOne(userRoleQueryWrapper);
        AdminUserInfo adminUserInfo = AdminUserInfo.fromEntity(user);
        adminUserInfo.setRole(userRole.getRoleCode());
        return adminUserInfo;
    }

    @Override
    //
    public AdminUserInfo AdminMotifyUserInfo(HttpServletRequest request, AdminMotifyRequest adminMotifyRequest) {
        String header = request.getHeader(jwtTokenUtil.getHeader());
        String token = jwtTokenUtil.getTokenFromHeader(header);
        //判断是否为管理员
        if (!isAdmin(token)) {
            return null;
        }
        // 1. 参数校验
        if (adminMotifyRequest == null || adminMotifyRequest.getUser() == null || adminMotifyRequest.getUser().getId() == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }

        Long userId = adminMotifyRequest.getUser().getId();

        // 2. 获取数据库中现有的用户信息 (作为比对基准)
        User existingUser = getById(userId);
        if (existingUser == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }


        // --- 逐个字段比对 ---

        // [用户名]
        String newUsername = adminMotifyRequest.getUsername();
        // 只有当新用户名不为空，且与旧用户名不同时，才更新
        if(StringUtils.isBlank(newUsername)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"用户名不能为空");
        }
        if (StringUtils.isNotBlank(newUsername) && !newUsername.equals(existingUser.getUsername())) {
            // 检查用户名是否重复 (仅在用户名真正改变时检查)
            User duplicate = userMapper.selectOne(new QueryWrapper<User>().eq("username", newUsername));
            if (duplicate != null) {
                throw new BusinessException(BusinessExceptionEnum.USER_NAME_DUPLICATE);
            }

            existingUser.setUsername(newUsername); // 更新本地对象以便后续返回

        }

        // [真实姓名]
        String newRealName = adminMotifyRequest.getRealName();
        if(StringUtils.isBlank(newRealName)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"真实姓名不能为空");
        }
        if (StringUtils.isNotBlank(newRealName) && !newRealName.equals(existingUser.getRealName())) {

            existingUser.setRealName(newRealName);

        }

        // [手机号]
        String newPhone = adminMotifyRequest.getPhone();
        if(StringUtils.isBlank(newPhone)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"手机号不能为空");
        }
        if (StringUtils.isNotBlank(newPhone) && !newPhone.equals(existingUser.getPhone())) {
            existingUser.setPhone(newPhone);
        }

        // [邮箱]
        String newEmail = adminMotifyRequest.getEmail();
        if(StringUtils.isBlank(newEmail)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"邮箱不能为空");
        }
        if (StringUtils.isNotBlank(newEmail) && !newEmail.equals(existingUser.getEmail())) {
            existingUser.setEmail(newEmail);
        }

        // [头像]
        String newAvatar = adminMotifyRequest.getAvatar();
        if(StringUtils.isBlank(newAvatar)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"头像不能为空");
        }
        if (StringUtils.isNotBlank(newAvatar) && !newAvatar.equals(existingUser.getAvatar())) {
            existingUser.setAvatar(newAvatar);
        }

        // [状态] (int 类型通常需要约定 0 是有效值还是默认值，这里假设前后端传值正确)
        if (adminMotifyRequest.getStatus() != existingUser.getStatus()) {
            existingUser.setStatus(adminMotifyRequest.getStatus());
        }

        // [是否删除]
        if (adminMotifyRequest.getIsDelete() != existingUser.getIsDelete()) {
            existingUser.setIsDelete(adminMotifyRequest.getIsDelete());
        }

        // 4. 执行 User 表更新
        userMapper.updateById(existingUser);
        AdminUserInfo adminUserInfo = AdminUserInfo.fromEntity(existingUser);
        // 5. 更新角色 (涉及关联表 UserRole)
        String newRole = adminMotifyRequest.getRole();
        if (StringUtils.isNotBlank(newRole)) {
            // 查询现有角色
            UserRole existingUserRole = userRoleMapper.selectOne(new QueryWrapper<UserRole>().eq("user_id", userId));

            if (existingUserRole == null) {
                // 如果原来没有角色，则新增
                UserRole newUserRole = new UserRole();
                newUserRole.setUserId(userId);
                newUserRole.setRoleCode(newRole);
                userRoleMapper.insert(newUserRole);
            } else if (!newRole.equals(existingUserRole.getRoleCode())) {
                // 如果原来有角色且不同，则更新
                existingUserRole.setRoleCode(newRole);
                userRoleMapper.updateById(existingUserRole);
            }
            // 更新本地对象用于返回
            adminUserInfo.setRole(newRole);
        } else {
            // 如果没有修改角色，尝试获取现有角色以便返回
            UserRole existingUserRole = userRoleMapper.selectOne(new QueryWrapper<UserRole>().eq("user_id", userId));
            if (existingUserRole != null) {
                adminUserInfo.setRole(existingUserRole.getRoleCode());
            }
        }

        // 6. 返回最新的用户信息
        return adminUserInfo;
    }

    @Override
    public IPage<AdminUserInfo> searchUser(SearchUserRequest searchUserRequest,HttpServletRequest servletRequest) {
        if (searchUserRequest == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        String header = servletRequest.getHeader(JwtConstant.HEADER);
        String token = jwtTokenUtil.getTokenFromHeader(header);
        if(!isAdmin(token)){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        //搜索词
        String keyword = searchUserRequest.getKeyword();
        PageRequest pageRequest = searchUserRequest.getPageRequest();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if(keyword != null){
            userQueryWrapper.like("username", keyword).or().like("real_name", keyword).or().like("phone", keyword);
        }
        Page<User> userPage = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        userMapper.selectPage(userPage, userQueryWrapper);
        Page<AdminUserInfo> resultPage = new Page<>();
        resultPage.setTotal(userPage.getTotal());
        resultPage.setSize(userPage.getSize());
        resultPage.setCurrent(userPage.getCurrent());
        List<AdminUserInfo> dtoList = userPage.getRecords().stream().map(AdminUserInfo::fromEntity).collect(Collectors.toList());
        for (AdminUserInfo dto : dtoList) {
            QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
            userRoleQueryWrapper.eq("user_id", dto.getId());
            UserRole userRole = userRoleMapper.selectOne(userRoleQueryWrapper);
            if (userRole == null) {
                continue;
            }
            dto.setRole(userRole.getRoleCode());
        }
        resultPage.setRecords(dtoList);
        return resultPage;
    }

    /**
     * 判断当前登录用户是否为管理员
     *
     * @param token
     * @return
     */
    private boolean isAdmin(String token) {
        User currentUser = getCurrentUser(token);
        Long id = currentUser.getId();
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", id);
        UserRole userRole = userRoleMapper.selectOne(userRoleQueryWrapper);
        if (userRole == null || !userRole.getRoleCode().equals("ADMIN")) {
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        return true;
    }


}




