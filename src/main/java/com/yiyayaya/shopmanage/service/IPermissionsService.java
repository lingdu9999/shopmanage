package com.yiyayaya.shopmanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yiyayaya.shopmanage.entity.Permissions;
import java.util.List;

public interface IPermissionsService extends IService<Permissions> {
  // Additional custom methods can be defined here if needed

  List<Permissions> getPermissionsByUserId(Integer userId);

  boolean isPermissionBoundToRole(Integer permissionId);
}
