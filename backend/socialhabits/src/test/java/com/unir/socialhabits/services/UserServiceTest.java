package com.unir.socialhabits.services;

import com.unir.socialhabits.dto.CreateUserDTO;
import com.unir.socialhabits.dto.UpdateUserDTO;
import com.unir.socialhabits.dto.UserDTO;
import com.unir.socialhabits.dto.UserDetailDTO;
import com.unir.socialhabits.entities.Professional;
import com.unir.socialhabits.entities.User;
import com.unir.socialhabits.entities.Habit;
import com.unir.socialhabits.entities.HabitGlobalStatus;
import com.unir.socialhabits.entities.HabitStatus;
import com.unir.socialhabits.entities.HabitType;
import com.unir.socialhabits.entities.Observation;
import com.unir.socialhabits.repositories.HabitRepository;
import com.unir.socialhabits.repositories.ProfessionalRepository;
import com.unir.socialhabits.repositories.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String EMAIL = "admin@test.com";

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private UserService userService;

    private Professional professional() {

        return Professional.builder()
                .id(1L)
                .name("Professional")
                .email(EMAIL)
                .build();
    }

    private User user() {

        User user = new User();

        user.setId(UUID.randomUUID());
        user.setFirstName("Daniel");
        user.setLastName("Martin");
        user.setAge(30);
        user.setPhoneNumber("666666666");
        user.setGeneralObservations("Everything OK");

        return user;
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createUser_shouldCreateUserSuccessfully() {

        Professional professional = professional();

        CreateUserDTO dto = new CreateUserDTO();

        dto.setFirstName("Daniel");
        dto.setLastName("Martin");
        dto.setAge(30);
        dto.setPhoneNumber("666666666");
        dto.setGeneralObservations("Everything OK");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        EMAIL,
                        null
                )
        );

        when(professionalRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(professional));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {

                    User saved = invocation.getArgument(0);
                    saved.setId(UUID.randomUUID());

                    return saved;

                });

        UserDTO result =
                userService.createUser(dto);

        assertNotNull(result);

        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
        assertEquals(dto.getAge(), result.getAge());

        verify(userRepository).save(any(User.class));

    }

    @Test
    void createUser_shouldSaveCorrectUser() {

        Professional professional = professional();

        CreateUserDTO dto = new CreateUserDTO();

        dto.setFirstName("Daniel");
        dto.setLastName("Martin");
        dto.setAge(30);
        dto.setPhoneNumber("666666666");
        dto.setGeneralObservations("Notes");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        EMAIL,
                        null
                )
        );

        when(professionalRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(professional));

        when(userRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userService.createUser(dto);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(captor.capture());

        User saved =
                captor.getValue();

        assertEquals(dto.getFirstName(), saved.getFirstName());
        assertEquals(dto.getLastName(), saved.getLastName());
        assertEquals(dto.getAge(), saved.getAge());
        assertEquals(dto.getPhoneNumber(), saved.getPhoneNumber());
        assertEquals(dto.getGeneralObservations(), saved.getGeneralObservations());
        assertEquals(professional, saved.getProfessional());

    }

    @Test
    void createUser_shouldThrowException_whenProfessionalDoesNotExist() {

        CreateUserDTO dto =
                new CreateUserDTO();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        EMAIL,
                        null
                )
        );

        when(professionalRepository.findByEmail(EMAIL))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> userService.createUser(dto)
        );

        verify(userRepository, never())
                .save(any());

    }

    @Test
    void getUserById_shouldReturnUser() {

        User user = user();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        UserDetailDTO dto =
                userService.getUserById(user.getId());

        assertNotNull(dto);

        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());

    }

    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> userService.getUserById(id)
        );

    }

    @Test
    void update_shouldUpdateUser() {

        User user = user();

        UpdateUserDTO dto =
                new UpdateUserDTO();

        dto.setFirstName("Updated");
        dto.setLastName("User");
        dto.setAge(40);
        dto.setPhoneNumber("777777777");
        dto.setGeneralObservations("Updated notes");

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        userService.update(user.getId(), dto);

        assertEquals("Updated", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals(40, user.getAge());
        assertEquals("777777777", user.getPhoneNumber());
        assertEquals("Updated notes", user.getGeneralObservations());

        verify(userRepository).save(user);

    }

    @Test
    void update_shouldThrowException_whenUserDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> userService.update(
                        id,
                        new UpdateUserDTO()
                )
        );

    }

    @Test
    void deleteUser_shouldDeleteUser() {

        User user = user();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());

        verify(userRepository)
                .delete(user);

    }

    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> userService.deleteUser(id)
        );

    }

}