package com.travelus.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ExpenseResponse {
    private String title;
    private Double amount;
    private String paidBy;
    private List<Share> shares;

    @Data
    public static class Share {
        private String username;
        private Double amount;
    }
}
