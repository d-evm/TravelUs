package com.travelus.backend.services;

import com.travelus.backend.dtos.ExpenseRequest;
import com.travelus.backend.dtos.ExpenseResponse;
import com.travelus.backend.dtos.GroupBalanceResponse;
import com.travelus.backend.dtos.SimplifiedTransaction;

import java.util.List;

public interface ExpenseService {
    void addExpense (ExpenseRequest request, String username);
    List<ExpenseResponse> getExpensesByGroup (Long groupId, String username);
    GroupBalanceResponse calculateBalance (Long groupId, String username);
    List<SimplifiedTransaction> getSimplifiedBalances(Long groupId);

}
