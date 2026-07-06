package com.unir.socialhabits.services;

import com.unir.socialhabits.dto.CreateObservationDTO;
import com.unir.socialhabits.dto.ObservationDTO;
import com.unir.socialhabits.entities.Observation;
import com.unir.socialhabits.entities.Professional;
import com.unir.socialhabits.entities.User;
import com.unir.socialhabits.repositories.ObservationRepository;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObservationServiceTest {

    private static final String EMAIL = "admin@test.com";

    @Mock
    private ObservationRepository observationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private ObservationService observationService;

    private User user() {

        User user = new User();

        user.setId(UUID.randomUUID());
        user.setFirstName("Daniel");

        return user;
    }

    private Professional professional() {

        return Professional.builder()
                .name("Professional")
                .email(EMAIL)
                .build();
    }

    @AfterEach
    void cleanSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createObservation_shouldCreateObservationSuccessfully() {

        User user = user();

        Professional professional = professional();

        CreateObservationDTO dto =
                new CreateObservationDTO();

        dto.setContent("Everything is fine");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        EMAIL,
                        null
                )
        );

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(professionalRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(professional));

        Observation saved =
                Observation.builder()
                        .id(UUID.randomUUID())
                        .user(user)
                        .professional(professional)
                        .content(dto.getContent())
                        .createdAt(LocalDateTime.now())
                        .build();

        when(observationRepository.save(any()))
                .thenReturn(saved);

        ObservationDTO result =
                observationService.createObservation(
                        user.getId(),
                        dto
                );

        assertNotNull(result);

        assertEquals(
                dto.getContent(),
                result.getContent()
        );

        assertEquals(
                professional.getName(),
                result.getProfessionalName()
        );

        verify(observationRepository)
                .save(any(Observation.class));

    }

    @Test
    void createObservation_shouldSaveCorrectObservation() {

        User user = user();

        Professional professional = professional();

        CreateObservationDTO dto =
                new CreateObservationDTO();

        dto.setContent("New observation");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        EMAIL,
                        null
                )
        );

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(professionalRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(professional));

        when(observationRepository.save(any()))
                .thenAnswer(invocation -> {

                    Observation observation =
                            invocation.getArgument(0);

                    observation.setId(UUID.randomUUID());

                    observation.setCreatedAt(LocalDateTime.now());

                    return observation;

                });

        observationService.createObservation(
                user.getId(),
                dto
        );

        ArgumentCaptor<Observation> captor =
                ArgumentCaptor.forClass(Observation.class);

        verify(observationRepository)
                .save(captor.capture());

        Observation observation =
                captor.getValue();

        assertEquals(
                dto.getContent(),
                observation.getContent()
        );

        assertEquals(
                user,
                observation.getUser()
        );

        assertEquals(
                professional,
                observation.getProfessional()
        );

    }

    @Test
    void createObservation_shouldThrowException_whenUserDoesNotExist() {

        UUID id = UUID.randomUUID();

        CreateObservationDTO dto =
                new CreateObservationDTO();

        dto.setContent("Test");

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> observationService.createObservation(
                        id,
                        dto
                )
        );

        verify(observationRepository, never())
                .save(any());

    }

    @Test
    void createObservation_shouldThrowException_whenProfessionalDoesNotExist() {

        User user = user();

        CreateObservationDTO dto =
                new CreateObservationDTO();

        dto.setContent("Test");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        EMAIL,
                        null
                )
        );

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(professionalRepository.findByEmail(EMAIL))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> observationService.createObservation(
                        user.getId(),
                        dto
                )
        );

        verify(observationRepository, never())
                .save(any());

    }

    @Test
    void deleteObservation_shouldDeleteObservation() {

        UUID id = UUID.randomUUID();

        observationService.deleteObservation(id);

        verify(observationRepository)
                .deleteById(id);

    }

}