package com.unir.socialhabits.services;

import com.unir.socialhabits.dto.HabitDTO;
import com.unir.socialhabits.entities.Habit;
import com.unir.socialhabits.entities.HabitStatus;
import com.unir.socialhabits.entities.HabitType;
import com.unir.socialhabits.entities.User;
import com.unir.socialhabits.repositories.HabitRepository;
import com.unir.socialhabits.repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private HabitService habitService;

    private User user() {

        User user = new User();

        user.setId(UUID.randomUUID());
        user.setFirstName("Daniel");
        user.setLastName("Martin");

        return user;
    }

    private HabitDTO habitDTO() {

        HabitDTO dto = new HabitDTO();

        dto.setType(HabitType.SLEEP);
        dto.setStatus(HabitStatus.CORRECT);
        dto.setDescription("Sleep 8 hours");
        dto.setDate(LocalDate.now());

        return dto;
    }

    @Test
    void createHabit_shouldCreateHabitSuccessfully() {

        User user = user();

        HabitDTO dto = habitDTO();

        Habit savedHabit = Habit.builder()
                .id(UUID.randomUUID())
                .user(user)
                .type(dto.getType())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .date(dto.getDate())
                .build();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(habitRepository.save(any(Habit.class)))
                .thenReturn(savedHabit);

        HabitDTO result =
                habitService.createHabit(user.getId(), dto);

        assertNotNull(result);

        assertEquals(dto.getType(), result.getType());
        assertEquals(dto.getStatus(), result.getStatus());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getDate(), result.getDate());

        verify(userService).updateUserStatus(user);

    }

    @Test
    void createHabit_shouldUseCurrentDate_whenDateIsNull() {

        User user = user();

        HabitDTO dto = habitDTO();
        dto.setDate(null);

        Habit savedHabit = Habit.builder()
                .id(UUID.randomUUID())
                .user(user)
                .type(dto.getType())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .date(LocalDate.now())
                .build();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(habitRepository.save(any(Habit.class)))
                .thenReturn(savedHabit);

        HabitDTO result =
                habitService.createHabit(user.getId(), dto);

        assertEquals(LocalDate.now(), result.getDate());

    }

    @Test
    void createHabit_shouldThrowException_whenUserDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> habitService.createHabit(id, habitDTO())
        );

        verify(habitRepository, never()).save(any());

    }

    @Test
    void createHabit_shouldSaveCorrectHabit() {

        User user = user();

        HabitDTO dto = habitDTO();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(habitRepository.save(any(Habit.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        habitService.createHabit(user.getId(), dto);

        ArgumentCaptor<Habit> captor =
                ArgumentCaptor.forClass(Habit.class);

        verify(habitRepository).save(captor.capture());

        Habit saved = captor.getValue();

        assertEquals(user, saved.getUser());
        assertEquals(dto.getType(), saved.getType());
        assertEquals(dto.getStatus(), saved.getStatus());
        assertEquals(dto.getDescription(), saved.getDescription());

    }

    @Test
    void deleteHabit_shouldDeleteHabitSuccessfully() {

        User user = user();

        Habit habit = Habit.builder()
                .id(UUID.randomUUID())
                .user(user)
                .build();

        when(habitRepository.findById(habit.getId()))
                .thenReturn(Optional.of(habit));

        habitService.deleteHabit(habit.getId());

        verify(habitRepository).delete(habit);

        verify(userService).updateUserStatus(user);

    }

    @Test
    void deleteHabit_shouldThrowException_whenHabitDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(habitRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> habitService.deleteHabit(id)
        );

        verify(habitRepository, never()).delete(any());

    }
}