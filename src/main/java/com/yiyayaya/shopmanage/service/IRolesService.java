package com.yiyayaya.shopmanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.Roles;

import java.util.List;

public interface IRolesService extends IService<Roles> {
    Pages<Roles> getAllRolesWithPermissions(Integer pageNum, Integer pageSize);
    // Additional custom methods can be defined here if needed

    boolean updateRolePermissions(Integer roleId, List<Integer> permissionIds);

    boolean deleteRole(Integer roleId);
    boolean addRole(Roles roles,List<Integer> permissions);
}