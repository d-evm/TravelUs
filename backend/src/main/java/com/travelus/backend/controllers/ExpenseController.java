package com.travelus.backend.controllers;

import com.travelus.backend.dtos.ExpenseRequest;
import com.travelus.backend.dtos.GroupBalanceResponse;
import com.travelus.backend.dtos.SimplifiedTransaction;
import com.travelus.backend.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    @Autowired
    ExpenseService expenseService;

    @PostMapping("/add")
    public ResponseEntity<?> addExpense (@RequestBody ExpenseRequest request,
                                         @AuthenticationPrincipal UserDetails userDetails){
        expenseService.addExpense(request, userDetails.getUsername());

        return ResponseEntity.ok("Expense added successfully.");
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getGroupExpenses (@PathVariable Long groupId,
                                               @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(expenseService.getExpensesByGroup(groupId, userDetails.getUsername()));

    }

    @GetMapping("/group/{groupId}/balance")
    public ResponseEntity<GroupBalanceResponse> getBalance (@PathVariable Long groupId,
                                                            @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(expenseService.calculateBalance(groupId, userDetails.getUsername()));
    }

    @GetMapping("/group/{groupId}/settle")
    public ResponseEntity<List<SimplifiedTransaction>> getSettlement(@PathVariable Long groupId) {
        return ResponseEntity.ok(expenseService.getSimplifiedBalances(groupId));
    }

}
