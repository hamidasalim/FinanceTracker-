package com.fintech.enterprise.controller;

import com.fintech.enterprise.model.Expense;
import com.fintech.enterprise.model.ExpenseCategory;
import com.fintech.enterprise.service.ExpenseService;
import com.fintech.enterprise.service.dto.ExpenseRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // --- Core CRUD ---

    @PostMapping("/submit")
    public ResponseEntity<Expense> createExpense(
            @RequestBody ExpenseRequestDTO expenseDto, // *** CHANGED: Use DTO ***
            Principal principal) {                     // *** ADDED: Get authenticated user ***

        // Pass the DTO and the user's name (ID/username) to the service
        Expense newExpense = expenseService.createExpense(expenseDto);
        return new ResponseEntity<>(newExpense, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expenseDetails) {
        try {
            Expense updatedExpense = expenseService.updateExpense(id, expenseDetails);
            return ResponseEntity.ok(updatedExpense);
        } catch (Exception e) {
            // Handle EntityNotFoundException or IllegalStateException
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    // --- Read Operations ---

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return expenseService.findExpenseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.findAllExpenses();
    }

    @GetMapping("/user/{userId}")
    public List<Expense> getExpensesByUser(@PathVariable Long userId) {
        return expenseService.findExpensesByUser(userId);
    }

    @GetMapping("/department/{deptId}")
    public List<Expense> getExpensesByDepartment(@PathVariable Long deptId) {
        return expenseService.findExpensesByDepartment(deptId);
    }

    @GetMapping("/category/{category}")
    public List<Expense> getExpensesByCategory(@PathVariable String category) {
        try {
            ExpenseCategory expenseCategory = ExpenseCategory.valueOf(category.toUpperCase());
            return expenseService.findExpensesByCategory(expenseCategory);
        } catch (IllegalArgumentException e) {
            // Log or handle the case where the category string is invalid
            return List.of();
        }
    }

    // --- Approval Workflow ---

    @PatchMapping("/{id}/approve")
    // Role checks (e.g., using Spring Security annotations like @PreAuthorize)
    // would be added here to ensure only Admin/Manager can approve.
    public ResponseEntity<Expense> approveExpense(@PathVariable Long id) {
        try {
            Expense approvedExpense = expenseService.approveExpense(id);
            return ResponseEntity.ok(approvedExpense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/deny/{reviewerId}")
    public ResponseEntity<Expense> denyExpense(@PathVariable Long id, @PathVariable Long reviewerId) {
        try {
            Expense deniedExpense = expenseService.denyExpense(id, reviewerId);
            return ResponseEntity.ok(deniedExpense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- Financial Insights ---

    @GetMapping("/insight/biggest-this-month")
    public ResponseEntity<Expense> getBiggestExpenseThisMonth() {
        return expenseService.getBiggestExpenseThisMonth()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build()); // Return 204 if no expenses this month
    }
}