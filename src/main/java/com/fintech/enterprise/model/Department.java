package com.fintech.enterprise.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a department with its defined budget and actual spending.
 */
@Entity
@Data
@NoArgsConstructor
// NOTE: Lombok's @AllArgsConstructor is intentionally omitted to allow for explicit constructors
// that prevent issues with the List<User> field and data loader compatibility.
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

    // Relationship: One Department has Many Users (Members)
    // The @JsonIgnore has been removed to allow the list of members to be serialized.
    // FetchType is changed to EAGER to ensure members are loaded immediately to avoid LazyInitializationException.
    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    private List<User> members;

    // --- Explicit Constructors ---

    // This explicit 4-argument constructor is required to fix the "no suitable constructor found" error
    // for classes like InitialDataLoader or test classes that only provide ID, name, budget, and spent amount.
    public Department(Long id, String name, BigDecimal yearlyBudget, BigDecimal spentAmount) {
        this.id = id;
        this.name = name;
        this.yearlyBudget = yearlyBudget;
        this.spentAmount = spentAmount;
    }
}