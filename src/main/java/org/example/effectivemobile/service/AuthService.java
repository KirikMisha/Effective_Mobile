package org.example.effectivemobile.service;

import org.example.effectivemobile.dto.RegistrationUserDto;
import org.example.effectivemobile.models.BankAccounts;
import org.example.effectivemobile.models.Users;
import org.example.effectivemobile.repositories.BankAccountsRepository;
import org.example.effectivemobile.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsersRepository usersRepository;
    private final BankAccountsRepository bankAccountsRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UsersRepository usersRepository, BankAccountsRepository bankAccountsRepository ,BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.bankAccountsRepository = bankAccountsRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public ResponseEntity<String> registerUser(RegistrationUserDto registrationUserDto) {
        if(usersRepository.existsByEmailOrFullNameOrLoginOrPhoneNumber(registrationUserDto.getEmail(), registrationUserDto.getFullName(), registrationUserDto.getLogin(), registrationUserDto.getPhoneNumber())) {
            return ResponseEntity.badRequest().body("Такой пользователь с такими данными уже существует");
        }

        Users newUser = new Users();
        newUser.setFullName(registrationUserDto.getFullName());
        newUser.setLogin(registrationUserDto.getLogin());
        newUser.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        newUser.setEmail(registrationUserDto.getEmail());
        newUser.setPhoneNumber(registrationUserDto.getPhoneNumber());
        newUser.setDateOfBirth(registrationUserDto.getDateOfBirth());
        try {
            Users savedUser = usersRepository.save(newUser);

            BankAccounts newBankAccounts = new BankAccounts();
            newBankAccounts.setInitialBalance(registrationUserDto.getInitialBalance());
            newBankAccounts.setUser(savedUser);

            bankAccountsRepository.save(newBankAccounts);

            return ResponseEntity.ok("Регистрация прошла успешно");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при попытке сохранить нового пользователя");
        }
    }

}
