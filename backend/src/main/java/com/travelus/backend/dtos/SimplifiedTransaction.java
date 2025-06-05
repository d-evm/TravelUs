package com.travelus.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimplifiedTransaction {
    private String from;
    private String to;
    private double amount;
}
