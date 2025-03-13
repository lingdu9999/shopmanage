package com.yiyayaya.shopmanage.mapper;

import com.yiyayaya.shopmanage.entity.OrderItemAttributes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Mapper
public interface OrderItemAttributesMapper extends BaseMapper<OrderItemAttributes> {

    @Select("SELECT COUNT(*) FROM order_items WHERE product_id = #{productId}")
    int countOrdersByProductId(Integer productId);
}
