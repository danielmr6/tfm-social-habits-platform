package com.unir.socialhabits.services;

import com.unir.socialhabits.dto.*;
import com.unir.socialhabits.entities.*;
import com.unir.socialhabits.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObservationService {

    private final ObservationRepository observationRepository;

    private final UserRepository userRepository;

    private final ProfessionalRepository professionalRepository;

    private Professional getLoggedProfessional(){

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return professionalRepository
                .findByEmail(email)
                .orElseThrow();
    }

    public ObservationDTO createObservation(
            UUID userId,
            CreateObservationDTO dto
    ){

        User user =
                userRepository.findById(userId)
                        .orElseThrow();

        Professional professional =
                getLoggedProfessional();

        Observation observation =
                Observation.builder()

                        .user(user)

                        .professional(professional)

                        .content(dto.getContent())

                        .build();

        Observation saved =
                observationRepository.save(
                        observation
                );

        return map(saved);

    }

    private ObservationDTO map(
            Observation observation
    ){

        ObservationDTO dto =
                new ObservationDTO();

        dto.setId(
                observation.getId()
        );

        dto.setContent(
                observation.getContent()
        );

        dto.setCreatedAt(
                observation.getCreatedAt()
        );

        dto.setProfessionalName(
                observation
                        .getProfessional()
                        .getName()
        );

        return dto;
    }

    public void deleteObservation(UUID id) {
        observationRepository.deleteById(id);
    }
}