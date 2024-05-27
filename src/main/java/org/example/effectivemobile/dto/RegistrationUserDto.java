package org.example.effectivemobile.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class RegistrationUserDto {

    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @NotBlank(message = "Полное имя не может быть пустым")
    private String fullName;

    @NotNull(message = "Дата рождения не может быть пустой")
    private String dateOfBirth;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Неверный формат номера телефона")
    private String phoneNumber;

    @Email(message = "Неверный формат адреса электронной почты")
    private String email;

    @PositiveOrZero(message = "Начальный баланс не может быть отрицательным")
    private double initialBalance;
}
