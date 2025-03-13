package com.yiyayaya.shopmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class Roles {

    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer id;
    private String roleName;
    private String description;
    @TableField(exist = false)
    private List<Permissions> permissions;
}
