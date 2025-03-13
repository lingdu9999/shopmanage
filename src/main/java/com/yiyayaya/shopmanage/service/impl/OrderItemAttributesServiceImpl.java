package com.yiyayaya.shopmanage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiyayaya.shopmanage.entity.OrderItemAttributes;
import com.yiyayaya.shopmanage.mapper.OrderItemAttributesMapper;
import com.yiyayaya.shopmanage.service.IOrderItemAttributesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Service
public class OrderItemAttributesServiceImpl extends ServiceImpl<OrderItemAttributesMapper, OrderItemAttributes> implements IOrderItemAttributesService {

    @Autowired
    private OrderItemAttributesMapper orderItemAttributesMapper;

    @Override
    public int countOrdersByProductId(Integer productId) {
        return orderItemAttributesMapper.countOrdersByProductId(productId);
    }
}
