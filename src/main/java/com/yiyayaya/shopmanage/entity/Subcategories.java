package com.yiyayaya.shopmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Subcategories {
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId; // 子分类 ID
    private String name; // 子分类名称
    private Integer parentId; // 关联的主分类 ID
} 