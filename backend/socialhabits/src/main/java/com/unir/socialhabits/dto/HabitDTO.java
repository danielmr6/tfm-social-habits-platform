package com.unir.socialhabits.dto;

import com.unir.socialhabits.entities.HabitStatus;
import com.unir.socialhabits.entities.HabitType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class HabitDTO {

    private UUID id;

    private HabitType type;

    private HabitStatus status;

    private String description;

    private LocalDate date;
}