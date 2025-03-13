package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.entity.Dto.RolesDTO;
import com.yiyayaya.shopmanage.entity.Roles;
import com.yiyayaya.shopmanage.service.IRolesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private IRolesService rolesService;

    @GetMapping("/getAll")
    public Result<Pages<Roles>> getAllRolesWithPermissions(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Pages<Roles> rolesPage = rolesService.getAllRolesWithPermissions(pageNum, pageSize);
        return Result.success(rolesPage);
    }

    @PostMapping("/updatePermissions")
    public Result<Boolean> updateRolePermissions(@RequestParam Integer roleId, @RequestBody List<Integer> permissionIds) {
        boolean result = rolesService.updateRolePermissions(roleId, permissionIds);
        return Result.success(result);
    }

    @GetMapping("/delete")
    public Result<Boolean> deleteRole(@RequestParam Integer roleId) {
        boolean res = rolesService.deleteRole(roleId);
        return Result.success(res);
    }

    @PostMapping("/add")
    public Result<Boolean> addRole(@RequestBody RolesDTO roles) {
        boolean b = rolesService.addRole(roles.getRoles(), roles.getPermissions());
        return Result.success(b);
    }
}