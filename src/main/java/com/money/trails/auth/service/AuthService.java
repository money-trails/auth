package com.money.trails.auth.service;

import com.money.trails.auth.dto.AuthResponse;
import com.money.trails.auth.dto.LoginRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);
}