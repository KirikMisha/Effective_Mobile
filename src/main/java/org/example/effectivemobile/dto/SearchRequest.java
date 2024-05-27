package org.example.effectivemobile.dto;

import lombok.Data;


@Data
public class SearchRequest {
    private String dateOfBirth;
    private String phoneNumber;
    private String fullName;
    private String email;
    private int page = 0;
    private int size = 10;
    private String sortBy;
    private String sortOrder;
}
