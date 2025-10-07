package com.fintech.enterprise.service;

import com.fintech.enterprise.model.Expense;
import com.fintech.enterprise.model.ExpenseCategory;
import com.fintech.enterprise.model.ExpenseStatus;
import com.fintech.enterprise.repository.DepartmentRepository; // ADDED: New import for Department Repository
import com.fintech.enterprise.repository.ExpenseRepository;
import com.fintech.enterprise.service.DepartmentService;
import com.fintech.enterprise.service.UserService;
import com.fintech.enterprise.service.dto.ExpenseRequestDTO;
import com.fintech.enterprise.model.User;
import com.fintech.enterprise.model.Department;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository; // ADDED: New field for DepartmentRepository

    // Constructor Injection (Best practice)
    public ExpenseServiceImpl(
            ExpenseRepository expenseRepository,
            UserService userService,
            DepartmentService departmentService,
            DepartmentRepository departmentRepository) { // ADDED: Inject DepartmentRepository

        this.expenseRepository = expenseRepository;
        this.userService = userService;
        this.departmentService = departmentService;
        this.departmentRepository = departmentRepository; // Initialize the new field
    }

    // --- Core CRUD ---

    @Override
    public Expense createExpense(ExpenseRequestDTO expenseDto) {

        // 1. Fetch the necessary entities (will throw an exception if not found)

        // FIX 1: Use the injected 'userService' to find the User
        User submittedBy = userService.getCurrentAuthenticatedUser();

        // FIX 2: Use the injected 'departmentRepository' to find the Department
        Department department = departmentRepository.findById(expenseDto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found for ID: " + expenseDto.getDepartmentId()));

        // 2. Create and populate the final Expense entity
        Expense newExpense = new Expense();

        // Map fields from DTO
        newExpense.setTitle(expenseDto.getTitle());
        newExpense.setDescription(expenseDto.getDescription());
        newExpense.setAmount(expenseDto.getAmount());
        newExpense.setCategory(expenseDto.getCategory());

        // Set server-managed fields
        newExpense.setDateSubmitted(LocalDate.now());
        newExpense.setStatus(ExpenseStatus.PENDING);

        // Set relationship entities
        newExpense.setSubmittedBy(submittedBy);
        newExpense.setDepartment(department);

        // 3. Save the entity
        return expenseRepository.save(newExpense);
    }

    @Override
    public Expense updateExpense(Long id, Expense expenseDetails) {
        return expenseRepository.findById(id).map(existingExpense -> {
            // Only update if PENDING
            if (existingExpense.getStatus() != ExpenseStatus.PENDING) {
                throw new IllegalStateException("Cannot update an expense that is not PENDING.");
            }
            // Copy fields from expenseDetails to existingExpense
            existingExpense.setTitle(expenseDetails.getTitle());
            existingExpense.setDescription(expenseDetails.getDescription());
            existingExpense.setAmount(expenseDetails.getAmount());
            existingExpense.setCategory(expenseDetails.getCategory());
            // Relationships (User/Dept) should typically not change after creation, but you can add logic here if needed.

            return expenseRepository.save(existingExpense);
        }).orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + id));
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    // --- Read Operations ---

    @Override
    public Optional<Expense> findExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    @Override
    public List<Expense> findAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> findExpensesByDepartment(Long departmentId) {
        return expenseRepository.findByDepartmentId(departmentId);
    }

    @Override
    public List<Expense> findExpensesByUser(Long userId) {
        return expenseRepository.findBySubmittedById(userId);
    }

    @Override
    public List<Expense> findExpensesByCategory(ExpenseCategory category) {
        return expenseRepository.findByCategory(category);
    }

    // --- Financial Insights ---

    @Override
    public Optional<Expense> getBiggestExpenseThisMonth() {
        return expenseRepository.findBiggestExpenseThisMonth();
    }

    // --- Approval Workflow ---

    private Expense changeStatus(Long expenseId, Long reviewerId, ExpenseStatus newStatus) {
        return expenseRepository.findById(expenseId).map(expense -> {
            // Check if expense is already reviewed (optional but good)
            if (expense.getStatus() != ExpenseStatus.PENDING) {
                throw new IllegalStateException("Expense has already been reviewed.");
            }

            expense.setReviewedBy(userService.findUserById(reviewerId)
                    .orElseThrow(() -> new EntityNotFoundException("Reviewer not found with ID: " + reviewerId)));

            expense.setStatus(newStatus);
            expense.setDateReviewed(LocalDate.now());

            if (newStatus == ExpenseStatus.APPROVED) {
            }

            return expenseRepository.save(expense);
        }).orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + expenseId));
    }

    @Override
    public Expense approveExpense(Long expenseId) {
        User user= userService.getCurrentAuthenticatedUser();

        return changeStatus(expenseId, user.getId(), ExpenseStatus.APPROVED);
    }

    @Override
    public Expense denyExpense(Long expenseId, Long reviewerId) {
        return changeStatus(expenseId, reviewerId, ExpenseStatus.DENIED);
    }
}