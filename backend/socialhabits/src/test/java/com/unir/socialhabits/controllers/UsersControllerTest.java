package com.unir.socialhabits.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unir.socialhabits.dto.CreateUserDTO;
import com.unir.socialhabits.dto.UpdateUserDTO;
import com.unir.socialhabits.dto.UserDTO;
import com.unir.socialhabits.dto.UserDetailDTO;
import com.unir.socialhabits.security.JwtAuthenticationFilter;
import com.unir.socialhabits.security.JwtService;
import com.unir.socialhabits.services.UserService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.PageImpl;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;

    @Test
    void createUser_shouldReturn200() throws Exception {

        CreateUserDTO dto =
                new CreateUserDTO();

        dto.setFirstName("Daniel");
        dto.setLastName("Martin");
        dto.setAge(30);

        UserDTO response =
                new UserDTO();

        response.setId(UUID.randomUUID());
        response.setFirstName("Daniel");

        when(userService.createUser(any()))
                .thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Daniel"));

    }

    @Test
    void getUser_shouldReturnUser() throws Exception {

        UUID id = UUID.randomUUID();

        UserDetailDTO dto =
                new UserDetailDTO();

        dto.setId(id);
        dto.setFirstName("Daniel");

        when(userService.getUserById(id))
                .thenReturn(dto);

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Daniel"));

    }

    @Test
    void updateUser_shouldReturn200() throws Exception {

        UUID id = UUID.randomUUID();

        UpdateUserDTO dto =
                new UpdateUserDTO();

        dto.setFirstName("Updated");

        doNothing().when(userService)
                .update(eq(id), any());

        mockMvc.perform(put("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

    }

    @Test
    void deleteUser_shouldReturn200() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing().when(userService)
                .deleteUser(id);

        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isOk());

    }

    @Test
    void getUsers_shouldReturnPage() throws Exception {

        UserDTO dto =
                new UserDTO();

        dto.setId(UUID.randomUUID());
        dto.setFirstName("Daniel");

        when(userService.getUsers(anyString(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName")
                        .value("Daniel"));

    }

}