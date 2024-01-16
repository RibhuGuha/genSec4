package com.replicacia.service;

import org.springframework.security.core.userdetails.User;

public interface AuthenticatedUserService {
    public User getAuthenticatedUser();
}