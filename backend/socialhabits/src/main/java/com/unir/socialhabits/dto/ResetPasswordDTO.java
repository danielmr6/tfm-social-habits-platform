package com.unir.socialhabits.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {

    private String token;

    @NotBlank
    @Size(min = 8, max = 50)
    private String newPassword;
}