package com.replicacia.rest.security;

import com.replicacia.rest.security.dto.UserRegisterRequestDTO;
import com.replicacia.rest.security.dto.UserRegisterResponseDTO;
import com.replicacia.rest.security.service.UserService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class UserRegistrationController {

  private final UserService userService;

  @PostMapping(value = "/signup")
  public ResponseEntity<UserRegisterResponseDTO> register(@Valid @RequestBody UserRegisterRequestDTO userRegisterRequestDTO){
    return ResponseEntity.created(URI.create("")).body(userService.register(userRegisterRequestDTO));
  }
}
