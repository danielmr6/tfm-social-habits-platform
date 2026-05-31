package com.unir.socialhabits.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="professionals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professional {

    @Id
    @GeneratedValue(
            strategy =
                    GenerationType.IDENTITY
    )
    private Long id;

    private String name;

    @Column(
            unique = true,
            nullable = false
    )
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy="professional")
    @JsonManagedReference
    private List<User> users;

}