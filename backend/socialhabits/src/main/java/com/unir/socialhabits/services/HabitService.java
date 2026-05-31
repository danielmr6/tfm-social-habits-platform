package com.unir.socialhabits.services;

import com.unir.socialhabits.entities.*;
import com.unir.socialhabits.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    public Habit createHabit(UUID userId, Habit habit) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        habit.setUser(user);

        return habitRepository.save(habit);
    }

    public void deleteHabit(UUID id) {
        habitRepository.deleteById(id);
    }
}