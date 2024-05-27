package org.example.effectivemobile.models;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;

    @Column(name = "date_of_birth", nullable = false)
    @NotNull(message = "Дата рождения не может быть пустой")
    private String dateOfBirth;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;


}
