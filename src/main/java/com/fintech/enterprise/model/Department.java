package com.fintech.enterprise.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Entity
@Data
@NoArgsConstructor

public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal yearlyBudget; // Total budget allocated

    @Column(nullable = false)
    private BigDecimal spentAmount = BigDecimal.ZERO; // Total amount spent so far

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    private List<User> members;

    // --- Explicit Constructors ---

    public Department(Long id, String name, BigDecimal yearlyBudget, BigDecimal spentAmount) {
        this.id = id;
        this.name = name;
        this.yearlyBudget = yearlyBudget;
        this.spentAmount = spentAmount;
    }
}