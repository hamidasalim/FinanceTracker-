package com.fintech.enterprise.service;

import com.fintech.enterprise.model.Department;
import com.fintech.enterprise.service.dto.BudgetOverviewDTO;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    // --- CRUD Operations ---
    Department createDepartment(Department department);

    List<Department> findAllDepartments();

    Optional<Department> findDepartmentById(Long id);

    Department updateDepartment(Long id, Department departmentDetails);

    void deleteDepartment(Long id);

    // --- Membership Operations ---


    Department addMemberToDepartment(Long departmentId, Long userId);


    Department removeMemberFromDepartment(Long departmentId, Long userId);


    List<BudgetOverviewDTO> getBudgetOverview();
}
