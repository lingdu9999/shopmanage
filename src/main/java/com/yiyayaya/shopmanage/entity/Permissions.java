package com.yiyayaya.shopmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permissions {
  @TableId(value = "permission_id", type = IdType.AUTO)
  private Integer id;
  private String permissionName;
  private String permissionCode;
  private String url;
  private String pageUrl;
  
}
