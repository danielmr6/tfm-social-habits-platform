package com.unir.socialhabits.controllers;

import com.unir.socialhabits.services.UserService;

import com.unir.socialhabits.dto.*;
import com.unir.socialhabits.services.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management operations")
public class UsersController {

    private final UserService userService;

    @Operation(summary = "Create user")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public UserDetailDTO getUser(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserDTO dto
    ) {
        userService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @Operation(summary = "List users with pagination")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getUsers(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getUsers(search, page, size));
    }
}