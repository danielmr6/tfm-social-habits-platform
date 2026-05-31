package com.unir.socialhabits.repositories;

import com.unir.socialhabits.entities.Professional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for professionals.
 */
@Repository
public interface ProfessionalRepository
        extends JpaRepository<
        Professional,
        UUID
        > {

    Optional<Professional>
    findByEmail(
            String email
    );

}