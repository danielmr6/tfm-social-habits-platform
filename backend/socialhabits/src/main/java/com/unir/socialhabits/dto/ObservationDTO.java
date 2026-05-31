package com.unir.socialhabits.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ObservationDTO {

    private UUID id;

    private String content;

    private LocalDateTime createdAt;

    private String professionalName; 
}