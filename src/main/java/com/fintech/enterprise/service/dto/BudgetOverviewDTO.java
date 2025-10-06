package com.fintech.enterprise.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) used for summarizing the budget and spending
 * status of a department for reporting purposes.
 */
@Data
@AllArgsConstructor
public class BudgetOverviewDTO {
    private String departmentName;
    private BigDecimal yearlyBudget;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private BigDecimal remainingPercent;
    private BigDecimal spentPercent; // Added this for comprehensive reporting
}
