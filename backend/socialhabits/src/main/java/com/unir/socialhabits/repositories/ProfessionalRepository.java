package com.unir.socialhabits.repositories;

import com.unir.socialhabits.entities.Professional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for professionals.
 */
public interface ProfessionalRepository
        extends JpaRepository<Professional, UUID> {

    Optional<Professional> findByEmail(String email);

}