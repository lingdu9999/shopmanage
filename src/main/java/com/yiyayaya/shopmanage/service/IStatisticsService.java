package com.yiyayaya.shopmanage.service;

import java.util.Map;

public interface IStatisticsService {
    int getOrderCount();
    double getTotalPayment();
    int getUserCount();
    Map<String, Object> getStatisticsForLast7Days();
    Map<String, Object> getStatisticsForLast30Days();
    Map<String, Object> getStatisticsForAllTime();
}
