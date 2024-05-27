package org.example.effectivemobile.controlles;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobile.dto.TransferRequest;
import org.example.effectivemobile.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/api/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest transferRequest) {
        try {
            Long senderId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            transferService.transferMoney(senderId, transferRequest.getRecipientPhone(), transferRequest.getAmount());
            return ResponseEntity.ok("Transfer successful");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Произошла ошибка: " + e.getMessage());
        }
    }
}
