package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.entity.Category;
import com.yiyayaya.shopmanage.entity.Subcategories;
import com.yiyayaya.shopmanage.entity.Dto.CategoryDto;
import com.yiyayaya.shopmanage.entity.UserActivity;
import com.yiyayaya.shopmanage.entity.vo.CategoryVo;
import com.yiyayaya.shopmanage.service.ICategoryService;
import com.yiyayaya.shopmanage.service.IUserActivityService;
import com.yiyayaya.shopmanage.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUserActivityService userActivityService;

    @Autowired
    private IUsersService usersService;

    @GetMapping("/list")
    public Result<List<CategoryVo>> list() {
        List<CategoryVo> categories = categoryService.getAllCategory();
        return Result.success(categories);
    }

    @GetMapping("/sublist")
    public Result<List<Subcategories>> sublist() {
        List<Subcategories> Subcategories = categoryService.getAllSubCategory();
        return Result.success(Subcategories);
    }

    @GetMapping("/getAll")
    public Result<Pages<CategoryVo>> getCategoriesPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "20") int pageSize, @RequestParam(defaultValue = "") String name) {
        Pages<CategoryVo> page = categoryService.getCategoryPage(pageNum, pageSize, name);
        return Result.success(page, "获取成功");
    }

    @GetMapping("/withSubcategories")
    public Result<List<Map<String, Object>>> getCategoriesWithSubcategories(@RequestParam(defaultValue = "") String name) {
        List<Map<String, Object>> categoriesWithSubcategories = categoryService.getCategoryWithSubCategory(name);
        return Result.success(categoriesWithSubcategories, "获取成功");
    }

    @GetMapping("/getParentId")
    public Result<Category> getParentCategoryBySubcategoryId(@RequestParam Integer subcategoryId ) {
        Category category = categoryService.getParentCategoryBySubcategoryId(subcategoryId);
        return Result.success(category, "获取成功");
    }

    @GetMapping("/subcategories")
    public Result<List<Subcategories>> getSubcategoriesByCategoryId(@RequestParam int categoryId) {
        List<Subcategories> subcategories = categoryService.getSubCategoryByCategoryId(categoryId);
        return Result.success(subcategories, "获取成功");
    }

    @PostMapping("/add")
    public Result<String> addCategory(@RequestBody CategoryDto categoryDto, @RequestHeader("user") Integer userId) {
        boolean success = categoryService.addOrUpdateCategory(categoryDto);
        if (success) {
            // 记录用户添加分类行为
            UserActivity activity = new UserActivity();
            activity.setUserId(userId);
            activity.setUsername(usersService.getUsernameById(userId));
            activity.setAction("添加分类");
            activity.setDetails("用户 " + usersService.getUsernameById(userId) + " 添加了分类: " + categoryDto.getName());
            activity.setTimestamp(LocalDateTime.now());
            userActivityService.recordActivity(activity);
        }
        return success ? Result.success("添加成功") : Result.error(500, "添加失败");
    }

    @PostMapping("/update")
    public Result<String> updateCategory(@RequestBody CategoryDto categoryDto, @RequestHeader("user") Integer userId) {
        boolean success = categoryService.addOrUpdateCategory(categoryDto);
        if (success) {
            // 记录用户更新分类行为
            UserActivity activity = new UserActivity();
            activity.setUserId(userId);
            activity.setUsername(usersService.getUsernameById(userId));
            activity.setAction("更新分类");
            activity.setDetails("用户 " + usersService.getUsernameById(userId) + " 更新了分类 分类名为 " + categoryDto.getName() + " 的分类信息");
            activity.setTimestamp(LocalDateTime.now());
            userActivityService.recordActivity(activity);
        }
        return success ? Result.success("更新成功") : Result.error(500, "更新失败");
    }

    @PostMapping("/delete")
    public Result<String> deleteCategory(@RequestBody Integer[] categoryId, @RequestHeader("user") Integer userId) {
        boolean success = categoryService.deleteCategory(categoryId);
        if (success) {
            // 记录用户删除分类行为
            for (Integer id : categoryId) {
                UserActivity activity = new UserActivity();
                activity.setUserId(userId);
                activity.setUsername(usersService.getUsernameById(userId));
                activity.setAction("删除分类");
                activity.setDetails("用户 " + usersService.getUsernameById(userId) + " 删除了分类名为 " + id + " 的分类");
                activity.setTimestamp(LocalDateTime.now());
                userActivityService.recordActivity(activity);
            }
        }
        return success ? Result.success("删除成功") : Result.error(500, "删除失败");
    }
}
