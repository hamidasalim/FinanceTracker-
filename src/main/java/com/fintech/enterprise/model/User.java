package com.fintech.enterprise.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // <-- NEW IMPORT: Required to break the JSON serialization loop
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List; // <-- NEW IMPORT

/**
 * Represents a system user with a specific role for access control.
 * Note: For a real app, you would add fields like 'passwordHash' and integrate with Spring Security's UserDetails.
 */
@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // Should be hashed in production!

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Relationship to Expense (The "many" side).
    // The @JsonBackReference tells Jackson to ignore this field when serializing a User object.
// Inside your User.java model file

    @JsonIgnore // Use @JsonIgnore to break the serialization loop
    @OneToMany(mappedBy = "submittedBy", fetch = FetchType.LAZY)
    private List<Expense> expenses;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = true) // <--- THIS IS THE CRITICAL CHANGE
    @JsonBackReference // Assuming you already added this to fix the loop
    private Department department;



    // Custom constructor to match the one used in InitialDataLoader
    // NOTE: Lombok's @AllArgsConstructor will conflict with the one created when adding the List<Expense>,
    // so it's safer to keep the specific constructor used by your DataLoader.
    public User(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }


}
