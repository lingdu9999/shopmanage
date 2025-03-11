package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.entity.Dto.OrderDTO;
import com.yiyayaya.shopmanage.entity.Dto.OrderDetailDTO;
import com.yiyayaya.shopmanage.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;

    @GetMapping("/getAll")
    public Result<Pages<OrderDTO>> getOrdersWithItems(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Pages<OrderDTO> page = ordersService.getOrdersWithItems(pageNum, pageSize, orderNumber, orderStatus, startDate, endDate);
        return Result.success(page);
    }

    @PostMapping("/detail/{orderId}")
    public Result<OrderDetailDTO> getOrderDetail(@PathVariable Integer orderId) {
        OrderDetailDTO orderDetail = ordersService.getOrderDetail(orderId);
        if (orderDetail == null) {
            return Result.error(500,"订单不存在");
        }
        return Result.success(orderDetail);
    }

    @PutMapping("/status/{orderId}")
    public Result<Boolean> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam String trackingNumber,
            @RequestParam Integer status) {
        boolean updated = ordersService.updateOrderStatus(orderId, trackingNumber, status);
        return Result.success(updated);
    }
}
