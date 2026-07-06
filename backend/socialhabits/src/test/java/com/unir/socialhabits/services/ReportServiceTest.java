package com.unir.socialhabits.services;

import com.unir.socialhabits.dto.HabitDTO;
import com.unir.socialhabits.dto.ObservationDTO;
import com.unir.socialhabits.dto.UserDetailDTO;
import com.unir.socialhabits.entities.HabitGlobalStatus;
import com.unir.socialhabits.entities.HabitStatus;
import com.unir.socialhabits.entities.HabitType;
import com.unir.socialhabits.entities.User;
import com.unir.socialhabits.repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReportService reportService;

    private User user() {

        User user = new User();

        user.setId(UUID.randomUUID());
        user.setFirstName("Daniel");
        user.setLastName("Martin");

        return user;
    }

    private UserDetailDTO detailDTO() {

        UserDetailDTO dto = new UserDetailDTO();

        dto.setId(UUID.randomUUID());

        dto.setFirstName("Daniel");
        dto.setLastName("Martin");

        dto.setAge(30);

        dto.setGeneralObservations("Everything OK");

        dto.setHabitStatus(HabitGlobalStatus.OK);

        dto.setMissingTodayHabits(false);

        dto.setRiskyHabitsToday(false);

        HabitDTO habit = new HabitDTO();

        habit.setType(HabitType.SLEEP);

        habit.setStatus(HabitStatus.CORRECT);

        habit.setDate(LocalDate.now());

        habit.setDescription("Sleep 8 hours");

        dto.setHabits(List.of(habit));

        ObservationDTO observation =
                new ObservationDTO();

        observation.setContent("Very good progress");

        observation.setProfessionalName("Professional");

        observation.setCreatedAt(LocalDateTime.now());

        dto.setObservations(List.of(observation));

        return dto;
    }

    @Test
    void generateUserReport_shouldGeneratePdfSuccessfully() {

        User user = user();

        UserDetailDTO dto = detailDTO();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(userService.toDetailDTO(user))
                .thenReturn(dto);

        byte[] pdf =
                reportService.generateUserReport(
                        user.getId()
                );

        assertNotNull(pdf);

        assertTrue(pdf.length > 0);

        verify(userRepository)
                .findById(user.getId());

        verify(userService)
                .toDetailDTO(user);

    }

    @Test
    void generateUserReport_shouldThrowException_whenUserDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> reportService.generateUserReport(id)
        );

        verify(userService, never())
                .toDetailDTO(any());

    }

    @Test
    void generateUserReport_shouldGeneratePdf_whenNoHabitsExist() {

        User user = user();

        UserDetailDTO dto = detailDTO();

        dto.setHabits(List.of());

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(userService.toDetailDTO(user))
                .thenReturn(dto);

        byte[] pdf =
                reportService.generateUserReport(
                        user.getId()
                );

        assertNotNull(pdf);

        assertTrue(pdf.length > 0);

    }

    @Test
    void generateUserReport_shouldGeneratePdf_whenNoObservationsExist() {

        User user = user();

        UserDetailDTO dto = detailDTO();

        dto.setObservations(List.of());

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(userService.toDetailDTO(user))
                .thenReturn(dto);

        byte[] pdf =
                reportService.generateUserReport(
                        user.getId()
                );

        assertNotNull(pdf);

        assertTrue(pdf.length > 0);

    }

    @Test
    void generateUserReport_shouldGeneratePdf_whenNoHabitsAndNoObservationsExist() {

        User user = user();

        UserDetailDTO dto = detailDTO();

        dto.setHabits(List.of());

        dto.setObservations(List.of());

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(userService.toDetailDTO(user))
                .thenReturn(dto);

        byte[] pdf =
                reportService.generateUserReport(
                        user.getId()
                );

        assertNotNull(pdf);

        assertTrue(pdf.length > 0);

    }
}