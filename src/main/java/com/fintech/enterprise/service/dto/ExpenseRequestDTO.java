package com.fintech.enterprise.service.dto;

import com.fintech.enterprise.model.ExpenseCategory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequestDTO {

    // Fields that the user submits directly
    private String title;
    private String description;
    private BigDecimal amount;
    private ExpenseCategory category;

    // Fields for relationships, provided as IDs (safest way to map)
    private Long departmentId; // Only need the ID of the Department

    // NOTE: We intentionally OMIT 'submittedBy' and 'dateSubmitted'
    // because they will be set by the server!
}