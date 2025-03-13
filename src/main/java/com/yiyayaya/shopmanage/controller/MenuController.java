package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.entity.Menu;
import com.yiyayaya.shopmanage.entity.Permissions;
import com.yiyayaya.shopmanage.service.IMenuService;
import com.yiyayaya.shopmanage.service.IPermissionsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单控制器
 * </p>
 * 
 * @author yourname
 * @since 2024-11-21
 */
@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IPermissionsService permissionsService;

    @GetMapping("/getAll")
    public Result<List<MenuResponse>> getAllMenus(@RequestHeader("user") Integer userId) {
        List<Menu> menus = menuService.getAllMenus(0);
        List<Permissions> permissions = permissionsService.getPermissionsByUserId(userId);
        List<MenuResponse> response = buildMenuResponse(menus, permissions);
        return Result.success(response);
    }

    @GetMapping("/getList")
    public Result<List<Menu>> getMenuList() {
        List<Menu> menus = menuService.getAllMenus(1);
        return Result.success(menus);
    }

    @GetMapping("/page")
    public Result<Pages<Menu>> getMenuPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String label) {
        Pages<Menu> page = menuService.getMenuPage(pageNum, pageSize, label);
        return Result.success(page);
    }

    @GetMapping("/parents")
    public Result<List<Menu>> getParentMenus() {
        List<Menu> parentMenus = menuService.getParentMenus();
        return Result.success(parentMenus);
    }

    @PostMapping("/add")
    public Result<String> addMenu(@RequestBody Menu menu) {
        boolean res = menuService.saveOrUpdate(menu);
        return res ? Result.success("添加成功") : Result.success("添加失败");
    }

    @PostMapping("/update")
    public Result<String> updateMenu(@RequestBody Menu menu) {
        if (menu.getId() == null) {
            return Result.error(400, "菜单ID不能为空");
        }
        boolean res = menuService.saveOrUpdate(menu);
        return res ? Result.success("更新成功") : Result.success("更新失败");
    }

    @PostMapping("/delete")
    public Result<String> deleteMenu(@RequestBody Menu menu) {
        if (menu.getId() == null) {
            return Result.error(400, "菜单ID不能为空");
        }
        menuService.deleteById(menu);
        return Result.success("删除成功");
    }

    @PostMapping("/status")
    public Result<String> updateStatus(@RequestBody Menu menu) {
        if (menu.getId() == null) {
            return Result.error(400, "菜单ID不能为空");
        }
        if (menu.getIsActive() == null) {
            return Result.error(400, "状态不能为空");
        }
        boolean res = menuService.updateStatus(menu);
        return res ? Result.success("状态修改成功") : Result.error(500, "状态修改失败");
    }

    private List<MenuResponse> buildMenuResponse(List<Menu> menus, List<Permissions> permissions) {
        List<MenuResponse> responseList = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getParentId() == 0 && menu.getIsActive() && hasPermission(menu, permissions)) { // 顶级菜单
                MenuResponse menuResponse = new MenuResponse();
                menuResponse.setLabel(menu.getLabel());
                menuResponse.setIcon(menu.getIcon());
                menuResponse.setPath(menu.getPath());
                menuResponse.setChildren(getChildren(menu, menus,permissions));
                responseList.add(menuResponse);
            }
        }
        return responseList;
    }

    private List<MenuResponse> getChildren(Menu parentMenu, List<Menu> allMenus, List<Permissions> permissions) {
        List<MenuResponse> children = new ArrayList<>();
        for (Menu menu : allMenus) {
            if (menu.getParentId() != null && menu.getParentId().equals(parentMenu.getId()) && menu.getIsActive()&& hasPermission(menu, permissions)) {
                MenuResponse childResponse = new MenuResponse();
                childResponse.setLabel(menu.getLabel());
                childResponse.setPath(menu.getPath());
                childResponse.setIcon(menu.getIcon());
                childResponse.setChildren(getChildren(menu, allMenus, permissions));
                children.add(childResponse);
            }
        }
        return children;
    }

    private boolean hasPermission(Menu menu, List<Permissions> permissions) {
        if (menu.getParentId() == 0) {
            return true;
        }
        for (Permissions permission : permissions) {
            if (StringUtils.equals(menu.getPath(), permission.getPageUrl())) {
                return true;
            }
        }
        return false;
    }

    // 内部类，用于构建返回的菜单格式
    public static class MenuResponse {
        private String label;
        private String icon;
        private String path;
        private List<MenuResponse> children;

        // Getters and Setters
        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public List<MenuResponse> getChildren() {
            return children;
        }

        public void setChildren(List<MenuResponse> children) {
            this.children = children;
        }
    }
}