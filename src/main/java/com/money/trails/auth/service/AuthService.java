package com.money.trails.auth.service;

import com.money.trails.auth.dto.AuthResponse;
import com.money.trails.auth.dto.SigninRequest;
import com.money.trails.auth.dto.SignupRequest;

public interface AuthService {

    AuthResponse signin(SigninRequest request);
    AuthResponse signup(SignupRequest request);
}