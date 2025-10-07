package com.fintech.enterprise.repository;

import com.fintech.enterprise.model.Expense;
import com.fintech.enterprise.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {


    List<Expense> findBySubmittedById(Long userId);

    List<Expense> findByDepartmentId(Long departmentId);

    List<Expense> findByCategory(ExpenseCategory category);

    @Query(value = "SELECT e FROM Expense e WHERE MONTH(e.dateSubmitted) = MONTH(CURRENT_DATE()) AND YEAR(e.dateSubmitted) = YEAR(CURRENT_DATE()) ORDER BY e.amount DESC LIMIT 1")
    Optional<Expense> findBiggestExpenseThisMonth();
}