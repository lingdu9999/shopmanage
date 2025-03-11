package com.yiyayaya.shopmanage.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Data
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String username;

    private String password;

    private String image;

    private String email;
    
    private String phone;

    private Integer role;

    private String message;

    private Integer issuse;

    @TableField(fill = FieldFill.DEFAULT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.DEFAULT)
    private LocalDateTime updatedAt;
}
