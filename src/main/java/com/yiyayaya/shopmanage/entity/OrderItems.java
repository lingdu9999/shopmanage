package com.yiyayaya.shopmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *  订单详情实体类
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_item_id", type = IdType.AUTO)
    private Integer orderItemId;

    private Integer orderId;

    private Integer productId;

    private Integer quantity;

    private Integer price;
}
