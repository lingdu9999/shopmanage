package com.yiyayaya.shopmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.Category;
import com.yiyayaya.shopmanage.entity.Dto.CategoryDto;
import com.yiyayaya.shopmanage.entity.vo.CategoryVo;
import com.yiyayaya.shopmanage.exception.ServiceException;
import com.yiyayaya.shopmanage.mapper.CategoryMapper;
import com.yiyayaya.shopmanage.mapper.SubcategoriesMapper;
import com.yiyayaya.shopmanage.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import com.yiyayaya.shopmanage.entity.Subcategories;

import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SubcategoriesMapper subcategoriesMapper;

    @Override
    public List<CategoryVo> getAllCategory() {
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        List<Category> categoryList = categoryMapper.selectList(categoryQueryWrapper);
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category c : categoryList){
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setCategoryId(c.getCategoryId());
            categoryVo.setName(c.getName());
            categoryVo.setDescription(c.getDescription());

            // 获取子分类并设置到 CategoryVo 中
            List<Subcategories> subcategories = categoryMapper.selectSubCategoryByCategoryId(c.getCategoryId());
            categoryVo.setChildren(subcategories);

            categoryVoList.add(categoryVo);
        }
        return categoryVoList;
    }

    @Override
    public Pages<CategoryVo> getCategoryPage(int pageNum, int pageSize, String name) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        Page<Category> page = new Page<>(pageNum, pageSize);
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        
        // 获取主分类分页数据
        Page<Category> categoriesPage = categoryMapper.selectPage(page, queryWrapper);
        
        // 创建 CategoryVo 列表
        List<CategoryVo> categoryVoList = new ArrayList<>();
        
        for (Category category : categoriesPage.getRecords()) {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setCategoryId(category.getCategoryId());
            categoryVo.setName(category.getName());
            categoryVo.setDescription(category.getDescription());
            
            // 获取子分类并设置到 CategoryVo 中
            List<Subcategories> subcategories = categoryMapper.selectSubCategoryByCategoryId(category.getCategoryId());
            categoryVo.setChildren(subcategories);
            
            categoryVoList.add(categoryVo);
        }
        
        // 创建新的分页对象
        
        return new Pages<>(categoryVoList,categoriesPage.getTotal(),categoriesPage.getCurrent(),categoriesPage.getSize()); // 返回包含子分类的分页对象
    }

    public List<Subcategories> getAllSubCategory(){
        return subcategoriesMapper.selectList(new QueryWrapper<Subcategories>());
    }

    public Category getParentCategoryBySubcategoryId(Integer subcategoryId) {
        // 1. 根据子分类ID查询子分类记录
        Subcategories subcategory = subcategoriesMapper.selectById(subcategoryId);
        if (subcategory != null && subcategory.getCategoryId() != null) {
            // 2. 根据子分类的 parentId 查询父分类记录
            return categoryMapper.selectById(subcategory.getParentId());
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getCategoryWithSubCategory(String name) {
        return categoryMapper.selectCategoryWithSubCategory(name); // 调用 Mapper 方法
    }

    @Override
    public List<Subcategories> getSubCategoryByCategoryId(Integer parentId) {
        return categoryMapper.selectSubCategoryByCategoryId(parentId); // 调用 Mapper 方法
    }

    @Transactional
    public boolean addOrUpdateCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setCategoryId(categoryDto.getCategoryId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        boolean categorySaved = false;
        System.out.println(category.getCategoryId());

        if (category.getCategoryId() != null ){
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", category.getName()).ne("category_id", category.getCategoryId());
            if (categoryMapper.selectCount(queryWrapper) > 0) {
                throw new ServiceException("主分类名已存在");
            }
            categorySaved = categoryMapper.updateCategoryById(category);
        }else {
            categorySaved = categoryMapper.insert(category) > 0;
        }

        if (categorySaved && categoryDto.getChildren() != null) {
            QueryWrapper<Subcategories> SubcategoriesWrapper = new QueryWrapper<>();
            SubcategoriesWrapper.eq("parent_id", category.getCategoryId());

            try{
                subcategoriesMapper.delete(SubcategoriesWrapper);
            }catch (Exception e){
                throw new ServiceException(e.getMessage().toString().indexOf("fk_product_catetoryid") != -1 ? "该分类已经有商品使用" : e.getMessage());
            }

            for (Subcategories subcategories : categoryDto.getChildren()) {
                subcategories.setParentId(category.getCategoryId());
            }
            subcategoriesMapper.insert(categoryDto.getChildren());
        }
        return categorySaved;
    }

    @Transactional
    public boolean deleteCategory(Integer[] categoryId) {
        try{
            for (Integer categoryId1 : categoryId) {
                QueryWrapper<Subcategories> SubcategoriesWrapper = new QueryWrapper<>();
                SubcategoriesWrapper.eq("category_id", categoryId1);
                subcategoriesMapper.delete(SubcategoriesWrapper);
            }
            boolean result = categoryMapper.deleteByIds(Arrays.stream(categoryId).toList()) == categoryId.length;
            if (!result) {
                throw new ServiceException("删除失败");
            }
            return true;
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}
