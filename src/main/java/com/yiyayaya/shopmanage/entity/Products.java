package com.yiyayaya.shopmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Getter
@Setter
@TableName("products")
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;

    private String sku;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private Integer subcategoryId;

    private String mainImage;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate productionDate;

    private Integer shelfDays;

    @TableField(exist = false)
    private List<ProductImages> imageUrls;

    @TableField(exist = false)
    private List<ProductAttributes> attributes;

    @TableField(exist = false)
    private String categoryName;

    private Integer status;
}
