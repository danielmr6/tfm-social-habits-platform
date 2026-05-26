package com.unir.socialhabits.config;

import com.unir.socialhabits.entities.Professional;
import com.unir.socialhabits.repositories.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProfessionalRepository professionalRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (professionalRepository.findByEmail("admin@test.com").isEmpty()) {

            Professional professional = Professional.builder()
                    .name("Admin")
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .role("ADMIN")
                    .build();

            professionalRepository.save(professional);
        }
    }
}