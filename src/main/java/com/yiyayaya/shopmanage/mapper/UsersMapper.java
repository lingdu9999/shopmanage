package com.yiyayaya.shopmanage.mapper;

import com.yiyayaya.shopmanage.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    
    @Select("SELECT * FROM users WHERE username LIKE CONCAT('%', #{username}, '%')")
    List<Users> selectByUsernameLike(String username);

    @Select("SELECT * FROM users WHERE role == role AND username LIKE CONCAT('%', #{username}, '%')")
    List<Users> selectByUsernameLikeAndRole(String username,Integer role);
}
