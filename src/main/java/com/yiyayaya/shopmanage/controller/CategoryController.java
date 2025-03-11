package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.entity.Category;
import com.yiyayaya.shopmanage.entity.Dto.CategoryDto;
import com.yiyayaya.shopmanage.entity.vo.CategoryVo;
import com.yiyayaya.shopmanage.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.yiyayaya.shopmanage.entity.Subcategories;

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
    public Result<String> addCategory(@RequestBody CategoryDto categoryDto) {
        boolean success = categoryService.addOrUpdateCategory(categoryDto);
        return success ? Result.success("添加成功") : Result.error(500, "添加失败");
    }

    @PostMapping("/update")
    public Result<String> updateCategory(@RequestBody CategoryDto categoryDto) {
        boolean success = categoryService.addOrUpdateCategory(categoryDto);
        return success ? Result.success("更新成功") : Result.error(500, "更新失败");
    }

    @PostMapping("/delete")
    public Result<String> deleteCategory(@RequestBody Integer[] categoryId) {
        boolean success = categoryService.deleteCategory(categoryId);
        return success ? Result.success("删除成功") : Result.error(500, "删除失败");
    }
}
