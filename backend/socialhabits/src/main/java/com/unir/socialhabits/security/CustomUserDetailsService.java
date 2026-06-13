package com.unir.socialhabits.security;

import com.unir.socialhabits.entities.Professional;
import com.unir.socialhabits.repositories.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ProfessionalRepository professionalRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Professional professional = professionalRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + email)
                );

        String role = professional.getRole() != null
                ? professional.getRole().name()
                : "USER";

        return User.builder()
                .username(professional.getEmail())
                .password(professional.getPassword())
                .authorities("ROLE_" + role)
                .build();
    }
}