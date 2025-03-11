package com.yiyayaya.shopmanage.entity.Dto;

import com.yiyayaya.shopmanage.entity.OrderItemAttributes;
import com.yiyayaya.shopmanage.entity.OrderItems;
import com.yiyayaya.shopmanage.entity.Products;
import lombok.Data;

import java.util.List;

@Data
public class OrderItemDTO {
    private OrderItems orderItem;
    private Products product;
    private List<OrderItemAttributes> orderItemAttributes;
}
