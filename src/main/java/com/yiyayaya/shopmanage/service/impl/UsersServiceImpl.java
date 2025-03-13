package com.yiyayaya.shopmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.Dto.UserSearchObj;
import com.yiyayaya.shopmanage.entity.Users;
import com.yiyayaya.shopmanage.exception.AuthException;
import com.yiyayaya.shopmanage.exception.ServiceException;
import com.yiyayaya.shopmanage.exception.ValidationException;
import com.yiyayaya.shopmanage.mapper.UsersMapper;
import com.yiyayaya.shopmanage.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiyayaya.shopmanage.utils.MD5Util;
import com.yiyayaya.shopmanage.utils.ValidationUtil;
import com.yiyayaya.shopmanage.entity.vo.UserVO;
import com.yiyayaya.shopmanage.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiyayaya.shopmanage.entity.Dto.PasswordUpdateDTO;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

  @Autowired
  private UsersMapper usersMapper;

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  public UserVO login(String username, String password) {
    // 参数校验
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      throw new ValidationException("用户名或密码不能为空");
    }

    // 对密码进行MD5加密
    String encryptedPassword = MD5Util.getMD5(password);
    log.debug("加密后的密码: {}", encryptedPassword);

    // 查询用户
    QueryWrapper<Users> wrapper = new QueryWrapper<>();
    wrapper.eq("username", username).eq("password", encryptedPassword);
    Users user = getOne(wrapper);

    // 用户不存在或密码错误
    if (user == null) {
      log.warn("用户登录失败: {}", username);
      throw new AuthException("用户名或密码错误");
    }

    // 用户被禁用
    if (user.getIssuse() != 1) {
      log.warn("用户登录失败: {}", username);
      throw new AuthException("该用户已被禁止登录");
    }
    if(user.getRole() > 2){
      log.warn("用户登录失败: {}", username);
      throw new AuthException("该用户没有权限");
    }

    // 登录成功
    log.info("用户登录成功: {}", username);
    String token = jwtUtil.createToken(user);
    UserVO userVO = UserVO.fromUser(user);
    userVO.setToken(token);
    return userVO;
  }

  @Override
  public boolean saveOrUpdateUser(Users user, Integer currentUserId) {
    // 参数校验
    if (StringUtils.isBlank(user.getUsername()) || (user.getUserId() == null && StringUtils.isBlank(user.getPassword())) ||
        StringUtils.isBlank(user.getEmail()) || StringUtils.isBlank(user.getImage()) ||
        StringUtils.isBlank(user.getPhone()) || user.getRole() == null) {
      throw new ValidationException("用户信息不完整");
    }

    // 验证手机号和邮箱格式
    if (!ValidationUtil.isValidPhone(user.getPhone())) {
      throw new ValidationException("手机号格式不正确");
    }
    if (!ValidationUtil.isValidEmail(user.getEmail())) {
      throw new ValidationException("邮箱格式不正确");
    }

    if (user.getUserId() != null) {
      // 检查用户是否存在
      Users existingUser = usersMapper.selectById(user.getUserId());
      if (existingUser == null) {
        throw new ServiceException("用户不存在");
      }

      // 检查当前用户权限
      Users currentUser = usersMapper.selectById(currentUserId);
      if (currentUser == null) {
        throw new ServiceException("当前用户不存在");
      }
      if (currentUserId != 1 && currentUser.getRole() >= existingUser.getRole()) {
        throw new AuthException("没有权限修改该用户: " + existingUser.getUsername());
      }

      QueryWrapper<Users> usernameWrapper = new QueryWrapper<>();
      usernameWrapper.eq("username", user.getUsername()).ne("user_id", user.getUserId());
      if (usersMapper.selectCount(usernameWrapper) > 0) {
        throw new ServiceException("用户名已存在");
      }
      // 更新用户信息
      user.setPassword(existingUser.getPassword());
      return usersMapper.updateById(user) > 0;
    } else {
      // 检查用户名是否已存在
      QueryWrapper<Users> usernameWrapper = new QueryWrapper<>();
      usernameWrapper.eq("username", user.getUsername());
      if (usersMapper.selectCount(usernameWrapper) > 0) {
        throw new ServiceException("用户名已存在");
      }

      // 加密密码并保存用户
      try {
        String encryptedPassword = MD5Util.getMD5(user.getPassword());
        user.setPassword(encryptedPassword);
        return usersMapper.insert(user) > 0;
      } catch (Exception e) {
        log.error("用户保存失败: {}", user.getUsername(), e);
        throw new ServiceException("系统异常，请稍后重试");
      }
    }
  }

  @Override
  public Pages<UserVO> getAllUsers(UserSearchObj userSearchObj) {
    // 创建分页对象
    Page<Users> page = new Page<>(userSearchObj.getPageNum(), userSearchObj.getPageSize());

    // 创建查询条件
    QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
    if (StringUtils.isNotBlank(userSearchObj.getUsername())) {
      queryWrapper.like("username", userSearchObj.getUsername());
    }
    if (userSearchObj.getRole() != null && userSearchObj.getRole() != -1) {
      queryWrapper.eq("role", userSearchObj.getRole());
    }
    queryWrapper.orderByAsc("role", "user_id");

    // 执行分页查询
    Page<Users> userPage = usersMapper.selectPage(page, queryWrapper);

    // 转换为 UserVO 列表
    List<UserVO> userVOList = new ArrayList<>();
    for (Users user : userPage.getRecords()) {
      UserVO userVO = UserVO.fromUser(user);
      userVOList.add(userVO);
    }

    // 创建并返回分页响应对象
    return new Pages<>(userVOList, userPage.getTotal(), userPage.getCurrent(), userSearchObj.getPageSize());
  }

  @Override
  public boolean deleteUser(Integer userId, Integer currentUserId) {
    // 检查是否尝试删除自己
    if (userId == currentUserId) {
      throw new AuthException("没有权限删除该用户");
    }

    // 检查用户是否存在
    Users currentUser = usersMapper.selectById(currentUserId);
    Users userToDelete = usersMapper.selectById(userId);
    if (currentUser == null || userToDelete == null) {
      throw new ServiceException("用户不存在");
    }

    // 权限检查
    if (currentUserId != 1 && currentUser.getRole() >= userToDelete.getRole()) {
      throw new AuthException("没有权限删除该用户: " + userToDelete.getUsername());
    }

    // 执行删除
    try {
      return usersMapper.deleteById(userId) > 0;
    } catch (Exception e) {
      log.error("删除用户失败: {}", userId, e);
      throw new ServiceException("删除用户失败，请稍后重试");
    }
  }

  @Override
  public boolean disableUserById(Integer userId, Integer currentUserId, Integer status) {
    // 检查是否尝试禁用自己
    if (userId == currentUserId) {
      throw new AuthException("没有权限操作该用户");
    }

    // 检查用户是否存在
    Users currentUser = usersMapper.selectById(currentUserId);
    Users userToDisable = usersMapper.selectById(userId);
    if (currentUser == null || userToDisable == null) {
      throw new ServiceException("用户不存在");
    }

    // 权限检查
    if (currentUserId != 1 && currentUser.getRole() >= userToDisable.getRole()) {
      throw new AuthException("没有权限操作该用户: " + userToDisable.getUsername());
    }

    // 更新用户状态
    try {
      userToDisable.setIssuse(status);
      return usersMapper.updateById(userToDisable) > 0;
    } catch (Exception e) {
      log.error("操作用户失败: {}", userId, e);
      throw new ServiceException("操作用户失败，请稍后重试");
    }
  }

  @Override
  public UserVO getUserById(Integer userId) {
    Users user = baseMapper.selectById(userId);
    if (user != null) {
      return UserVO.fromUser(user);
    }
    return null;
  }

  @Override
  public boolean updatePassword(PasswordUpdateDTO passwordUpdateDTO, Integer currentUserId) {
    // 参数校验
    if (passwordUpdateDTO.getUserId() == null || StringUtils.isBlank(passwordUpdateDTO.getOldPassword()) || StringUtils.isBlank(passwordUpdateDTO.getNewPassword())) {
      throw new ValidationException("参数不完整");
    }

    if (!passwordUpdateDTO.getUserId().equals(currentUserId) && currentUserId != 1) {
      throw new AuthException("没有权限修改该用户密码");
    }

    // 检查用户是否存在
    Users user = usersMapper.selectById(passwordUpdateDTO.getUserId());
    if (user == null) {
      throw new ServiceException("用户不存在");
    }

    // 验证旧密码是否正确
    String encryptedOldPassword = MD5Util.getMD5(passwordUpdateDTO.getOldPassword());
    if (!user.getPassword().equals(encryptedOldPassword)) {
      throw new AuthException("旧密码不正确");
    }

    // 更新密码
    try {
      String encryptedNewPassword = MD5Util.getMD5(passwordUpdateDTO.getNewPassword());
      user.setPassword(encryptedNewPassword);
      return usersMapper.updateById(user) > 0;
    } catch (Exception e) {
      log.error("密码修改失败: {}", passwordUpdateDTO.getUserId(), e);
      throw new ServiceException("密码修改失败，请稍后重试");
    }
  }

  @Override
  public String getUsernameById(Integer userId) {
    Users user = usersMapper.selectById(userId);
    return user != null ? user.getUsername() : null;
  }
}

