package com.unir.socialhabits.controllers;

import com.unir.socialhabits.dto.*;
import com.unir.socialhabits.services.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login, registration and password management")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login user")
    @ApiResponse(responseCode = "200", description = "Successful login")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Send password reset email")
    @ApiResponse(responseCode = "200", description = "Email sent")
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @RequestBody Map<String, String> body
    ) {
        authService.sendPasswordReset(body.get("email"));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reset password using token")
    @ApiResponse(responseCode = "200", description = "Password updated")
    @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordDTO dto
    ) {
        authService.resetPassword(dto.getToken(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Register professional user")
    @ApiResponse(responseCode = "200", description = "User created")
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterProfessionalDTO dto
    ) {
        authService.register(dto);
        return ResponseEntity.ok().build();
    }
}