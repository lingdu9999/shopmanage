package com.yiyayaya.shopmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.Menu;
import com.yiyayaya.shopmanage.exception.ServiceException;
import com.yiyayaya.shopmanage.mapper.MenuMapper;
import com.yiyayaya.shopmanage.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单服务实现类
 * </p>
 * 
 * @author yourname
 * @since 2024-11-21
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAllMenus(Integer type) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (type == 1) {
            queryWrapper.ne("parent_id", 0);
        }
        return menuMapper.selectList(queryWrapper);
    }

    @Override
    public Pages<Menu> getMenuPage(Integer current, Integer pageSize, String label) {
        Page<Menu> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        
        // 如果label不为空，添加模糊查询条件
        if (label != null && !label.trim().isEmpty()) {
            wrapper.like(Menu::getLabel, label);
        }
        
        // 先按parentId排序，再按sortOrder排序
        wrapper.orderByAsc(Menu::getParentId)
               .orderByAsc(Menu::getSortOrder);
        
        Page<Menu> menuPage = menuMapper.selectPage(page, wrapper);
        
        return new Pages<>(
            menuPage.getRecords(),
            menuPage.getTotal(),
            menuPage.getCurrent(),
            menuPage.getSize()
        );
    }

    @Override
    public List<Menu> getParentMenus() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, 0)  // parentId为0的是父级菜单
               .orderByAsc(Menu::getSortOrder);  // 按照排序字段升序
        return menuMapper.selectList(wrapper);
    }

    @Override
    public boolean saveOrUpdate(Menu menu) {
        // 如果是子菜单，检查父菜单是否存在
        if (menu.getParentId() != null && menu.getParentId() != 0) {
            Menu parentMenu = menuMapper.selectById(menu.getParentId());
            if (parentMenu == null) {
                throw new ServiceException("父级菜单不存在");
            }
            // 如果是更新操作，检查是否将菜单设置为自己的子菜单
            if (menu.getId() != null && menu.getId().equals(menu.getParentId())) {
                throw new ServiceException("不能将父级菜单设置为自己");
            }
        }
        
        if (menu.getId() == null) {
           return menuMapper.insert(menu) > 0;
        } else {
           return menuMapper.updateById(menu) > 0;
        }
    }

    @Override
    public void deleteById(Menu menu) {
        // 先查询要删除的菜单是否存在
        Menu existMenu = menuMapper.selectById(menu.getId());
        if (existMenu == null) {
            throw new ServiceException("要删除的菜单不存在");
        }

        if (existMenu.getParentId() == 0) {
            // 如果是父级菜单，需要先删除其下的所有子菜单
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Menu::getParentId, existMenu.getId());
            List<Menu> children = menuMapper.selectList(wrapper);
            
            if (!children.isEmpty()) {
                // 删除所有子菜单
                wrapper.clear();
                wrapper.eq(Menu::getParentId, existMenu.getId());
                menuMapper.delete(wrapper);
            }
        }
        
        // 删除菜单本身
        menuMapper.deleteById(menu.getId());
    }

    @Override
    public boolean updateStatus(Menu menu) {
        // 先查询要修改的菜单是否存在
        Menu existMenu = menuMapper.selectById(menu.getId());
        if (existMenu == null) {
            throw new ServiceException("菜单不存在");
        }

        // 如果是父级菜单，同时更新所有子菜单的状态
        if (existMenu.getParentId() == 0) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Menu::getParentId, existMenu.getId());
            List<Menu> children = menuMapper.selectList(wrapper);
            
            if (!children.isEmpty()) {
                // 更新所有子菜单的状态
                Menu updateMenu = new Menu();
                updateMenu.setIsActive(menu.getIsActive());
                wrapper.clear();
                wrapper.eq(Menu::getParentId, existMenu.getId());
                menuMapper.update(updateMenu, wrapper);
            }
        }
        
        // 更新菜单本身的状态
        Menu updateMenu = new Menu();
        updateMenu.setId(menu.getId());
        updateMenu.setIsActive(menu.getIsActive());
        return menuMapper.updateById(updateMenu) > 0;
    }
} 