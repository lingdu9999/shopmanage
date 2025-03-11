package com.yiyayaya.shopmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductImages implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "image_id", type = IdType.AUTO)
    private Integer imageId;

    private Integer productId; // 关联的产品ID

    private String imageUrl; // 图片URL
} 