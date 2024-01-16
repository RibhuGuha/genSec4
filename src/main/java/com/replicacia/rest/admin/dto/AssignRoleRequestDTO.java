package com.replicacia.rest.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignRoleRequestDTO {

  private Long userId;
  private Long roleId;

}
