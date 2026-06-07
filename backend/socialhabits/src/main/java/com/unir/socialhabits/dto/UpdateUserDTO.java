package com.unir.socialhabits.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Summary information returned to clients.
 */
@Getter
@Setter
public class UpdateUserDTO {
    private String firstName;

    private String lastName;

    private Integer age;

    private String phoneNumber;

    private String generalObservations;
}