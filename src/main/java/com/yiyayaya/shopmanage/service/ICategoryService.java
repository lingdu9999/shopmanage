package com.yiyayaya.shopmanage.service;

import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

import com.yiyayaya.shopmanage.entity.Subcategories;
import com.yiyayaya.shopmanage.entity.Dto.CategoryDto;
import com.yiyayaya.shopmanage.entity.vo.CategoryVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
public interface ICategoryService extends IService<Category> {
    List<CategoryVo> getAllCategory();
    List<Subcategories> getAllSubCategory();
    Pages<CategoryVo> getCategoryPage(int pageNum, int pageSize, String name);
    List<Map<String, Object>> getCategoryWithSubCategory(String name);
    List<Subcategories> getSubCategoryByCategoryId(Integer parentId);
    boolean addOrUpdateCategory(CategoryDto categoryDto) ;
    boolean deleteCategory(Integer[] categoryId);
    Category getParentCategoryBySubcategoryId(Integer subcategoryId);
}
