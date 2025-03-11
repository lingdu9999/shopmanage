package com.yiyayaya.shopmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *  订单条目属性 实体类
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderItemAttributes implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_item_attribute_id", type = IdType.AUTO)
    private Integer orderItemAttributeId;

    private Integer orderItemId;

    private Integer productAttributeId;

    private String attributeValue;
}

