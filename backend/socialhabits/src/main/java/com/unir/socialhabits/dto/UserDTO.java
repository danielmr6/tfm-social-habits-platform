package com.unir.socialhabits.dto;

import com.unir.socialhabits.entities.HabitGlobalStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Summary information returned to clients.
 */
@Getter
@Setter
public class UserDTO {

    private UUID id;

    private String firstName;

    private String lastName;

    private Integer age;

    private String phoneNumber;

    private HabitGlobalStatus habitStatus;

    private boolean hasMissingTodayHabits;

    private boolean hasRiskyHabitsToday;
}