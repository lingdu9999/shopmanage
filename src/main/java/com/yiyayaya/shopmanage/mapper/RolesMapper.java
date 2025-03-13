package com.yiyayaya.shopmanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiyayaya.shopmanage.entity.Roles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

@Mapper
public interface RolesMapper extends BaseMapper<Roles> {
    // Additional custom methods can be defined here if needed

    @Select("SELECT r.role_id, r.role_name, r.description " +
            "FROM roles r")
    @Results({
        @Result(column = "role_id", property = "id"),
        @Result(column = "role_name", property = "roleName"),
        @Result(column = "description", property = "description"),
        @Result(column = "role_id", property = "permissions", 
                many = @Many(select = "com.yiyayaya.shopmanage.mapper.PermissionsMapper.getPermissionsByRoleId"))
    })
    List<Roles> getAllRolesWithPermissions();

    @Delete("DELETE FROM role_permission WHERE role_id = #{roleId}")
    void deleteRolePermissions(Integer roleId);

    @Insert("INSERT INTO role_permission (role_id, permission_id) VALUES (#{roleId}, #{permissionId})")
    void insertRolePermission(Integer roleId, Integer permissionId);
}
