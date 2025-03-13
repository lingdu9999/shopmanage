package com.yiyayaya.shopmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.Roles;
import com.yiyayaya.shopmanage.entity.Permissions;
import com.yiyayaya.shopmanage.exception.ServiceException;
import com.yiyayaya.shopmanage.mapper.RolesMapper;
import com.yiyayaya.shopmanage.mapper.PermissionsMapper;
import com.yiyayaya.shopmanage.service.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles> implements IRolesService {

    @Autowired
    private RolesMapper rolesMapper;

    @Autowired
    private PermissionsMapper permissionsMapper;

    @Override
    public Pages<Roles> getAllRolesWithPermissions(Integer pageNum, Integer pageSize) {
        Page<Roles> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Roles> queryWrapper = new QueryWrapper<>();
        // Add any necessary query conditions here
        rolesMapper.selectPage(page, queryWrapper);
        
        List<Roles> rolesList = page.getRecords();
        for (Roles role : rolesList) {
            List<Permissions> permissions = permissionsMapper.getPermissionsByRoleId(role.getId());
            role.setPermissions(permissions);
        }
        
        return new Pages<>(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());
    }

    @Override
    public boolean updateRolePermissions(Integer roleId, List<Integer> permissionIds) {
        // Delete existing permissions for the role
        rolesMapper.deleteRolePermissions(roleId);

        // Add new permissions for the role
        for (Integer permissionId : permissionIds) {
            rolesMapper.insertRolePermission(roleId, permissionId);
        }
        return true;
    }

    @Override
    public boolean deleteRole(Integer roleId) {
        QueryWrapper<Roles> queryWrapper = new QueryWrapper<>();
        Roles roles = rolesMapper.selectById(roleId);
        if (roles == null) {
            throw new ServiceException("角色不存在");
        }
        rolesMapper.deleteRolePermissions(roleId);
        queryWrapper.eq("role_id", roleId);
        return rolesMapper.delete(queryWrapper) > 0;
    }
    @Override
    public boolean addRole(Roles roles,List<Integer> permissions) {
      try {
        rolesMapper.insert(roles);
        for (Integer permissionId : permissions) {
          rolesMapper.insertRolePermission(roles.getId(), permissionId);
        }
        return true;
      }catch (Exception e){
        throw new ServiceException(e.getMessage());
      }
    }

}
