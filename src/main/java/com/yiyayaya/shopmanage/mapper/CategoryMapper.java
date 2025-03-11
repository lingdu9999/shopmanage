package com.yiyayaya.shopmanage.mapper;

import com.yiyayaya.shopmanage.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiyayaya.shopmanage.entity.Subcategories;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
  @Update("update category set name = '${name}',description = '${description}' where category_id = ${categoryId}")
  boolean updateCategoryById(Category category);
//  @Select("select  category_id,name from category")
  List<Category> getAllCategory();
  List<Map<String, Object>> selectCategoryWithSubCategory(String name);
  List<Subcategories> selectSubCategoryByCategoryId(Integer parentId);

  boolean insertOrUpdateById(Subcategories subcategories);

}
