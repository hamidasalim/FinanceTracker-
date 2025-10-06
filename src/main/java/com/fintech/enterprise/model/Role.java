package com.fintech.enterprise.model;

/**
 * Defines the possible roles for users in the system, used for Spring Security authorization.
 */
public enum Role {
    ADMIN,      // Full access, can set budgets and manage users.
    MANAGER,    // Can view all expenses and approve/reject expenses.
    EMPLOYEE    // Can only add expenses.
}
