package com.yiyayaya.shopmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.UserActivity;
import com.yiyayaya.shopmanage.mapper.UserActivityMapper;
import com.yiyayaya.shopmanage.service.IUserActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActivityServiceImpl extends ServiceImpl<UserActivityMapper, UserActivity> implements IUserActivityService {

    @Autowired
    private UserActivityMapper userActivityMapper;

    @Override
    public void recordActivity(UserActivity activity) {
        userActivityMapper.insert(activity);
    }

    @Override
    public Pages<UserActivity> getUserActivities(int pageNum, int pageSize,String username) {
        Page<UserActivity> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UserActivity> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(username)) {
            queryWrapper.eq("username", username);
        }
        queryWrapper.orderByDesc("timestamp");
        Page<UserActivity> activityPage = userActivityMapper.selectPage(page, queryWrapper);
        return new Pages<>(activityPage.getRecords(), activityPage.getTotal(), activityPage.getCurrent(), activityPage.getSize());
    }
}
