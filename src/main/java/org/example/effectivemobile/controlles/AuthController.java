package org.example.effectivemobile.controlles;

import jakarta.validation.Valid;
import org.example.effectivemobile.Exceptions.AppError;
import org.example.effectivemobile.dto.JwtRequest;
import org.example.effectivemobile.dto.JwtResponse;
import org.example.effectivemobile.dto.RegistrationUserDto;
import org.example.effectivemobile.models.Users;
import org.example.effectivemobile.service.AuthService;
import org.example.effectivemobile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private final AuthService authService;
    private final UserService userService;
    private final org.example.effectivemobile.utils.JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public AuthController(
            AuthService authService, UserService userService,
            org.example.effectivemobile.utils.JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager
    ) {
        this.authService = authService;
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody RegistrationUserDto registrationUserDto, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        return authService.registerUser(registrationUserDto);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getLogin(), authRequest.getPassword())
            );
        }catch (BadCredentialsException e){
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Некоректный логин или пароль"),
                    HttpStatus.UNAUTHORIZED);
        }
        Users user = userService.findByLogin(authRequest.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        String token = jwtTokenUtils.generateToken(user.getId());
        return ResponseEntity.ok(new JwtResponse(token));

    }
}
