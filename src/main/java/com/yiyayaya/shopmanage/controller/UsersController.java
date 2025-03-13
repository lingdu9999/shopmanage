package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.entity.Dto.LoginObj;
import com.yiyayaya.shopmanage.entity.Dto.PasswordUpdateDTO;
import com.yiyayaya.shopmanage.entity.Dto.UserSearchObj;
import com.yiyayaya.shopmanage.entity.UserActivity;
import com.yiyayaya.shopmanage.entity.Users;
import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.vo.UserVO;
import com.yiyayaya.shopmanage.exception.AuthException;
import com.yiyayaya.shopmanage.exception.ServiceException;
import com.yiyayaya.shopmanage.service.IUserActivityService;
import com.yiyayaya.shopmanage.service.IUsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IUserActivityService userActivityService;

    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody LoginObj loginObj) {
        UserVO userVO = usersService.login(loginObj.getUsername(), loginObj.getPassword());
        if (userVO != null) {
            // 记录用户登录行为
            UserActivity activity = new UserActivity();
            activity.setUserId(userVO.getUserId());
            activity.setUsername(userVO.getUsername());
            activity.setAction("登录");
            activity.setDetails("用户 " + userVO.getUsername() + " 登录了系统");
            activity.setTimestamp(LocalDateTime.now());
            userActivityService.recordActivity(activity);

            return Result.success(userVO);
        } else {
            return Result.error(500, "用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody Users user) {
        log.info("用户注册/更新请求: {}", user.getUsername());
        boolean result = usersService.saveOrUpdateUser(user,0);
        if (result) {
            // 记录用户注册行为
            UserActivity activity = new UserActivity();
            activity.setUserId(user.getUserId());
            activity.setUsername(user.getUsername());
            activity.setAction("注册");
            activity.setDetails("用户 " + user.getUsername() + " 注册了系统");
            activity.setTimestamp(LocalDateTime.now());
            userActivityService.recordActivity(activity);
        }
        return Result.success(result);
    }

    @PostMapping("/getAll")
    public Result<Pages<UserVO>> getAllUsers(@RequestBody UserSearchObj userSearchObj) {
        Pages<UserVO> users = usersService.getAllUsers(userSearchObj);
        return Result.success(users);
    }

    @DeleteMapping("/delete/{userId}/**")
    @Transactional
    public Result<Boolean> deleteUser(@PathVariable Integer userId, @RequestHeader("user") Integer currentUserId, HttpServletRequest request) {
        String[] userIdList = request.getRequestURI().replace("/users/delete/", "").split("/");
        log.info(Arrays.toString(userIdList));
        boolean result = false;
        for(String userIdStr : userIdList) {
            try{
               result = usersService.deleteUser(Integer.parseInt(userIdStr), currentUserId);
               if (result) {
                   // 记录用户删除行为
                   UserActivity activity = new UserActivity();
                   activity.setUserId(currentUserId);
                   activity.setUsername(usersService.getUsernameById(currentUserId));
                   activity.setAction("删除用户");
                   activity.setDetails("用户 " + usersService.getUsernameById(currentUserId) + " 删除了用户ID为 " + userIdStr + " 的用户");
                   activity.setTimestamp(LocalDateTime.now());
                   userActivityService.recordActivity(activity);
               }
            }catch (Exception e){
                log.error(e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        }
        return Result.success(result);
    }

    @PostMapping("/update")
    public Result<Boolean> updateUser(@RequestBody Users user,@RequestHeader("user") Integer currentUserId) {
        if (user.getUserId() == 1 && currentUserId != 1){
            throw new AuthException("没有权限操作："+user.getUsername());
        }
        boolean result = usersService.saveOrUpdateUser(user,currentUserId);
        if (result) {
            // 记录用户更新行为
            UserActivity activity = new UserActivity();
            activity.setUserId(currentUserId);
            activity.setUsername(usersService.getUsernameById(currentUserId));
            activity.setAction("更新用户");
            activity.setDetails("用户 " + usersService.getUsernameById(currentUserId) + " 更新了用户ID为 " + user.getUserId() + " 的用户信息");
            activity.setTimestamp(LocalDateTime.now());
            userActivityService.recordActivity(activity);
        }
        return Result.success(result);
    }

    @PostMapping("/search")
    public Result<Pages<UserVO>> searchUsers(@RequestBody UserSearchObj userSearchObj) {
        Pages<UserVO> users = usersService.getAllUsers(userSearchObj);
        return Result.success(users);
    }

    @GetMapping("/disabled")
    public Result<Boolean> disableUser(@RequestParam Integer userId,@RequestParam Integer status,@RequestHeader("user") Integer currentUserId) {
        boolean res = usersService.disableUserById(userId,currentUserId,status);
        if (res) {
            // 记录用户禁用行为
            UserActivity activity = new UserActivity();
            activity.setUserId(currentUserId);
            activity.setUsername(usersService.getUsernameById(currentUserId));
            activity.setAction( status == 1 ? "启用" : "禁用" +"用户");
            activity.setDetails("用户 " + usersService.getUsernameById(currentUserId) + (status == 1 ? "启用" : "禁用") +"了用户ID为 " + userId + " 的用户");
            activity.setTimestamp(LocalDateTime.now());
            userActivityService.recordActivity(activity);
        }
        return Result.success(res);
    }

    @GetMapping("/getUserById/{userId}")
    public Result<UserVO> getUserById(@PathVariable Integer userId) {
        UserVO user = usersService.getUserById(userId);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error(404, "用户不存在");
        }
    }

    @PostMapping("/updatePassword")
    public Result<Boolean> updatePassword(@RequestBody PasswordUpdateDTO passwordUpdateDTO, @RequestHeader("user") Integer currentUserId) {
        boolean result = usersService.updatePassword(passwordUpdateDTO, currentUserId);
        if (result) {
            // 记录用户更新密码行为
            UserActivity activity = new UserActivity();
            activity.setUserId(currentUserId);
            activity.setUsername(usersService.getUsernameById(currentUserId));
            activity.setAction("更新密码");
            activity.setDetails("用户 " + usersService.getUsernameById(currentUserId) + " 更新了用户ID为 " + passwordUpdateDTO.getUserId() + " 的密码");
            activity.setTimestamp(LocalDateTime.now());
            userActivityService.recordActivity(activity);
        }
        return Result.success(result);
    }

    @GetMapping("/activities")
    public Result<Pages<UserActivity>> getUserActivities(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String username) {
        Pages<UserActivity> activities = userActivityService.getUserActivities(pageNum, pageSize,username);
        return Result.success(activities);
    }
}
