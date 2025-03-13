package com.yiyayaya.shopmanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiyayaya.shopmanage.entity.Permissions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

import java.util.List;

@Mapper
public interface PermissionsMapper extends BaseMapper<Permissions> {
    // Additional custom methods can be defined here if needed

    @Select("SELECT p.permission_id, p.permission_name, p.permission_code, p.url, p.page_url " +
            "FROM permissions p " +
            "LEFT JOIN role_permission rp ON p.permission_id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    @Results({
        @Result(column = "permission_id", property = "id"),
        @Result(column = "permission_name", property = "permissionName"),
        @Result(column = "permission_code", property = "permissionCode"),
        @Result(column = "url", property = "url"),
        @Result(column = "page_url", property = "pageUrl")
    })
    List<Permissions> getPermissionsByRoleId(Integer roleId);

    @Select("SELECT p.permission_id, p.permission_name, p.permission_code, p.url, p.page_url " +
            "FROM permissions p " +
            "LEFT JOIN role_permission rp ON p.permission_id = rp.permission_id " +
            "LEFT JOIN users u ON rp.role_id = u.role " +
            "WHERE u.user_id = #{userId}")
    @Results({
        @Result(column = "permission_id", property = "id"),
        @Result(column = "permission_name", property = "permissionName"),
        @Result(column = "permission_code", property = "permissionCode"),
        @Result(column = "url", property = "url"),
        @Result(column = "page_url", property = "pageUrl")
    })
    List<Permissions> getPermissionsByUserId(Integer userId);

    @Select("SELECT COUNT(*) FROM role_permission WHERE permission_id = #{permissionId}")
    int countRolesByPermissionId(Integer permissionId);
}
