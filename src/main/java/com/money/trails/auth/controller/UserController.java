package com.money.trails.auth.controller;

import com.money.trails.auth.dto.AuthResponse;
import com.money.trails.auth.dto.LoginRequest;
import com.money.trails.auth.service.AuthService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "UserController", description = "User login and register controller")
public class UserController {

    private AuthService authService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(authService.login(request));
    }
}