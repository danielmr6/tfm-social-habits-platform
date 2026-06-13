package com.unir.socialhabits.services;

import com.unir.socialhabits.dto.LoginRequestDTO;
import com.unir.socialhabits.dto.LoginResponseDTO;
import com.unir.socialhabits.dto.RegisterProfessionalDTO;
import com.unir.socialhabits.entities.Professional;
import com.unir.socialhabits.entities.PasswordResetToken;
import com.unir.socialhabits.repositories.ProfessionalRepository;
import com.unir.socialhabits.repositories.PasswordResetTokenRepository;
import com.unir.socialhabits.security.JwtService;
import com.unir.socialhabits.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;

    private final ProfessionalRepository professionalRepository;

    private final PasswordResetTokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public LoginResponseDTO login(LoginRequestDTO request) {

        Professional professional =
                professionalRepository.findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException("Invalid credentials"));

        if (professional.getLockUntil() != null &&
                professional.getLockUntil().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Account temporarily locked");
        }

        boolean validPassword = passwordEncoder.matches(
                request.getPassword(),
                professional.getPassword()
        );

        if (!validPassword) {
            professional.setLoginAttempts(
                    professional.getLoginAttempts() + 1
            );

            if (professional.getLoginAttempts() >= 5) {
                professional.setLockUntil(
                        LocalDateTime.now().plusMinutes(15)
                );
            }

            professionalRepository.save(professional);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }

        professional.setLoginAttempts(0);
        professional.setLockUntil(null);
        professionalRepository.save(professional);
        String token = jwtService.generateToken(professional.getEmail());

        return new LoginResponseDTO(token);
    }

    @Transactional
    public void sendPasswordReset(String email){

        Professional professional =
                professionalRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Invalid request"
                                        )
                        );

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

        String link = "http://localhost:3000/reset-password?token=" + token;

        emailService.send(
                email,
                "Password recovery",
                "Click here to reset your password:\n" + link
        );
    }

    /**
     * Updates encrypted password.
     */
    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken =
                tokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid token"
                                )
                        );

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Token expired"
            );
        }

        Professional professional =
                professionalRepository.findByEmail(resetToken.getEmail())
                        .orElseThrow();

        professional.setPassword(
                passwordEncoder.encode(newPassword)
        );

        professionalRepository.save(professional);

        tokenRepository.delete(resetToken);
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
