package org.example.effectivemobile.controlles;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobile.dto.ChangePhoneDto;
import org.example.effectivemobile.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChangePhoneOrEmailController {

    private final UserService userService;

    @PostMapping("/change/phone")
    public ResponseEntity<?> changePhone(@RequestBody ChangePhoneDto changePhoneDto) {
        try {
            Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            boolean isChanged = userService.changePhoneNumber(userId, changePhoneDto.getCurrentPhoneNumber(), changePhoneDto.getNewPhoneNumber());
            if (isChanged) {
                return ResponseEntity.ok("Номер телефона успешно обновлен");
            } else {
                return ResponseEntity.status(400).body("Текущий номер телефона не соответствует");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Произошла ошибка: " + e.getMessage());
        }
    }
}
