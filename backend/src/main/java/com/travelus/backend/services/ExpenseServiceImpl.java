package com.travelus.backend.services;

import com.travelus.backend.dtos.ExpenseRequest;
import com.travelus.backend.dtos.ExpenseResponse;
import com.travelus.backend.dtos.GroupBalanceResponse;
import com.travelus.backend.dtos.SimplifiedTransaction;
import com.travelus.backend.models.*;
import com.travelus.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ExpenseServiceImpl implements ExpenseService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    GroupMemberRepository groupMemberRepository;

    @Autowired
    ExpenseShareRepository expenseShareRepository;

    @Override
    public void addExpense(ExpenseRequest request, String username) {
        User payer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        boolean isMember = groupMemberRepository.findByGroupAndUserUsername(group, username).isPresent();
        if (!isMember) throw new RuntimeException("You are not a member of this group");

        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setGroup(group);
        expense.setPaidBy(payer);
        Expense savedExpense = expenseRepository.save(expense);

        List<GroupMember> members = groupMemberRepository.findByGroup(group);

        Set<Long> seenUserIds = new HashSet<>();  // Ensure unique users
        List<User> uniqueUsers = new ArrayList<>();

        for (GroupMember member : members) {
            User user = member.getUser();
            if (!seenUserIds.contains(user.getId())) {
                seenUserIds.add(user.getId());
                uniqueUsers.add(user);
            }
        }

        double splitAmount = request.getAmount() / uniqueUsers.size();

        for (User user : uniqueUsers) {
            ExpenseShare share = new ExpenseShare();
            share.setExpense(savedExpense);
            share.setUser(user);
            share.setShareAmount(splitAmount);
            expenseShareRepository.save(share);
        }
    }


    public List<ExpenseResponse> getExpensesByGroup (Long groupId, String username){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        boolean isMemberOfGroup = groupMemberRepository.findByGroupAndUserUsername(group, username).isPresent();
        if (!isMemberOfGroup) throw new RuntimeException("Unauthorized");

        List<Expense> expenses = expenseRepository.findByGroupId(groupId);

        return expenses.stream().map(exp -> {
            ExpenseResponse res = new ExpenseResponse();
            res.setTitle(exp.getTitle());
            res.setAmount(exp.getAmount());
            res.setPaidBy(exp.getPaidBy().getUsername());

            List<ExpenseResponse.Share> shares = expenseShareRepository.findByExpenseId(exp.getId())
                    .stream()
                    .map(share -> {
                        ExpenseResponse.Share s = new ExpenseResponse.Share();
                        s.setUsername(share.getUser().getUsername());
                        s.setAmount(share.getShareAmount());
                        return s;
                    }).collect(Collectors.toList());

            res.setShares(shares);
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public GroupBalanceResponse calculateBalance(Long groupId, String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<Expense> expenses = expenseRepository.findByGroup(group);

        Map<String, Double> netBalances = new HashMap<>();

        for (Expense expense : expenses) {
            User payer = expense.getPaidBy();
            List<ExpenseShare> shares = expenseShareRepository.findByExpense(expense);

            for (ExpenseShare share : shares) {
                User user = share.getUser();

                if (user.getId().equals(currentUser.getId()) && !payer.getId().equals(currentUser.getId())) {
                    // current user owes this amount to payer
                    netBalances.put(payer.getUsername(),
                            netBalances.getOrDefault(payer.getUsername(), 0.0) - share.getShareAmount());
                } else if (!user.getId().equals(currentUser.getId()) && payer.getId().equals(currentUser.getId())) {
                    // other user owes current user
                    netBalances.put(user.getUsername(),
                            netBalances.getOrDefault(user.getUsername(), 0.0) + share.getShareAmount());
                }
            }
        }

        List<GroupBalanceResponse.Owe> result = netBalances.entrySet().stream()
                .filter(entry -> Math.abs(entry.getValue()) > 0.01) // remove near-zero noise
                .map(entry -> {
                    GroupBalanceResponse.Owe entryResult = new GroupBalanceResponse.Owe();
                    entryResult.setUsername(entry.getKey());
                    entryResult.setAmount(Math.round(Math.abs(entry.getValue()) * 100.0) / 100.0);
                    return entryResult;
                })
                .toList();

        GroupBalanceResponse response = new GroupBalanceResponse();
        response.setYouOwe(new ArrayList<>());
        response.setYouAreOwed(new ArrayList<>());

        for (Map.Entry<String, Double> entry : netBalances.entrySet()) {
            double val = entry.getValue();
            if (val < -0.01) {
                // current user owes them
                GroupBalanceResponse.Owe owe = new GroupBalanceResponse.Owe();
                owe.setUsername(entry.getKey());
                owe.setAmount(Math.round(Math.abs(val) * 100.0) / 100.0);
                response.getYouOwe().add(owe);
            } else if (val > 0.01) {
                // they owe current user
                GroupBalanceResponse.Owe owed = new GroupBalanceResponse.Owe();
                owed.setUsername(entry.getKey());
                owed.setAmount(Math.round(val * 100.0) / 100.0);
                response.getYouAreOwed().add(owed);
            }
        }

        return response;
    }

    @Override
    public List<SimplifiedTransaction> getSimplifiedBalances(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        List<Expense> expenses = expenseRepository.findByGroup(group);

        Map<String, Double> balanceMap = new HashMap<>();

        for (Expense expense : expenses) {
            User payer = expense.getPaidBy();
            List<ExpenseShare> shares = expenseShareRepository.findByExpense(expense);

            for (ExpenseShare share : shares) {
                String user = share.getUser().getUsername();
                double amount = share.getShareAmount();

                balanceMap.put(user, balanceMap.getOrDefault(user, 0.0) - amount);

                String payerUsername = payer.getUsername();
                balanceMap.put(payerUsername, balanceMap.getOrDefault(payerUsername, 0.0) + amount);
            }
        }

        PriorityQueue<UserBalance> debtors = new PriorityQueue<>(Comparator.comparingDouble(u -> u.amount)); // most negative first
        PriorityQueue<UserBalance> creditors = new PriorityQueue<>((a, b) -> Double.compare(b.amount, a.amount)); // most positive first

        for (Map.Entry<String, Double> entry : balanceMap.entrySet()) {
            double val = Math.round(entry.getValue() * 100.0) / 100.0;
            if (Math.abs(val) < 0.01) continue;

            if (val < 0) debtors.add(new UserBalance(entry.getKey(), val));
            else creditors.add(new UserBalance(entry.getKey(), val));
        }

        List<SimplifiedTransaction> result = new ArrayList<>();

        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            UserBalance debtor = debtors.poll();
            UserBalance creditor = creditors.poll();

            double settlementAmount = Math.min(-debtor.amount, creditor.amount);
            settlementAmount = Math.round(settlementAmount * 100.0) / 100.0;

            result.add(new SimplifiedTransaction(debtor.user, creditor.user, settlementAmount));

            double newDebtorBal = debtor.amount + settlementAmount;
            double newCreditorBal = creditor.amount - settlementAmount;

            if (newDebtorBal < -0.01) debtors.add(new UserBalance(debtor.user, newDebtorBal));
            if (newCreditorBal > 0.01) creditors.add(new UserBalance(creditor.user, newCreditorBal));
        }

        return result;
    }

    private record UserBalance(String user, double amount) {}

}
