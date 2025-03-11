package com.yiyayaya.shopmanage.entity.Dto;

import lombok.Data;

@Data
public class UserSearchObj {
  String username;
  Integer role;
  Integer pageNum = 1;
  Integer pageSize = 10;
}
