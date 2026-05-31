package com.unir.socialhabits.controllers;

import com.unir.socialhabits.services.UserService;

import com.unir.socialhabits.dto.LoginRequestDTO;
import com.unir.socialhabits.dto.LoginResponseDTO;
import com.unir.socialhabits.dto.CreateUserDTO;
import com.unir.socialhabits.dto.UserDTO;
import com.unir.socialhabits.services.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UsersController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(
            @RequestBody CreateUserDTO dto
    ) {

        return ResponseEntity.ok(
                userService.createUser(dto)
        );
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