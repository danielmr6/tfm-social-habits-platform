package com.unir.socialhabits.services;

import com.unir.socialhabits.entities.*;
import com.unir.socialhabits.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObservationService {

    private final ObservationRepository observationRepository;
    private final UserRepository userRepository;
    private final ProfessionalRepository professionalRepository;

    public Observation create(UUID userId, UUID professionalId, String content) {

        User user = userRepository.findById(userId).orElseThrow();
        Professional prof = professionalRepository.findById(professionalId).orElseThrow();

        Observation obs = new Observation();
        obs.setUser(user);
        obs.setProfessional(prof);
        obs.setContent(content);

        return observationRepository.save(obs);
    }
}