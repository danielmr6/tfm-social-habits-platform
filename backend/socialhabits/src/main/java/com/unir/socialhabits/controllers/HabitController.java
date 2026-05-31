package com.unir.socialhabits.controllers;

import com.unir.socialhabits.entities.Habit;
import com.unir.socialhabits.services.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @PostMapping("/{userId}")
    public Habit createHabit(
            @PathVariable UUID userId,
            @RequestBody Habit habit
    ) {
        return habitService.createHabit(userId, habit);
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable UUID id) {
        habitService.deleteHabit(id);
    }
}