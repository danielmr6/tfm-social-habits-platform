package com.unir.socialhabits.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateObservationDTO {

    @NotBlank(message = "Content is required")
    @Size(max = 2000)
    private String content;
}