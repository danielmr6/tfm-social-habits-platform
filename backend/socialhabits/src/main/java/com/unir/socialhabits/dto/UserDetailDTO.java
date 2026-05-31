package com.unir.socialhabits.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserDetailDTO {

    private UUID id;

    private String firstName;

    private String lastName;

    private Integer age;

    private List<HabitDTO> habits;

    private List<ObservationDTO> observations;
}