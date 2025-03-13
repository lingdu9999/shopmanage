package com.yiyayaya.shopmanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.UserActivity;

public interface IUserActivityService extends IService<UserActivity> {
    void recordActivity(UserActivity activity);

    /**
     * 获取用户行为记录
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 用户行为记录分页
     */
    Pages<UserActivity> getUserActivities(int pageNum, int pageSize,String username);
}
