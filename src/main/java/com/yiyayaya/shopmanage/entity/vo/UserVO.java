package com.yiyayaya.shopmanage.entity.vo;

import com.yiyayaya.shopmanage.entity.Users;
import lombok.Data;

@Data
public class UserVO {
    private Integer userId;
    private String username;
    private String image;
    private String email;
    private Integer role;
    private String phone;
    private String token;
    private String message;
    private Integer issuse;

    // 可以添加一个转换方法
    public static UserVO fromUser(Users user) {
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setUserId(user.getUserId());
        vo.setUsername(user.getUsername());
        vo.setImage(user.getImage());
        vo.setEmail(user.getEmail());
        vo.setRole(user.getRole());
        vo.setPhone(user.getPhone());
        vo.setMessage(user.getMessage());
        vo.setIssuse(user.getIssuse());
        return vo;
    }
} 