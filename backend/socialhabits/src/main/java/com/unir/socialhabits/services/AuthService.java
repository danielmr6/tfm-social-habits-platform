package com.unir.socialhabits.services;

import com.unir.socialhabits.dto.LoginRequestDTO;
import com.unir.socialhabits.dto.LoginResponseDTO;
import com.unir.socialhabits.entities.Professional;
import com.unir.socialhabits.repositories.ProfessionalRepository;
import com.unir.socialhabits.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfessionalRepository professionalRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request) {

        Professional professional = professionalRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        boolean validPassword = passwordEncoder.matches(
                request.getPassword(),
                professional.getPassword()
        );

        if (!validPassword) {
            throw new RuntimeException("Password incorrecta");
        }

        String token = jwtService.generateToken(professional);

        return new LoginResponseDTO(token);
    }
}