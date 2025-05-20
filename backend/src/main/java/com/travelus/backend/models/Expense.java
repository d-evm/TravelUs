package com.travelus.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Group group;

    private String title;

    @ManyToOne
    private User paidBy;

    private Double amount;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<ExpenseShare> shares;
}
