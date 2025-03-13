package com.yiyayaya.shopmanage.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class UserActivity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String username; // 新增字段
    private String action;
    private String details;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;


}
