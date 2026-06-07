package com.unir.socialhabits.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="users")
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

    @ManyToOne
    @JsonBackReference
    private Professional professional;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Habit> habits;

    @OneToMany(mappedBy = "user")
    private List<Observation> observations;
}