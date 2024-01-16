package com.replicacia.service;

import com.replicacia.model.Role;

import com.replicacia.rest.admin.dto.AssignRoleRequestDTO;
import java.util.List;
import java.util.Optional;

public interface RoleService {
  Role save(Role role);

  public String delete(Long roleId);

  List<Role> findAll();

  Role updateRole(Role role);

  void associateUserRole(AssignRoleRequestDTO assignRoleRequestDTO);

  String removeUserRole(Long userId, Long roleId);

  Optional<Role> findById(Long id);
}
