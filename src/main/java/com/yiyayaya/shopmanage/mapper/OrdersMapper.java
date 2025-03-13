package com.yiyayaya.shopmanage.mapper;

import com.yiyayaya.shopmanage.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    @Select("SELECT COUNT(*) FROM orders")
    int countOrders();

    @Select("SELECT IFNULL(SUM(total_amount), 0) FROM orders")
    double sumTotalPayment();

    @Select("SELECT COUNT(*) FROM orders WHERE order_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")
    int countOrdersLast7Days();

    @Select("SELECT IFNULL(SUM(total_amount), 0) FROM orders WHERE order_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")
    double sumTotalPaymentLast7Days();

    @Select("SELECT COUNT(*) FROM orders WHERE order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)")
    int countOrdersLast30Days();

    @Select("SELECT IFNULL(SUM(total_amount), 0) FROM orders WHERE order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)")
    double sumTotalPaymentLast30Days();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 0")
    int countOrdersByStatus0();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 1")
    int countOrdersByStatus1();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 2")
    int countOrdersByStatus2();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 3")
    int countOrdersByStatus3();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 4")
    int countOrdersByStatus4();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 0 AND order_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")
    int countOrdersByStatus0Last7Days();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 1 AND order_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")
    int countOrdersByStatus1Last7Days();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 2 AND order_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")
    int countOrdersByStatus2Last7Days();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 3 AND order_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")
    int countOrdersByStatus3Last7Days();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 4 AND order_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")
    int countOrdersByStatus4Last7Days();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 0 AND order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)")
    int countOrdersByStatus0Last30Days();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 1 AND order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)")
    int countOrdersByStatus1Last30Days();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 2 AND order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)")
    int countOrdersByStatus2Last30Days();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 3 AND order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)")
    int countOrdersByStatus3Last30Days();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 4 AND order_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)")
    int countOrdersByStatus4Last30Days();
}
