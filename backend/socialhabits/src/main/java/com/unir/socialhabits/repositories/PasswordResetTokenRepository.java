package com.unir.socialhabits.repositories;

import com.unir.socialhabits.entities.PasswordResetToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for password recovery tokens.
 */
public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByEmail(String email);

}