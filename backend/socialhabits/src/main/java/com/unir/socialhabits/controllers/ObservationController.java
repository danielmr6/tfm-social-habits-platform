package com.unir.socialhabits.controllers;

import com.unir.socialhabits.dto.*;

import com.unir.socialhabits.services.ObservationService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/observations")
@RequiredArgsConstructor
@Tag(name = "Observations", description = "User observations management")
public class ObservationController {

    private final ObservationService observationService;

    @Operation(summary = "Create observation")
    @PostMapping("/{userId}")
    public ObservationDTO createObservation(
            @PathVariable UUID userId,
            @Valid @RequestBody CreateObservationDTO dto
    ) {
        return observationService.createObservation(userId, dto);
    }

    @Operation(summary = "Delete observation")
    @DeleteMapping("/{observationId}")
    public void deleteObservation(@PathVariable UUID observationId) {
        observationService.deleteObservation(observationId);
    }
}