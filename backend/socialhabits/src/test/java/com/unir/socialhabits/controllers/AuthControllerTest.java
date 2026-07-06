package com.unir.socialhabits.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unir.socialhabits.dto.LoginRequestDTO;
import com.unir.socialhabits.dto.LoginResponseDTO;
import com.unir.socialhabits.dto.RegisterProfessionalDTO;
import com.unir.socialhabits.dto.ResetPasswordDTO;
import com.unir.socialhabits.security.JwtAuthenticationFilter;
import com.unir.socialhabits.security.JwtService;
import com.unir.socialhabits.services.AuthService;
import com.unir.socialhabits.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
@TestPropertySource(properties = {
        "app.frontend-url=http://localhost:3000"
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void login_shouldReturn200() throws Exception {

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("admin@test.com");
        dto.setPassword("12345678");

        when(authService.login(any()))
                .thenReturn(new LoginResponseDTO("jwt-token"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));

    }

    @Test
    void register_shouldReturn200() throws Exception {

        RegisterProfessionalDTO dto =
                new RegisterProfessionalDTO();

        dto.setName("Daniel");
        dto.setEmail("admin@test.com");
        dto.setPassword("12345678");

        doNothing().when(authService)
                .register(any());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

    }

    @Test
    void forgotPassword_shouldReturn200() throws Exception {

        doNothing().when(authService)
                .sendPasswordReset(any());

        mockMvc.perform(post("/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("email", "admin@test.com"))))
                .andExpect(status().isOk());

    }

    @Test
    void resetPassword_shouldReturn200() throws Exception {

        ResetPasswordDTO dto =
                new ResetPasswordDTO();

        dto.setToken("abc");
        dto.setNewPassword("12345678");

        doNothing().when(authService)
                .resetPassword(any(), any());

        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

    }

    @Test
    void login_shouldReturn400_whenEmailIsInvalid() throws Exception {

        LoginRequestDTO dto =
                new LoginRequestDTO();

        dto.setEmail("correo");
        dto.setPassword("12345678");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

    }

}