package com.yiyayaya.shopmanage.entity.Dto;

import com.yiyayaya.shopmanage.entity.Orders;
import com.yiyayaya.shopmanage.entity.OrderItemAttributes;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailDTO {
    private Orders order;
    private List<OrderItemDTO> orderItems;
    private List<OrderItemAttributes> orderItemAttributes;
}
