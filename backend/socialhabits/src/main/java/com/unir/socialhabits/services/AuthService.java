package com.unir.socialhabits.services;

import com.unir.socialhabits.dto.LoginRequestDTO;
import com.unir.socialhabits.dto.LoginResponseDTO;
import com.unir.socialhabits.dto.RegisterProfessionalDTO;
import com.unir.socialhabits.entities.Professional;
import com.unir.socialhabits.entities.PasswordResetToken;
import com.unir.socialhabits.repositories.ProfessionalRepository;
import com.unir.socialhabits.repositories.PasswordResetTokenRepository;
import com.unir.socialhabits.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;

    private final ProfessionalRepository professionalRepository;

    private final PasswordResetTokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO request) {

        Professional professional = professionalRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        boolean validPassword = passwordEncoder.matches(
                request.getPassword(),
                professional.getPassword()
        );

        if (!validPassword) {
            throw new RuntimeException("Incorrect password");
        }

        String token = jwtService.generateToken(professional.getEmail());

        return new LoginResponseDTO(token);
    }

    /**
     * Generates password recovery token.
     */
    public void sendPasswordReset(
            String email
    ){

        Professional professional =
                professionalRepository
                        .findByEmail(email)
                        .orElseThrow();

        String token =
                UUID.randomUUID().toString();

        tokenRepository.deleteByEmail(email);

        PasswordResetToken resetToken =
                new PasswordResetToken();

        resetToken.setToken(token);

        resetToken.setEmail(email);

        resetToken.setExpiryDate(
                LocalDateTime.now()
                        .plusMinutes(15)
        );

        tokenRepository.save(resetToken);

    }

    /**
     * Updates encrypted password.
     */
    public void resetPassword(
            String token,
            String newPassword
    ){

        PasswordResetToken resetToken =
                tokenRepository
                        .findByToken(token)
                        .orElseThrow();

        Professional professional =
                professionalRepository
                        .findByEmail(
                                resetToken.getEmail()
                        )
                        .orElseThrow();

        professional.setPassword(
                passwordEncoder.encode(
                        newPassword
                )
        );

        professionalRepository.save(
                professional
        );

        tokenRepository.delete(
                resetToken
        );

    }

    public void register(
            RegisterProfessionalDTO dto
    ){

        boolean exists =
                professionalRepository
                        .findByEmail(
                                dto.getEmail()
                        )
                        .isPresent();

        if(exists){

            throw new RuntimeException(
                    "Email already exists"
            );

        }

        Professional professional =

                Professional.builder()

                        .name(
                                dto.getName()
                        )

                        .email(
                                dto.getEmail()
                        )

                        .password(

                                passwordEncoder.encode(
                                        dto.getPassword()
                                )

                        )

                        .build();

        professionalRepository.save(
                professional
        );

    }
}
