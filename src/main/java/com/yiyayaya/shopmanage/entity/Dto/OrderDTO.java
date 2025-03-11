package com.yiyayaya.shopmanage.entity.Dto;

import com.yiyayaya.shopmanage.entity.OrderItems;
import com.yiyayaya.shopmanage.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private Orders order;
    private List<OrderItems> orderItems;
}

