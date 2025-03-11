package com.yiyayaya.shopmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *  订单实体类
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    private Integer userId;

    private String orderNumber;

    private Integer totalAmount;

    private Integer status;

    private String shippingAddress;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    private String buyerName; // 买家姓名
    private String buyerPhoneNumber; // 买家电话

    private String trackingNumber; // 物流单号

    private String paymentMethod; // 支付方式
    private Integer paymentStatus; // 支付状态
}
