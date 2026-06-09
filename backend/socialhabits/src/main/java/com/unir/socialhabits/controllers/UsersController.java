package com.unir.socialhabits.controllers;

import com.unir.socialhabits.services.UserService;

import com.unir.socialhabits.dto.*;
import com.unir.socialhabits.services.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UsersController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(
            @Valid @RequestBody CreateUserDTO dto
    ) {

        return ResponseEntity.ok(
                userService.createUser(dto)
        );
    }

    @GetMapping("/{id}")
    public UserDetailDTO getUser(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserDTO dto
    ) {
        userService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getUsers(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                userService.getUsers(search,page,size)
        );
    }
}