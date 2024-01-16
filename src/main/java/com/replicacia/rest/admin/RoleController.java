package com.replicacia.rest.admin;

import com.replicacia.rest.admin.dto.AssignRoleRequestDTO;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.replicacia.exception.ResourceNotFoundException;
import com.replicacia.model.Role;
import com.replicacia.service.RoleService;

@RestController
@RequestMapping(value = "/app/role")
public class RoleController {

  @Autowired private RoleService roleService;

  @PostMapping("/create")
  // @PreAuthorize("hasAuthority('ROLE_WRITE')")
  public ResponseEntity<Role> createRole(@RequestBody Role role) {
    Role rl = roleService.save(role);

    URI location =
        ServletUriComponentsBuilder.fromPath("/update/{roleId}")
            .buildAndExpand(rl.getRoleId())
            .toUri();

    return ResponseEntity.created(location).body(rl);
  }

  @PutMapping("/update/{roleId}")
  // @PreAuthorize("hasAuthority('ROLE_UPDATE')")
  public ResponseEntity<Role> updateRole(
      @PathVariable(value = "roleId") Long roleId, @RequestBody Role role) {
    Role rl =
        roleService
            .findById(roleId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Role with ID :" + roleId + " Not Found!"));

    role.setRoleId(rl.getRoleId());
    roleService.updateRole(role);
    return ResponseEntity.ok().body(role);
  }

  @DeleteMapping("/delete/{roleId}")
  // @PreAuthorize("hasAuthority('ROLE_DELETE')")
  public ResponseEntity<String> deleteRole(@PathVariable(value = "roleId") Long roleId) {
    return ResponseEntity.ok().body(roleService.delete(roleId));
  }

  @GetMapping("/list")
  // @PreAuthorize("hasAuthority('ROLE_LIST')")
  public ResponseEntity<List<Role>> getRoleList() {
    return ResponseEntity.ok().body(roleService.findAll());
  }

  @PutMapping("/addUserRole")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  // @PreAuthorize("hasAuthority('ROLE_UPDATE')")
  public void associateUserRole(@RequestBody AssignRoleRequestDTO requestDTO) {
    roleService.associateUserRole(requestDTO);
  }

  @PutMapping("/removeUserRole/{userId}/{roleId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  // @PreAuthorize("hasAuthority('ROLE_UPDATE')")
  public void removeUserRole(@RequestBody AssignRoleRequestDTO requestDTO) {
    roleService.associateUserRole(requestDTO);
  }

  @GetMapping("/{roleId}")
  // @PreAuthorize("hasAuthority('ROLE_LIST')")
  public ResponseEntity<Role> getRole(@PathVariable(value = "roleId") Long roleId) {
    Role role =
        roleService
            .findById(roleId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Role with ID :" + roleId + " Not Found!"));

    return ResponseEntity.ok().body(role);
  }
}
