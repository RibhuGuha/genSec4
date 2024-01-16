package com.replicacia.service.impl;

import com.replicacia.rest.admin.dto.AssignRoleRequestDTO;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.replicacia.model.AppUser;
import com.replicacia.model.Role;
import com.replicacia.repo.RoleRepository;
import com.replicacia.repo.UserRepository;
import com.replicacia.service.RoleService;
;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired private RoleRepository roleRepository;

  @Autowired private UserRepository userRepository;

  @Override
  public Role save(Role role) {
    return roleRepository.save(role);
  }

  @Override
  public String delete(Long roleId) {
    Optional<Role> role = roleRepository.findById(roleId);
    Role role1 = null;
    if (role.isPresent()) {
      role1 = role.get();
      if (role1.getAppUser() != null) {
        return "Role associated with user";
      } else {
        roleRepository.delete(role1);
        return "Role deleted";
      }
    } else {
      return "role no present";
    }
  }

  @Override
  public List<Role> findAll() {
    return roleRepository.findAll();
  }

  @Override
  public Role updateRole(Role role) {
    return roleRepository.save(role);
  }

  @Override
  public void associateUserRole(AssignRoleRequestDTO requestDTO) {
    Optional<AppUser> user = userRepository.findById(requestDTO.getUserId());
    Optional<Role> role = roleRepository.findById(requestDTO.getRoleId());
    if (user.isPresent() && role.isPresent()) {
      user.get().getRoles().add(role.get());
      userRepository.save(user.get());
    }
  }

  @Override
  public String removeUserRole(Long userId, Long roleId) {
    Optional<AppUser> user = userRepository.findById(userId);
    Optional<Role> role = roleRepository.findById(roleId);
    if (user.isPresent() && role.isPresent()) {
      role.get().setAppUser(null);
      user.get().getRoles().remove(role.get());
      userRepository.save(user.get());
    }
    return "Role associated with user";
  }

  @Override
  public Optional<Role> findById(Long id) {
    return roleRepository.findById(id);
  }
}
