package org.example.effectivemobile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePhoneDto {

    @NotBlank(message = "Поле не может быть пустым")
    private String currentPhoneNumber;

    @NotBlank(message = "Поле не может быть пустым")
    private String newPhoneNumber;
}