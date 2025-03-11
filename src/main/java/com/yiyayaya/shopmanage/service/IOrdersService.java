package com.yiyayaya.shopmanage.service;

import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.Orders;
import com.yiyayaya.shopmanage.entity.Dto.OrderDTO;
import com.yiyayaya.shopmanage.entity.Dto.OrderDetailDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
public interface IOrdersService extends IService<Orders> {
    Pages<OrderDTO> getOrdersWithItems(Integer pageNum, Integer pageSize, String orderNumber, Integer orderStatus, String startDate, String endDate);
    OrderDetailDTO getOrderDetail(Integer orderId);
    boolean updateOrderStatus(Integer orderId, String trackingNumber, Integer status);
}
