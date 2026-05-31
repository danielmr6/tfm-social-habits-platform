package com.unir.socialhabits.dto;

import lombok.*;

@Getter
@Setter
public class CreateUserDTO {

    private String firstName;

    private String lastName;

    private Integer age;

    private String phoneNumber;

    private String generalObservations;
}