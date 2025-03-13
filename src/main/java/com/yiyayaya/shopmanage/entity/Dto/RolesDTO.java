package com.yiyayaya.shopmanage.entity.Dto;

import com.yiyayaya.shopmanage.entity.Roles;
import lombok.Data;

import java.util.List;

@Data
public class RolesDTO {
  private Roles roles;
  private List<Integer> permissions;
}
