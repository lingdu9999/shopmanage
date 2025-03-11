package com.yiyayaya.shopmanage.entity.vo;

import com.yiyayaya.shopmanage.entity.Subcategories;
import lombok.Data;

import java.util.List;

@Data
public class CategoryVo {
    private Integer categoryId; // 主分类 ID
    private String name; // 主分类名称
    private String description;
    private List<Subcategories> children; // 子分类列表
} 