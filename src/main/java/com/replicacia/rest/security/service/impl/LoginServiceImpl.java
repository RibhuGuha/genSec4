package com.replicacia.rest.security.service.impl;

import com.replicacia.rest.security.util.JwtTokenUtil;
import com.replicacia.rest.security.dto.LoginRequestDTO;
import com.replicacia.rest.security.dto.LoginResponseDTO;
import com.replicacia.rest.security.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@CrossOrigin
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;

  @Override
  public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUserName(), loginRequestDTO.getPassword()));

    final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    final String token = jwtTokenUtil.generateToken(userDetails);

    return LoginResponseDTO.builder().type("Bearer").token(token).build();
  }
}
