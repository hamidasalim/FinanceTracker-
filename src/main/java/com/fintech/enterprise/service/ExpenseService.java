package com.fintech.enterprise.service;

import com.fintech.enterprise.model.Expense;
import com.fintech.enterprise.model.ExpenseCategory;
import com.fintech.enterprise.service.dto.ExpenseRequestDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Expense entities and business logic.
 */
public interface ExpenseService {

    // --- Core CRUD ---
    Expense createExpense(ExpenseRequestDTO expenseDto);
    Expense updateExpense(Long id, Expense expenseDetails);
    void deleteExpense(Long id);

    // --- Read Operations ---
    Optional<Expense> findExpenseById(Long id);
    List<Expense> findAllExpenses();
    List<Expense> findExpensesByDepartment(Long departmentId);
    List<Expense> findExpensesByUser(Long userId);
    List<Expense> findExpensesByCategory(ExpenseCategory category);

    // --- Financial Insights ---
    Optional<Expense> getBiggestExpenseThisMonth();

    // --- Approval Workflow ---
    Expense approveExpense(Long expenseId);
    Expense denyExpense(Long expenseId, Long reviewerId);
}