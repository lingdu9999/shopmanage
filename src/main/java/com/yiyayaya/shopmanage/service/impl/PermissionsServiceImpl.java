package com.yiyayaya.shopmanage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiyayaya.shopmanage.entity.Permissions;
import com.yiyayaya.shopmanage.mapper.PermissionsMapper;
import com.yiyayaya.shopmanage.service.IPermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionsServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements IPermissionsService {

    @Autowired
    private PermissionsMapper permissionsMapper;

    @Override
    public List<Permissions> getPermissionsByUserId(Integer userId) {
        return permissionsMapper.getPermissionsByUserId(userId);
    }

    @Override
    public boolean isPermissionBoundToRole(Integer permissionId) {
        return permissionsMapper.countRolesByPermissionId(permissionId) > 0;
    }
}