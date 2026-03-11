package com.datnguyeni.shop_backend.controller;

import com.datnguyeni.shop_backend.dto.requestDTO.LoginRequest;
import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.LoginResponse;
import com.datnguyeni.shop_backend.service.AuthService;
import com.datnguyeni.shop_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController( AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws Exception{

        String token = authService.login(request);

        return ResponseEntity.ok(
                new LoginResponse(token, "Bearer")
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        authService.logout(token);

        return ResponseEntity.ok("Logged out successfully");
    }


}
