package com.unir.socialhabits.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Stores temporary password reset tokens.
 */
@Entity
@Getter
@Setter
@Table(name="password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Generated reset token.
     */
    private String token;

    /**
     * Email associated with the reset request.
     */
    private String email;

    /**
     * Expiration date.
     */
    private LocalDateTime expiryDate;

}