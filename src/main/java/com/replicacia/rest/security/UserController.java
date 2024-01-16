package com.replicacia.rest.security;

import com.replicacia.exception.ResourceNotFoundException;
import com.replicacia.model.AppUser;
import com.replicacia.rest.security.dto.UpdatePasswordRequestDTO;
import com.replicacia.rest.security.dto.UserResponseDTO;
import com.replicacia.rest.security.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/app/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final ModelMapper mapper;

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponseDTO> getUserByUserId(@PathVariable(value = "userId") Long userId) {
    AppUser appUser = userService.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID :" + userId + " Not Found!"));
    return ResponseEntity.ok().body(mapper.map(appUser, UserResponseDTO.class));
  }

  @PutMapping("/verify")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateVerifiedUserFlag(@RequestParam Long userId) {
    Optional<AppUser> appUser = userService.findById(userId);
    appUser.ifPresent(
        user -> {
          AppUser updatedUser = appUser.get();
          updatedUser.setIsEmailVerified(Boolean.TRUE);
          updatedUser.setActive(Boolean.TRUE);
          userService.update(updatedUser);
        }
    );
  }

  @PutMapping("/forgotPassword")
  public ResponseEntity<?> forgotPassword(@RequestParam String emailId){
    Optional<AppUser> appUser = userService.findByEmail(emailId);
    if(appUser.isPresent()){
          return userService.forgotPassword(emailId);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email id is not registered with us.");
    }
  }

  @PutMapping("/updatePassword")
  public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequestDTO requestDTO){
    Optional<AppUser> appUser = userService.findByEmail(requestDTO.getEmail());
    if(appUser.isPresent() && requestDTO.getNewPassword().equals(requestDTO.getVerifyPassword())){
      AppUser user = appUser.get();
      user.setPasswordChanged(true);
      user.setPassword(requestDTO.getNewPassword());
      userService.update(user);
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(
              UserResponseDTO.builder()
                  .userId(user.getUserId())
                  .active(user.getActive())
                  .email(requestDTO.getEmail())
                  .isEmailVerified(user.getIsEmailVerified())
                  .country(user.getCountry())
                  .userName(user.getUsername())
                  .build()
          );
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}
