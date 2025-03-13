package com.yiyayaya.shopmanage.service;

import com.yiyayaya.shopmanage.entity.Dto.UserSearchObj;
import com.yiyayaya.shopmanage.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.vo.UserVO;
import com.yiyayaya.shopmanage.entity.Dto.PasswordUpdateDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
public interface IUsersService extends IService<Users> {
  UserVO login(String username, String password);
  boolean saveOrUpdateUser(Users user,Integer currentUserId);
  Pages<UserVO> getAllUsers(UserSearchObj userSearchObj);
  boolean deleteUser(Integer userId, Integer currentUserId);
  boolean disableUserById(Integer userId, Integer currentUserId,Integer status);

  /**
   * 根据用户ID获取用户信息
   * @param userId 用户ID
   * @return UserVO 用户信息
   */
  UserVO getUserById(Integer userId);

  boolean updatePassword(PasswordUpdateDTO passwordUpdateDTO, Integer currentUserId);

  /**
   * 根据用户ID获取用户名
   * @param userId 用户ID
   * @return 用户名
   */
  String getUsernameById(Integer userId);
}
