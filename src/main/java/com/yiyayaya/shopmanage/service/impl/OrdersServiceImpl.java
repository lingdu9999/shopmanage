package com.yiyayaya.shopmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.OrderItems;
import com.yiyayaya.shopmanage.entity.Orders;
import com.yiyayaya.shopmanage.entity.Dto.OrderDTO;
import com.yiyayaya.shopmanage.entity.OrderItemAttributes;
import com.yiyayaya.shopmanage.entity.Products;
import com.yiyayaya.shopmanage.entity.Dto.OrderDetailDTO;
import com.yiyayaya.shopmanage.entity.Dto.OrderItemDTO;
import com.yiyayaya.shopmanage.mapper.OrderItemsMapper;
import com.yiyayaya.shopmanage.mapper.OrdersMapper;
import com.yiyayaya.shopmanage.mapper.OrderItemAttributesMapper;
import com.yiyayaya.shopmanage.mapper.ProductsMapper;
import com.yiyayaya.shopmanage.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import java.util.ArrayList;
import java.util.List;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderItemAttributesMapper orderItemAttributesMapper;

    @Autowired
    private ProductsMapper productsMapper;

    @Override
    public Pages<OrderDTO> getOrdersWithItems(Integer pageNum, Integer pageSize, String orderNumber, Integer orderStatus, String startDate, String endDate) {
        Page<Orders> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Orders> ordersWrapper = new QueryWrapper<>();

        if (orderNumber != null) {
            ordersWrapper.like("order_number", orderNumber);
        }
        if (orderStatus != null) {
            ordersWrapper.eq("status", orderStatus);
        }
        if (startDate != null && endDate != null) {
            ordersWrapper.between("order_date", startDate, endDate);
        }

        Page<Orders> orderPage = ordersMapper.selectPage(page, ordersWrapper);

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (Orders order : orderPage.getRecords()) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrder(order);

            QueryWrapper<OrderItems> orderItemWrapper = new QueryWrapper<>();
            orderItemWrapper.eq("order_id", order.getOrderId());
            List<OrderItems> orderItems = orderItemsMapper.selectList(orderItemWrapper);
            orderDTO.setOrderItems(orderItems);

            orderDTOList.add(orderDTO);
        }

        return new Pages<>(
                orderDTOList,
                orderPage.getTotal(),
                orderPage.getCurrent(),
                orderPage.getSize()
        );
    }

    @Override
    public OrderDetailDTO getOrderDetail(Integer orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return null;
        }

        QueryWrapper<OrderItems> orderItemWrapper = new QueryWrapper<>();
        orderItemWrapper.eq("order_id", orderId);
        List<OrderItems> orderItems = orderItemsMapper.selectList(orderItemWrapper);

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setOrder(order);

        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        for (OrderItems orderItem : orderItems) {
            Products product = productsMapper.selectById(orderItem.getProductId());

            QueryWrapper<OrderItemAttributes> orderItemAttributesWrapper = new QueryWrapper<>();
            orderItemAttributesWrapper.eq("order_item_id", orderItem.getOrderItemId());
            List<OrderItemAttributes> orderItemAttributes = orderItemAttributesMapper.selectList(orderItemAttributesWrapper);

            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setOrderItem(orderItem);
            orderItemDTO.setProduct(product);
            orderItemDTO.setOrderItemAttributes(orderItemAttributes);

            orderItemDTOList.add(orderItemDTO);
        }
        orderDetailDTO.setOrderItems(orderItemDTOList);

        return orderDetailDTO;
    }

    @Override
    public boolean updateOrderStatus(Integer orderId, String trackingNumber, Integer status) {
        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setTrackingNumber(trackingNumber);
        order.setStatus(status);

        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id", orderId);

        return ordersMapper.update(order, updateWrapper) > 0;
    }
}
