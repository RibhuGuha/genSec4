package com.replicacia.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.replicacia.exception.ResourceNotFoundException;
import com.replicacia.model.AppUser;
import com.replicacia.rest.security.service.UserService;

//@RestController
@RequestMapping(value = "/")
public class UserController implements UserApi {

    @Autowired
    private UserService userService;

    @Override
    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(@RequestBody AppUser user) {
        AppUser appUser = userService.save(user);
        URI location = ServletUriComponentsBuilder.fromPath("/app/user/update/{userId}")
                .buildAndExpand(appUser.getUserId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    @PutMapping("/app/user/update/{userId}")
    @PreAuthorize("updateOwnRecord(#userId)")
    public ResponseEntity<AppUser> updateUser(@PathVariable(value = "userId") Long userId, @RequestBody AppUser user) {
        AppUser appUser = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID :" + userId + " Not Found!"));
        userService.update(user);
        return ResponseEntity.ok().body(user);
    }

    @Override
    @GetMapping("/app/user/{userId}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<AppUser> getUser(@PathVariable(value = "userId") Long userId) {
        AppUser appUser = userService.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID :" + userId + " Not Found!"));
        return ResponseEntity.ok().body(appUser);
    }

    @Override
    @GetMapping("/app/user/{username}/profile")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable(value = "username") String username) {
        AppUser appUser = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with Username :" + username + " Not Found!"));
        return ResponseEntity.ok().body(appUser);
    }

    @Override
    @GetMapping("app/user/list/{active}")
    @PreAuthorize("hasAuthority('USER_LIST')")
    public ResponseEntity<List<AppUser>> getAllUsersByStatus(@PathVariable(value = "active") Boolean active) {
        return ResponseEntity.ok().body(userService.findAllByActive(active));
    }
}