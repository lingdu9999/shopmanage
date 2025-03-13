package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private IStatisticsService statisticsService;

    @GetMapping("/echarts")
    public Result<Map<String, Object>> getEChartsStatistics() {
        Map<String, Object> data = new HashMap<>();
        data.put("last7Days", statisticsService.getStatisticsForLast7Days());
        data.put("last30Days", statisticsService.getStatisticsForLast30Days());
        data.put("allTime", statisticsService.getStatisticsForAllTime());
        return Result.success(data);
    }
}
