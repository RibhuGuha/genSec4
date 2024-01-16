package com.replicacia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.replicacia.model.AppUser;


public interface UserApi {

	public ResponseEntity<?> createUser(AppUser user);
	public ResponseEntity<AppUser>  updateUser(Long userId,AppUser user);
	public ResponseEntity<List<AppUser>> getAllUsersByStatus(Boolean active);
	public ResponseEntity<AppUser> getUser(Long userId);
	public ResponseEntity<AppUser> getUserByUsername(String username);

}
