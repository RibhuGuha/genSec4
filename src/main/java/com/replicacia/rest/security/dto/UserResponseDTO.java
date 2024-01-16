package com.replicacia.rest.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
  private Long userId;
  private String userName;
  private String email;
  private String country;
  private Boolean isEmailVerified;
  private Boolean active;
}
