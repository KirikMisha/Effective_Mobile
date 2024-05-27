package org.example.effectivemobile.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private List<UserInfo> users;
    private int totalPages;
    private long totalElements;

    @Data
    public static class UserInfo {
        private String fullName;
        private String phoneNumber;
        private double balance;
    }
}
