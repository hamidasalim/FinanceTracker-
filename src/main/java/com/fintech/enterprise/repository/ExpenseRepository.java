package com.fintech.enterprise.repository;

import com.fintech.enterprise.model.Expense;
import com.fintech.enterprise.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Expense entity operations.
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Custom query methods based on your requirements:

    // Get expenses by the user who submitted them
    List<Expense> findBySubmittedById(Long userId);

    // Get expenses charged to a specific department
    List<Expense> findByDepartmentId(Long departmentId);

    // Get expenses by category
    List<Expense> findByCategory(ExpenseCategory category);

    // Get the biggest expense submitted this month
    @Query(value = "SELECT e FROM Expense e WHERE MONTH(e.dateSubmitted) = MONTH(CURRENT_DATE()) AND YEAR(e.dateSubmitted) = YEAR(CURRENT_DATE()) ORDER BY e.amount DESC LIMIT 1")
    Optional<Expense> findBiggestExpenseThisMonth();
}