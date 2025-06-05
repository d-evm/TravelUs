package com.travelus.backend.dtos;

import lombok.Data;

@Data
public class ExpenseRequest {
    private Long groupId;
    private String title;
    private Double amount;
}
