package com.money.trails.auth.controller;

import com.money.trails.auth.dto.AuthResponse;
import com.money.trails.auth.dto.SigninRequest;
import com.money.trails.auth.dto.SignupRequest;
import com.money.trails.auth.service.AuthService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "User login and register controller")
public class UserController {

    private final AuthService authService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "For successful sign in"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.accepted().body(authService.signin(request));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "For successful sign up"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok().body(authService.signup(request));
    }
}