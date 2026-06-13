package com.unir.socialhabits.controllers;

import com.unir.socialhabits.dto.*;

import com.unir.socialhabits.services.ObservationService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/observations")
@RequiredArgsConstructor
public class ObservationController {

    private final ObservationService observationService;

    @PostMapping("/{userId}")
    public ObservationDTO createObservation(

            @PathVariable UUID userId,

            @Valid @RequestBody CreateObservationDTO dto

    ){

        return observationService
                .createObservation(
                        userId,
                        dto
                );

    }

    @DeleteMapping("/{observationId}")
    public void deleteObservation(
            @PathVariable UUID observationId
    ) {
        observationService.deleteObservation(observationId);
    }

}