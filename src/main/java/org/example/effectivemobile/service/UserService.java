package org.example.effectivemobile.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.RequiredTypes;
import org.example.effectivemobile.models.Users;
import org.example.effectivemobile.repositories.UsersRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public Optional<Users> findByLogin(String login){
        return usersRepository.findByLogin(login);
    }

    public Optional<Users> findById(Long id){
        return usersRepository.findById(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Users users = findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден")
        ));
        return new User(users.getLogin(), users.getPassword(), Collections.emptyList());
    }

    @Transactional
    public boolean changePhoneNumber(Long userId, String currentPhoneNumber, String newPhoneNumber) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            if (user.getPhoneNumber().equals(currentPhoneNumber)) {
                user.setPhoneNumber(newPhoneNumber);
                usersRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
