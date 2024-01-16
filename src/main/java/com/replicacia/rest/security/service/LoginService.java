package com.replicacia.rest.security.service;

import com.replicacia.rest.security.dto.LoginRequestDTO;
import com.replicacia.rest.security.dto.LoginResponseDTO;

public interface LoginService {
  LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
