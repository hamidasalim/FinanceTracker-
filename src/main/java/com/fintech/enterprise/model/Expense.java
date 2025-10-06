package com.fintech.enterprise.model;

import com.fasterxml.jackson.annotation.JsonManagedReference; // NEW IMPORT
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents an expense submitted by a user for approval.
 */
@Entity
@Table(name = "expense")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column( nullable = false)
    private LocalDate dateSubmitted = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseStatus status = ExpenseStatus.PENDING; // Default status

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    // --- Relationships ---

    // The user who submitted the expense. (Many expenses to one user)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    // @JsonManagedReference is used here to include the User when Expense is serialized.
    // The 'User' model must use @JsonIgnore or @JsonBackReference on its 'expenses' list.
    @JsonManagedReference
    private User submittedBy;

    // The department to which the expense is charged. (Many expenses to one department)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonManagedReference // Same rationale as for User
    private Department department;

    // The user who approved/denied the expense (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewedBy;

    private LocalDate dateReviewed;

    @PrePersist
    protected void onCreate() {
        if (dateSubmitted == null) {
            dateSubmitted = LocalDate.now();
        }
    }
}