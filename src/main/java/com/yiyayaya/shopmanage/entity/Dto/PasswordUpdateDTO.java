package com.yiyayaya.shopmanage.entity.Dto;

import lombok.Data;

@Data
public class PasswordUpdateDTO {
    private Integer userId;
    private String oldPassword;
    private String newPassword;
}
