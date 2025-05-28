package com.travelus.backend.repositories;

import com.travelus.backend.models.Expense;
import com.travelus.backend.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroupId(Long groupId);
    List<Expense> findByPaidById(Long userId);
    List<Expense> findByGroup(Group group);
}
