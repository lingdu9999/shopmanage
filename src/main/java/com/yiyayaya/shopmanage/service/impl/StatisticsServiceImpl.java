package com.yiyayaya.shopmanage.service.impl;

import com.yiyayaya.shopmanage.mapper.OrdersMapper;
import com.yiyayaya.shopmanage.mapper.UsersMapper;
import com.yiyayaya.shopmanage.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public int getOrderCount() {
        return ordersMapper.countOrders();
    }

    @Override
    public double getTotalPayment() {
        return ordersMapper.sumTotalPayment();
    }

    @Override
    public int getUserCount() {
        return usersMapper.countUsers();
    }

    @Override
    public Map<String, Object> getStatisticsForLast7Days() {
        Map<String, Object> data = new HashMap<>();
        data.put("orderCount", ordersMapper.countOrdersLast7Days());
        data.put("totalPayment", ordersMapper.sumTotalPaymentLast7Days());
        data.put("userCount", usersMapper.countUsersLast7Days());
        data.put("status0Orders", ordersMapper.countOrdersByStatus0Last7Days());
        data.put("status1Orders", ordersMapper.countOrdersByStatus1Last7Days());
        data.put("status2Orders", ordersMapper.countOrdersByStatus2Last7Days());
        data.put("status3Orders", ordersMapper.countOrdersByStatus3Last7Days());
        data.put("status4Orders", ordersMapper.countOrdersByStatus4Last7Days());
        return data;
    }

    @Override
    public Map<String, Object> getStatisticsForLast30Days() {
        Map<String, Object> data = new HashMap<>();
        data.put("orderCount", ordersMapper.countOrdersLast30Days());
        data.put("totalPayment", ordersMapper.sumTotalPaymentLast30Days());
        data.put("userCount", usersMapper.countUsersLast30Days());
        data.put("status0Orders", ordersMapper.countOrdersByStatus0Last30Days());
        data.put("status1Orders", ordersMapper.countOrdersByStatus1Last30Days());
        data.put("status2Orders", ordersMapper.countOrdersByStatus2Last30Days());
        data.put("status3Orders", ordersMapper.countOrdersByStatus3Last30Days());
        data.put("status4Orders", ordersMapper.countOrdersByStatus4Last30Days());
        return data;
    }

    @Override
    public Map<String, Object> getStatisticsForAllTime() {
        Map<String, Object> data = new HashMap<>();
        data.put("orderCount", getOrderCount());
        data.put("totalPayment", getTotalPayment());
        data.put("userCount", getUserCount());
        data.put("status0Orders", ordersMapper.countOrdersByStatus0());
        data.put("status1Orders", ordersMapper.countOrdersByStatus1());
        data.put("status2Orders", ordersMapper.countOrdersByStatus2());
        data.put("status3Orders", ordersMapper.countOrdersByStatus3());
        data.put("status4Orders", ordersMapper.countOrdersByStatus4());
        return data;
    }
}
