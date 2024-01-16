package com.replicacia.rest.security.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequestDTO implements Serializable {
	private static final long serialVersionUID = 5926468583005150707L;
	private String userName;
	private String password;
}