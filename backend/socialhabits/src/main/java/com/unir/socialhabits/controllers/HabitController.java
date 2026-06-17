package com.unir.socialhabits.controllers;

import com.unir.socialhabits.dto.*;
import com.unir.socialhabits.entities.Habit;
import com.unir.socialhabits.services.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.UUID;

@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
@Tag(name = "Habits", description = "Habit management")
public class HabitController {

    private final HabitService habitService;

    @Operation(summary = "Create habit for user")
    @PostMapping("/{userId}")
    public HabitDTO createHabit(
            @PathVariable UUID userId,
            @RequestBody HabitDTO dto
    ) {
        return habitService.createHabit(userId, dto);
    }

    @Operation(summary = "Delete habit")
    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable UUID id) {
        habitService.deleteHabit(id);
    }
}