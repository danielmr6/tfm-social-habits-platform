package com.unir.socialhabits.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String lastName;

    private Integer age;

    private String phoneNumber;

    private String generalObservations;
}