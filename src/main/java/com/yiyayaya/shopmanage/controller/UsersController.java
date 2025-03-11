package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.entity.Dto.LoginObj;
import com.yiyayaya.shopmanage.entity.Dto.PasswordUpdateDTO;
import com.yiyayaya.shopmanage.entity.Dto.UserSearchObj;
import com.yiyayaya.shopmanage.entity.Users;
import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.vo.UserVO;
import com.yiyayaya.shopmanage.exception.AuthException;
import com.yiyayaya.shopmanage.exception.ServiceException;
import com.yiyayaya.shopmanage.service.IUsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody LoginObj loginObj) {
        UserVO userVO = usersService.login(loginObj.getUsername(), loginObj.getPassword());
        if (userVO != null) {
            return Result.success(userVO);
        } else {
            return Result.error(500, "用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody Users user) {
        log.info("用户注册/更新请求: {}", user.getUsername());
        boolean result = usersService.saveOrUpdateUser(user,0);
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
        return Result.success(result);
    }
}
