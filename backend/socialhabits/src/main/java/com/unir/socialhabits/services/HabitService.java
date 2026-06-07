package com.unir.socialhabits.services;

import com.unir.socialhabits.entities.*;
import com.unir.socialhabits.dto.*;
import com.unir.socialhabits.repositories.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public HabitDTO createHabit(UUID userId, HabitDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Habit habit = Habit.builder()
                .user(user)
                .type(dto.getType())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .date(dto.getDate() != null ? dto.getDate() : LocalDate.now())
                .build();

        Habit saved = habitRepository.save(habit);

        userService.updateUserStatus(user);

        return mapToDTO(saved);
    }

    public void deleteHabit(UUID habitId) {

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        User user = habit.getUser();

        habitRepository.delete(habit);

        userService.updateUserStatus(user);
    }

    private HabitDTO mapToDTO(Habit habit) {

        HabitDTO dto = new HabitDTO();

        dto.setId(habit.getId());
        dto.setType(habit.getType());
        dto.setStatus(habit.getStatus());
        dto.setDescription(habit.getDescription());
        dto.setDate(habit.getDate());

        return dto;
    }
}