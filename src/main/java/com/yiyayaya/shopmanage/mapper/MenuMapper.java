package com.yiyayaya.shopmanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiyayaya.shopmanage.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    // 可以添加自定义查询方法
} 