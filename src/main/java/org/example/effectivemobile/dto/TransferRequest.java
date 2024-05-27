package org.example.effectivemobile.dto;

import lombok.Data;

@Data
public class TransferRequest {
    private String recipientPhone;
    private double amount;
}
