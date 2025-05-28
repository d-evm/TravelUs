package com.travelus.backend.repositories;

import com.travelus.backend.models.Expense;
import com.travelus.backend.models.ExpenseShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseShareRepository extends JpaRepository<ExpenseShare, Long> {
    List<ExpenseShare> findByUserId(Long userId);
    List<ExpenseShare> findByExpenseId(Long expenseId);
    List<ExpenseShare> findByExpense(Expense expense);
}