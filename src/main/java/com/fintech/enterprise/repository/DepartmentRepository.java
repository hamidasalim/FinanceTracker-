package com.fintech.enterprise.repository;

import com.fintech.enterprise.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for Department entity.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // Custom query methods can be added here if needed
}

