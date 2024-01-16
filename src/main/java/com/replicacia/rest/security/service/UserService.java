package com.replicacia.rest.security.service;

import com.replicacia.rest.security.dto.UserRegisterRequestDTO;
import com.replicacia.rest.security.dto.UserRegisterResponseDTO;
import java.util.List;
import java.util.Optional;

import com.replicacia.model.AppUser;
import org.springframework.http.ResponseEntity;

public interface UserService {
	AppUser save(AppUser user);

	AppUser update(AppUser user);

	List<AppUser> findAllByActive(Boolean active);

	AppUser findAllByUserName(String userName);

	Optional<AppUser>  findById(Long userId);

	Optional<AppUser>  findByUsername(String username);

	Optional<AppUser> findByEmail(String email);

	UserRegisterResponseDTO register(UserRegisterRequestDTO userRegisterRequestDTO);

	ResponseEntity<String> forgotPassword(String emailId);

}
