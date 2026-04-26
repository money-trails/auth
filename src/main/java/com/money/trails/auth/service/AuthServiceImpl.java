package com.money.trails.auth.service;

import com.money.trails.auth.dto.AuthResponse;
import com.money.trails.auth.dto.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    public AuthResponse login(LoginRequest request) {
        return new AuthResponse("some-token");
    }
}