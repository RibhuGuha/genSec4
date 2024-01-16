package com.replicacia.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.replicacia.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
