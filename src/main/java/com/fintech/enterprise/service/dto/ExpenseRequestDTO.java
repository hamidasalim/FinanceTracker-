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

    private String title;
    private String description;
    private BigDecimal amount;
    private ExpenseCategory category;
    private Long departmentId;

}