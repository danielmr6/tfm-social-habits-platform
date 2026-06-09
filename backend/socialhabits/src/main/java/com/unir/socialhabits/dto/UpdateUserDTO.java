package com.unir.socialhabits.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Min(1)
    @Max(120)
    private Integer age;

    @Pattern(
            regexp = "^[0-9+ ]{6,20}$",
            message = "Invalid phone number"
    )
    private String phoneNumber;

    @Size(max = 1000)
    private String generalObservations;
}