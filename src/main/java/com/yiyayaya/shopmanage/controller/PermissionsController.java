package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.entity.Permissions;
import com.yiyayaya.shopmanage.service.IPermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionsController {

    @Autowired
    private IPermissionsService permissionsService;

    @GetMapping("/getAll")
    public Result<List<Permissions>> getAllPermissions() {
        List<Permissions> permissions = permissionsService.list();
        return Result.success(permissions);
    }

    @PostMapping("/add")
    public Result<Boolean> addPermission(@RequestBody Permissions permission) {
        boolean result = permissionsService.save(permission);
        return Result.success(result);
    }

    @PostMapping("/delete")
    public Result<Boolean> deletePermission(@RequestParam Integer permissionId) {
        boolean isBound = permissionsService.isPermissionBoundToRole(permissionId);
        if (isBound) {
            return Result.error(400, "权限已绑定角色，无法删除");
        }
        boolean result = permissionsService.removeById(permissionId);
        return Result.success(result);
    }

    @GetMapping("/getByUser")
    public Result<List<Permissions>> getPermissionsByUser(@RequestHeader("user") Integer userId) {
        List<Permissions> permissions = permissionsService.getPermissionsByUserId(userId);
        return Result.success(permissions);
    }
}