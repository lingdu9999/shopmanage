package com.yiyayaya.shopmanage.service;

import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.Menu;

import java.util.List;

/**
 * <p>
 * 菜单服务接口
 * </p>
 * 
 * @author yourname
 * @since 2024-11-21
 */
public interface IMenuService {
    /**
     * 获取所有菜单
     * 
     * @return 菜单列表
     */
    List<Menu> getAllMenus();

    /**
     * 分页查询菜单
     * @param current 当前页
     * @param pageSize 每页大小
     * @param label 菜单名称(可选，用于模糊查询)
     * @return 分页结果
     */
    Pages<Menu> getMenuPage(Integer current, Integer pageSize, String label);

    /**
     * 获取所有父级菜单
     * @return 父级菜单列表
     */
    List<Menu> getParentMenus();

    /**
     * 保存或更新菜单
     * @param menu 菜单信息
     */
    boolean saveOrUpdate(Menu menu);

    void deleteById(Menu menu);

    /**
     * 更新菜单状态
     * @param menu 包含id和状态的菜单对象
     * @return 更新是否成功
     */
    boolean updateStatus(Menu menu);
} 