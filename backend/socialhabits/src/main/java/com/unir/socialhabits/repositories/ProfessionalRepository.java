package com.unir.socialhabits.repositories;

import com.unir.socialhabits.entities.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionalRepository
        extends JpaRepository<Professional, Long> {

    Optional<Professional> findByEmail(String email);
}