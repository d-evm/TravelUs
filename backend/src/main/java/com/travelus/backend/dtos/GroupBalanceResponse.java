package com.travelus.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class GroupBalanceResponse {
    private List<Owe> youOwe;
    private List<Owe> youAreOwed;

    @Data
    public static class Owe {
        private String username;
        private double amount;
    }
}
